package net.confex.ss;

import net.confex.Constants;
import net.confex.application.ConfexPlugin;
import net.confex.translations.Translator;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.framework.BundleContext;

public class KmPlugin extends ConfexPlugin {

    public static final String ID = "net.confex.ss.KmPlugin";

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        System.out.println("Plugin KmPlugin started.");
        super.start(context);
        IEclipsePreferences preferences = new ConfigurationScope().getNode(ConfexPlugin.ID);
        System.out.println("Setup localisation = ru.");
        String lang = preferences.get(Constants.PREFERED_LANG, "ru");
        Translator.setLang(lang);
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }
}
