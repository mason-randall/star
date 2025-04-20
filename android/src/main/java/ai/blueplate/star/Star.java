package ai.blueplate.star;

import android.content.Context;

import com.getcapacitor.PluginCall;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;

import java.nio.charset.StandardCharsets; // Added for explicit charset

public class Star {

    private Context context;

    public Star(Context context) {
        this.context = context;
    }

    // createData method remains unchanged, but added charset suggestion
    public static byte[] createData(StarIoExt.Emulation emulation, String value) {
        // Consider specifying a charset for consistent byte conversion
        byte[] data = value.getBytes(StandardCharsets.UTF_8); // Example: Using UTF-8

        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();

        builder.appendRaw(data);
        builder.appendRaw((byte) 0x0a); // Appends a line feed

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }

    /**
     * Prints the given value multiple times based on the copies parameter.
     *
     * @param value  The string value to print.
     * @param call   The PluginCall object for resolving or rejecting the Capacitor call.
     * @param copies The number of copies to print (Number). Defaults to 1 if null or <= 1.
     * @return The original value string.
     */
    public String print(String value, PluginCall call, Number copies) {
        StarIOPort port = null;
        int numberOfCopies = 1; // Default to 1 copy

        // Determine the actual number of copies needed
        if (copies != null) {
            int requestedCopies = copies.intValue(); // Get the integer value from Number
            if (requestedCopies > 1) {
                numberOfCopies = requestedCopies;
            }
            // Consider how to handle 0 or negative copies. Currently treated as 1.
            // if (requestedCopies <= 0) {
            //     call.reject("Invalid number of copies: Must be 1 or more.");
            //     return value;
            // }
        }

        // Log the print action details
        System.out.println("Attempting to print '" + value + "' - Copies: " + numberOfCopies);


        try {
            // Port open
            // Ensure 'context' is valid before calling this
            port = StarIOPort.getPort("USB:", "", 10000, context);

            // Add null check after attempting to get the port
            if (port == null) {
                System.err.println("Failed to get Star IO Port. Port is null.");
                throw new StarIOPortException("Failed to get port. Port is null.");
            }

            // Print end monitoring - Start (once before the loop)
            StarPrinterStatus status = port.beginCheckedBlock();

            // Add null check for status after beginCheckedBlock
            if (status == null) {
                System.err.println("Failed to begin checked block. Status is null.");
                throw new StarIOPortException("Failed to begin checked block. Status is null.");
            }

            // Check initial printer status (optional but good practice)
            if (status.offline) {
                System.err.println("Printer is offline before starting print job.");
                throw new StarIOPortException("Printer is offline. Cannot start print job.");
            }


            // Create print data ONCE before the loop if it's the same for all copies
            // Using StarPRNT enum directly is slightly preferred
            byte[] byteArray = createData(StarIoExt.Emulation.StarPRNT, value);

            // --- Loop Starts Here ---
            // Send the same print data multiple times
            for (int i = 0; i < numberOfCopies; i++) {
                System.out.println("Sending print data for copy " + (i + 1) + " of " + numberOfCopies);
                // Send print data for the current copy
                port.writePort(byteArray, 0, byteArray.length);

                // Optional: Short delay between copies if printer needs it, but often not necessary
                // try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            // --- Loop Ends Here ---

            // Print end monitoring - End (once after the loop)
            status = port.endCheckedBlock();

            // Status judgment during printing completion monitoring
            if (status == null) {
                // If status is null here, something went wrong during endCheckedBlock
                System.err.println("endCheckedBlock returned null status.");
                // Rejecting as the final status is unknown
                call.reject("Printing status unknown: Could not get status after printing.");
            } else if (status.offline) {
                // Printer is offline after the print job finished (or during)
                System.err.println("Printer is offline after printing completed.");
                // Original code resolved here. Depending on requirements,
                // you might want to reject if the printer ends offline.
                call.resolve(); // Or: call.reject("Printer went offline during or after printing.");
            } else {
                // Printer is online, assume success for all copies sent
                System.out.println("Printing completed successfully.");
                call.resolve();
            }

        } catch (StarIOPortException e) {
            // Log the error details
            System.err.println("Star I/O Port Exception: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            // Reject the plugin call with error details
            call.reject("Printing failed: " + e.getMessage(), e);

        } finally {
            // Ensure port release happens even if errors occurred
            if (port != null) {
                try {
                    // Port close
                    System.out.println("Releasing Star IO Port.");
                    StarIOPort.releasePort(port);
                } catch (StarIOPortException e) {
                    // Log this error, but usually can't do much more in the finally block
                    System.err.println("Failed to release Star IO Port: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return value; // Return the original value as per the method signature
    }

    public boolean openDrawer(PluginCall call) throws StarIOPortException {

        StarIOPort port = StarIOPort.getPort("USB:", "", 10000, context);

        ICommandBuilder builder = StarIoExt.createCommandBuilder(StarIoExt.Emulation.valueOf("StarPRNT"));
        builder.appendPeripheral(ICommandBuilder.PeripheralChannel.valueOf("No1"));

        port.writePort(builder.getCommands(), 0, builder.getCommands().length);

        port.endCheckedBlock();

        call.resolve();
        return true;
    }

}