    public List getControllerVariables() {
        ArrayList result = new ArrayList();
        if (hasController() && getController().getAttributes().getNamedItem("class") == null) {
            NodeList children = getController().getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if ("variable".equals(children.item(i).getNodeName())) {
                    NamedNodeMap attributes = children.item(i).getAttributes();
                    String name = attributes.getNamedItem("name").getNodeValue();
                    org.w3c.dom.Node accessAttr = attributes.getNamedItem("access");
                    String access = accessAttr == null ? "read,write" : accessAttr.getNodeValue();
                    org.w3c.dom.Node mappedNameAttr = attributes.getNamedItem("mapped-name");
                    String mappedName = mappedNameAttr == null ? null : mappedNameAttr.getNodeValue();
                    result.add(new Variable(name, access.indexOf("read") != -1, access.indexOf("write") != -1, access.indexOf("required") != -1, mappedName));
                }
            }
        }
        return result;
    }
