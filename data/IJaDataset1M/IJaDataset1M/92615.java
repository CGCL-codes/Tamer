package com.volantis.xml.pipeline.sax.config;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet;
import com.volantis.xml.namespace.ExpandedName;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of the {@link DynamicProcessConfiguration} class
 */
public class MockDynamicProcessConfiguration implements DynamicProcessConfiguration {

    /**
     * Map for storing the rules in
     */
    Map ruleSets = new HashMap();

    public NamespaceRuleSet getNamespaceRules(String namespaceURI, boolean create) {
        NamespaceRuleSet ruleSet = getNamespaceRules(namespaceURI);
        if (null == ruleSet && create) {
            ruleSet = new SimpleNamespaceRuleSet(namespaceURI);
            ruleSets.put(namespaceURI, ruleSet);
        }
        return ruleSet;
    }

    public NamespaceRuleSet getNamespaceRules(String namespaceURI) {
        return (NamespaceRuleSet) ruleSets.get(namespaceURI);
    }

    public DynamicElementRule getRule(ExpandedName element) {
        DynamicElementRule rule = null;
        NamespaceRuleSet ruleSet = getNamespaceRules(element.getNamespaceURI());
        if (ruleSet != null) {
            rule = ruleSet.getRule(element.getLocalName());
        }
        return rule;
    }
}
