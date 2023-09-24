package plugins.hello.world;

import freenet.clients.http.ToadletContainer;
import freenet.l10n.BaseL10n.LANGUAGE;
import freenet.node.probe.Error;
import freenet.node.probe.Listener;
import freenet.node.probe.Type;
import freenet.pluginmanager.*;
import freenet.support.Logger;
import freenet.support.plugins.helpers1.PluginContext;
import freenet.support.plugins.helpers1.WebInterface;

public class MyApplication implements FredPlugin, FredPluginThreadless, FredPluginL10n {
    private PluginRespirator pluginRespirator;
    private ToadletContainer tc;
    // path used to access the plugin web interface
    public static String basePath = "/MyApplication";
    // WebInterface accessor
    public WebInterface webInterface;
    // Overview page
    private Overview oc;
    public static final String encoding = "UTF-8";

    public String getVersion() {
        return "0.1-SNAPSHOT";
    }

    static {
        Logger.registerClass(MyApplication.class);
    }
    
    @Override
    public void runPlugin(PluginRespirator pr)
    {
        pluginRespirator = pr;
        tc = pr.getToadletContainer();
        Logger.error(this, "FOOBAR MYAPPLICATION WEBINTERFACE HELLO WORLD");
        setupWebInterface();
    }
    
    @Override
    public void terminate()
    {
        pluginRespirator.getToadletContainer().unregister(this.oc);
    }
    
    // L10n stuff
    public void setLanguage(LANGUAGE newLanguage) {
        Logger.error(this, "Not implemented: Should set LANGUAGE to: " + newLanguage.isoCode);
    }
    public String getString(String untranslated){
            return untranslated;
    }

    private void setupWebInterface()
    {
        PluginContext pluginContext = new PluginContext(pluginRespirator);
        webInterface = new WebInterface(pluginContext);
        
        pluginRespirator.getPageMaker().addNavigationCategory(basePath + "/","MyApplication.menuName.name", "MyApplication.menuName.tooltip", this);
        
        // pages
        oc = new Overview(pluginRespirator.getHLSimpleClient(), basePath, "");

        // create fproxy menu items
        String menuName = "MyApplication";
        pluginRespirator.getPageMaker().addNavigationCategory(basePath, menuName, menuName, this);
        tc.register(oc, menuName, basePath + "/", true, menuName, "tooltip", false, oc); // false: MyApplication.allowFullAccessOnly
        // tc.register(oc, null, basePath + "/", true, false); // false: do we want to restrict to full access?
        
        // register other toadlets without link in menu but as first item to check
        // so it also works for paths which are included in the above menu links.
        // full access only will be checked inside the specific toadlet
        // for(Toadlet curToad : newToadlets) {
        //     tc.register(curToad, null, curToad.path(), true, false);
        // }
        
        // finally add toadlets which have been registered within the menu to our list
        //newToadlets.add(oc);
        System.out.println("Sending probe");
        pluginRespirator.getNode().startProbe((byte)20, pluginRespirator.getNode().random.nextLong(), Type.IDENTIFIER, new Listener() {
            @Override
            public void onError(Error error, Byte code, boolean local) {
                System.out.println("Probe error code: " + error.code + (local ? " local" : " remote"));
            }

            @Override
            public void onRefused() {
                System.out.println("Probe refused");
            }

            @Override
            public void onOutputBandwidth(float outputBandwidth) {
                System.out.println("Probe bandwidth: " + outputBandwidth);
            }

            @Override
            public void onBuild(int build) {
                System.out.println("Probe build: " + build);
            }

            @Override
            public void onIdentifier(long probeIdentifier, byte percentageUptime) {
                System.out.println("Probe identifier: " + probeIdentifier + " with uptime " + percentageUptime);
            }

            @Override
            public void onLinkLengths(float[] linkLengths) {
                System.out.println("Probe got link lengths: " + linkLengths.length);
            }

            @Override
            public void onLocation(float location) {
                System.out.println("Probe got link lengths: " + location);
            }

            @Override
            public void onStoreSize(float storeSize) {
                System.out.println("Probe got link lengths: " + storeSize);
            }

            @Override
            public void onUptime(float uptimePercent) {
                System.out.println("Probe got uptime percent: " + uptimePercent);
            }

            @Override
            public void onRejectStats(byte[] stats) {
                System.out.println("Probe got reject stats: " +
                        "CHK request " + stats[0] + " | " +
                        "SSK request " + stats[1] + " | " +
                        "CHK insert " + stats[2] + " | " +
                        "SSK insert " + stats[3]);
            }

            @Override
            public void onOverallBulkOutputCapacity(byte bandwidthClassForCapacityUsage, float capacityUsage) {
                System.out.println("Probe got " + capacityUsage + " for bandwidth class " +
                        bandwidthClassForCapacityUsage);
            }
        });
        System.out.println("Probe sent");
    }
}

// private class Resources {
//     
// }
