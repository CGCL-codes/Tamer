package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.MpsPluginConfiguration;

/**
 * Adds digester rules for the mps tag.  
 * 
 * @todo this should be moved into MPS repository once the plugin 
 * configuration API is made available to MPS. Note that this implies that the 
 * EnabledDigester needs to be made available to MPS as well.
 */
public class MpsPluginRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public void addRuleInstances(Digester digester) {
        String pattern = prefix + "/mps";
        digester.addObjectCreate(pattern, MpsPluginConfiguration.class);
        digester.addSetNext(pattern, "addApplicationPlugin");
        digester.addSetProperties(pattern, new String[] { "internal-base-url", "message-recipient-info" }, new String[] { "internalBaseUrl", "messageRecipientInfo" });
        MpsChannelRuleSet mpsChannelRuleSet = new MpsChannelRuleSet(pattern + "/channels");
        mpsChannelRuleSet.addRuleInstances(digester);
    }
}
