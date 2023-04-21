    private void addControllerVariable(Variable variable) {
        if (variable.name == null || "".equals(variable.name)) return;
        Document document = getNode().getOwnerDocument();
        IDOMNode before = (IDOMNode) getController().getLastChild();
        IDOMNode element = (IDOMNode) document.createElement("variable");
        org.w3c.dom.Node name = document.createAttribute("name");
        name.setNodeValue(variable.name);
        element.getAttributes().setNamedItem(name);
        org.w3c.dom.Node access = document.createAttribute("access");
        String accessString = ",";
        if (Boolean.TRUE.equals(variable.read)) accessString += "read";
        if (Boolean.TRUE.equals(variable.write)) accessString += ",write";
        if (Boolean.TRUE.equals(variable.required)) accessString += ",required";
        if (!",read,write".equals(accessString)) {
            access.setNodeValue(accessString.substring(1));
            element.getAttributes().setNamedItem(access);
        }
        if (variable.mappedName != null && !"".equals(variable.mappedName)) {
            org.w3c.dom.Node mappedName = document.createAttribute("mapped-name");
            mappedName.setNodeValue(variable.mappedName);
            element.getAttributes().setNamedItem(mappedName);
        }
        getController().insertBefore(element, before);
        IDOMNode text = (IDOMNode) document.createTextNode("\n" + getPaddingString(getLevel() + 1));
        getController().insertBefore(text, element);
    }
