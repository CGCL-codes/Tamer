    public void digestGroupRules(Element root) {
        groupRules = new HashSet();
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            GroupRules gr = getGroupRules(child.getName());
            ;
            if (gr != null) {
                gr.digest(child);
                addGroupRules(gr);
            }
        }
    }
