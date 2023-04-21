    void digestRules(Element root) {
        rules = new HashSet();
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Rule rule = getRule(child.getName());
            if (rule != null) {
                rule.digest(child);
                addRule(rule);
            }
        }
    }
