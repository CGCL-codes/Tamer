    private void parseO() {
        ngtXMLHandler xnodeAtrKey = null;
        ngtXMLHandler[] xAttributes = null;
        ngtXMLHandler node = super.getChildNode("classKeys");
        if (node != null) {
            ngtXMLHandler[] xnodes = node.getChildNodes();
            if (xnodes != null) {
                p_classKeys = new String[xnodes.length];
                p_classKeysActive = new boolean[xnodes.length];
                for (int i = 0; i < xnodes.length; i++) {
                    p_classKeysActive[i] = GenericParseUtils.parseBoolean(xnodes[i].getAttribute("active"));
                    p_classKeys[i] = xnodes[i].getAttribute("name");
                }
            }
        }
        node = super.getChildNode("attributeKeys");
        if (node != null) {
            String[] ktypes = { "read", "write", "delete", "fullControl" };
            for (int i = 0; i < ktypes.length; i++) {
                xnodeAtrKey = node.getChildNode(ktypes[i]);
                Vector atts = new Vector();
                if (xnodeAtrKey != null) {
                    xAttributes = xnodeAtrKey.getChildNodes();
                    for (int k = 0; k < xAttributes.length; k++) {
                        boolean active = GenericParseUtils.parseBoolean(xAttributes[k].getAttribute("active", "true"));
                        if (active) {
                            atts.add(xAttributes[k].getText());
                        }
                    }
                }
                switch(i) {
                    case 0:
                        p_attributesReadKey = (String[]) atts.toArray(new String[atts.size()]);
                        break;
                    case 1:
                        p_attributesWriteKey = (String[]) atts.toArray(new String[atts.size()]);
                        break;
                    case 2:
                        p_attributesDeleteKey = (String[]) atts.toArray(new String[atts.size()]);
                        break;
                    case 3:
                        p_attributesFullControlKey = (String[]) atts.toArray(new String[atts.size()]);
                        break;
                }
            }
        }
    }
