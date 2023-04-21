    private void processOracleDS(final IProject project, final boolean exists, final StringBuilder name, final File srcFile, final StringBuffer ds, final File oracleds) {
        if (!exists) {
            try {
                FileUtils.copyFile(srcFile, oracleds);
            } catch (IOException e) {
                Logger.log(Logger.ERROR, "Could not copy the oracle datasource file to Bea WL", e);
            }
        }
        final IFile wlAppDD = project.getFile(new Path("ear/src/main/resources/META-INF/weblogic-application.xml"));
        if (!wlAppDD.exists()) {
            Logger.getLog().error("File weblogic-application.xml MUST exists at ear/src/main/resources/META-INF");
            throw new IllegalStateException("File weblogic-application.xml MUST exists at ear/src/main/resources/META-INF");
        }
        ContentHandlerTemplate.handle(wlAppDD, new ContentHandlerCallback() {

            @SuppressWarnings("unchecked")
            public void processHandle(Document doc) {
                final Element root = doc.getDocumentElement();
                final Element elemType = XMLUtils.getChildElementByTagName(root, "wls:type");
                final String elemCont = (elemType != null) ? elemType.getTextContent() : "";
                if (elemCont != null && (elemCont.length() > 0 && "JDBC".equals(elemCont))) {
                    Logger.log(Logger.INFO, "[CONF ERROR] you have defined previously a type. You can not define to types at same time");
                    Logger.log(Logger.INFO, "[CONF ERROR] we have decided to solve name :: " + name.toString());
                    Logger.log(Logger.INFO, "[CONF ERROR] we have decided to solve path :: " + ds.toString());
                    Element elemPath = XMLUtils.getChildElementByTagName(root, "wls:path");
                    elemPath.setTextContent(new StringBuilder("jdbc").append("/").append(ds.toString()).toString());
                    Element elemName = XMLUtils.getChildElementByTagName(root, "wls:name");
                    elemName.setTextContent(name.toString());
                    return;
                }
                Element module = doc.createElementNS("wls", "module");
                module.setPrefix("wls");
                Element _name = doc.createElementNS("wls", "name");
                _name.setPrefix("wls");
                _name.setTextContent(name.toString());
                module.appendChild(_name);
                Element type = doc.createElementNS("wls", "type");
                type.setPrefix("wls");
                type.setTextContent("JDBC");
                module.appendChild(type);
                Element path = doc.createElementNS("wls", "path");
                path.setPrefix("wls");
                path.setTextContent(new StringBuilder("jdbc").append("/").append(ds.toString()).toString());
                module.appendChild(path);
                Element preferApppackages = XMLUtils.getChildElementByTagName(root, "wls:prefer-application-packages");
                root.insertBefore(module, preferApppackages);
            }
        });
    }
