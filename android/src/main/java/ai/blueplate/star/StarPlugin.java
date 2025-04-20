package ai.blueplate.star;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.starmicronics.stario.StarIOPortException;

@CapacitorPlugin(name = "Star")
public class StarPlugin extends Plugin {

    private Star implementation;

    @Override
    public void load() {
        implementation = new Star(getContext());
    }

    @PluginMethod
    public void print(PluginCall call) {
        String value = call.getString("value");

        Number copies = call.getInt("copies");

        JSObject ret = new JSObject();
        ret.put("value", implementation.print(value, call, copies));
    }

    @PluginMethod
    public void openDrawer(PluginCall call) throws StarIOPortException {

        JSObject ret = new JSObject();
        ret.put("value", implementation.openDrawer(call));
    }
}
