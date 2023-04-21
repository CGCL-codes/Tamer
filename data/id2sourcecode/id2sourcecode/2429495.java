    public WamToolkit getToolkit(String uri, WamDocument doc) throws WamParseException, IOException {
        WamToolkit ret = theToolkits.get(uri);
        if (ret != null) return ret;
        java.net.URL url;
        try {
            url = new java.net.URL(uri);
        } catch (java.net.MalformedURLException e) {
            throw new WamParseException("Could not parse URL " + uri, e);
        }
        Element rootEl;
        try {
            rootEl = new org.dom4j.io.SAXReader(theDF).read(new java.io.InputStreamReader(url.openStream())).getRootElement();
        } catch (org.dom4j.DocumentException e) {
            throw new WamParseException("Could not parse toolkit XML for " + uri, e);
        }
        String name = rootEl.elementTextTrim("name");
        if (name == null) throw new WamParseException("No name element for toolkit at " + uri);
        String descrip = rootEl.elementTextTrim("description");
        if (descrip == null) throw new WamParseException("No description element for toolkit at " + uri);
        String version = rootEl.elementTextTrim("version");
        if (version == null) throw new WamParseException("No version element for toolkit at " + uri);
        if (doc == null || doc.getDefaultToolkit() == null) ret = new WamToolkit(url, name, descrip, version); else ret = new WamToolkit(doc.getDefaultToolkit(), url, name, descrip, version);
        for (Element el : (java.util.List<Element>) rootEl.elements()) {
            String elName = el.getName();
            if (elName.equals("name") || elName.equals("descrip") || elName.equals("version")) continue;
            if (elName.equals("dependencies")) {
                for (Element dEl : (java.util.List<Element>) el.elements()) {
                    if (!dEl.getName().equals("depends")) throw new WamParseException("Illegal element under " + elName);
                    if (doc == null || doc.getDefaultToolkit() == null) throw new WamParseException("Default toolkit cannot have dependencies");
                    WamToolkit dependency = getToolkit(dEl.getTextTrim(), doc);
                    try {
                        ret.addDependency(dependency);
                    } catch (WamException e) {
                        throw new WamParseException("Toolkit is already sealed?", e);
                    }
                }
            } else if (elName.equals("types")) {
                for (Element tEl : (java.util.List<Element>) el.elements()) {
                    if (!tEl.getName().equals("type")) throw new WamParseException("Illegal element under " + elName);
                    String tagName = tEl.attributeValue("tag");
                    if (tagName == null) throw new WamParseException("tag attribute expected for " + tEl.getName() + " element");
                    String className = tEl.getTextTrim();
                    if (className == null || className.length() == 0) throw new WamParseException("Class name expected for element " + tEl.getName());
                    try {
                        ret.map(tagName, className);
                    } catch (WamException e) {
                        throw new WamParseException("Toolkit is already sealed?", e);
                    }
                }
            } else if (elName.equals("security")) {
                for (Element pEl : (java.util.List<Element>) el.elements()) {
                    if (!pEl.getName().equals("permission")) throw new WamParseException("Illegal element under " + elName);
                    String typeName = pEl.attributeValue("type");
                    if (typeName == null) throw new WamParseException("No type name in permission element");
                    typeName = typeName.toLowerCase();
                    int idx = typeName.indexOf("/");
                    String subTypeName = null;
                    if (idx >= 0) {
                        subTypeName = typeName.substring(idx + 1).trim();
                        typeName = typeName.substring(0, idx).trim();
                    }
                    WamPermission.Type type = WamPermission.Type.byKey(typeName);
                    if (type == null) throw new WamParseException("No such permission type: " + typeName);
                    WamPermission.SubType[] allSubTypes = type.getSubTypes();
                    WamPermission.SubType subType = null;
                    if (allSubTypes != null && allSubTypes.length > 0) {
                        if (subType == null) throw new WamParseException("No sub-type specified for permission type " + type);
                        for (WamPermission.SubType st : allSubTypes) if (st.getKey().equals(subTypeName)) subType = st;
                        if (subType == null) throw new WamParseException("No such sub-type " + subTypeName + " for permission type " + type);
                    } else if (subTypeName != null) throw new WamParseException("No sub-types exist (such as " + subTypeName + ") for permission type " + type);
                    boolean req = "true".equalsIgnoreCase(pEl.attributeValue("required"));
                    String explanation = pEl.getTextTrim();
                    String[] params = new String[subType == null ? 0 : subType.getParameters().length];
                    if (subType != null) for (int p = 0; p < subType.getParameters().length; p++) {
                        params[p] = pEl.attributeValue(subType.getParameters()[p].getKey());
                        String val = subType.getParameters()[p].validate(params[p]);
                        if (val != null) throw new WamParseException("Invalid parameter " + subType.getParameters()[p].getName() + ": " + val);
                    }
                    try {
                        ret.addPermission(new WamPermission(type, subType, params, req, explanation));
                    } catch (WamException e) {
                        throw new WamParseException("Unexpected WAM Exception: toolkit is sealed?", e);
                    }
                }
            } else throw new WamParseException("Illegal element under " + elName);
        }
        return ret;
    }
