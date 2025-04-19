package ai.blueplate.star;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Star")
public class StarPlugin extends Plugin {

    private Star implementation = new Star();

    @PluginMethod
    public void print(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.print(value, ret));
    }

    @PluginMethod
    public void openDrawer(PluginCall call) {

        JSObject ret = new JSObject();
        ret.put("value", implementation.openDrawer(ret);
    }
}
