package ai.blueplate.star;


import android.util.Log;

import com.getcapacitor.PluginCall;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

public class Star {

    public String print(String value, PluginCall ret) {
        StarIOPort port = null;

        try {
            // Port open
            port = StarIOPort.getPort(portName, portSettings, 10000, context);

            // Print end monitoring -Start
            StarPrinterStatus status = port.beginCheckedBlock();

            byte[] byteArray = value.getBytes();

            // Send print data
            port.writePort(byteArray, 0, byteArray.length);

            // Print end monitoring -End
            status = port.endCheckedBlock();

            // Status judgment during printing completion monitoring
            if (status.offline == false) {
                ret.resolve();
            }
            else {
                ret.resolve();
            }
        }
        catch (StarIOPortException e) {
            ret.resolve();
        }
        finally {
            try {
                // Port close
                StarIOPort.releasePort(port);
            }
            catch (StarIOPortException e) {}
        }
    }
}
