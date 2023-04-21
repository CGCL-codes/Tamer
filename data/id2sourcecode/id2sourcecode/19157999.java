    public static ModuleDescriptor read(URL url) throws Exception {
        ModuleDescriptor moduleInfo;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(url.openStream());
        Element root = document.getDocumentElement();
        if (root.getLocalName().equals("module-descriptor")) {
            if (DEPRECATED_NS_MODULE_DESCRIPTOR.equals(root.getNamespaceURI()) || DEPRECATED_NS_MODULE_DESCRIPTOR.equals(root.getAttribute("xmlns"))) {
                String msg = "[DEPRECATED] Module descriptor file '" + url.toString() + "' uses deprecated namespace '" + DEPRECATED_NS_MODULE_DESCRIPTOR + "'. It should be replaced by '" + NS_MODULE_DESCRIPTOR + "'.";
                LOG.warn(msg);
            }
            Element nameElem = getSingleChildElement(root, "module-name", true);
            String name = nameElem.getTextContent().trim();
            if (name.equals("")) throw new Exception("Text content of element 'module-name' must not be empty!");
            moduleInfo = new ModuleDescriptor(url, name);
            Element editElem = getSingleChildElement(root, "content-editable", false);
            if (editElem != null) {
                boolean editable = Boolean.valueOf(editElem.getTextContent());
                moduleInfo.setContentEditable(editable);
            }
            Element searchElem = getSingleChildElement(root, "default-search", false);
            if (searchElem != null) {
                moduleInfo.setDefaultSearchable(true);
                String priority = searchElem.getAttribute("priority").trim();
                if (priority.length() > 0) {
                    moduleInfo.setDefaultSearchPriority(Integer.parseInt(priority));
                }
                List<Element> filterAttrElems = getChildElements(searchElem, "filter-attribute");
                for (Element filterAttrElem : filterAttrElems) {
                    String filterAttrName = filterAttrElem.getAttribute("name");
                    if (filterAttrName.equals("")) throw new Exception("Element 'filter-attribute' requires 'name' attribute!");
                    String filterAttrValue = filterAttrElem.getAttribute("value");
                    if (filterAttrValue.equals("")) throw new Exception("Element 'filter-attribute' requires 'value' attribute!");
                    moduleInfo.addDefaultSearchFilterAttribute(filterAttrName, filterAttrValue);
                }
            }
            Element overElem = getSingleChildElement(root, "override-modules", false);
            if (overElem != null) {
                List<Element> filterAttrElems = getChildElements(overElem, "filter-attribute");
                for (Element filterAttrElem : filterAttrElems) {
                    String filterAttrName = filterAttrElem.getAttribute("name");
                    if (filterAttrName.equals("")) throw new Exception("Element 'filter-attribute' requires 'name' attribute!");
                    String filterAttrValue = filterAttrElem.getAttribute("value");
                    if (filterAttrValue.equals("")) throw new Exception("Element 'filter-attribute' requires 'value' attribute!");
                    moduleInfo.addModuleOverrideFilterAttribute(filterAttrName, filterAttrValue);
                }
                List<Element> modElems = getChildElements(overElem, "module");
                for (Element modElem : modElems) {
                    String modName = modElem.getAttribute("name").trim();
                    if (modName.equals("")) throw new Exception("Element 'module' requires 'name' attribute!");
                    List<Element> resElems = getChildElements(modElem, "resource");
                    for (Element resElem : resElems) {
                        String resPath = resElem.getAttribute("path").trim();
                        if (resPath.equals("")) throw new Exception("Element 'resource' requires 'path' attribute!");
                        moduleInfo.addOverridedResource(modName, resPath);
                    }
                }
            }
            Element staticElem = getSingleChildElement(root, "static", false);
            if (staticElem != null) {
                List<Element> pathElems = getChildElements(staticElem, "path");
                for (Element pathElem : pathElems) {
                    String path = pathElem.getTextContent().trim();
                    if (!path.equals("")) {
                        if (!path.startsWith("/")) path = "/" + path;
                        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
                        if (path.equals("") || path.equals("/PUSTEFIX-INF")) path = "/"; else if (path.startsWith("/PUSTEFIX-INF")) path = path.substring(13);
                        moduleInfo.addStaticPath(path);
                    }
                }
            }
        } else throw new Exception("Illegal module descriptor");
        return moduleInfo;
    }
