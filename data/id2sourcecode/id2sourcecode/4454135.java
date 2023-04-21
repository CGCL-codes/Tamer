    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String warPluginId = commandLine.getOptionValue(CMD_LINE_WAR_PLUGIN_OPTION);
        String destDirname = commandLine.getOptionValue(CMD_LINE_DESTDIR_OPTION);
        PluginDescriptor warPluginDescriptor = getManager().getRegistry().getPluginDescriptor(warPluginId);
        File facesConfigXMLFile = new File(destDirname, "faces-config.xml");
        if (!facesConfigXMLFile.exists()) {
            logger.debug(facesConfigXMLFile.getPath() + " does not exist");
            String templateFilename = getDescriptor().getAttribute(ATTRIBUTE_TEMPLATE_FACESCONFIGXML).getValue();
            File templateFile = getFilePath(templateFilename);
            if (!templateFile.exists()) {
                throw new RuntimeException("Could not locate: '" + templateFile.getPath() + "' in " + getDescriptor().getId());
            }
            facesConfigXMLFile.getParentFile().mkdirs();
            logger.debug("Copy " + templateFile.getPath() + " to " + facesConfigXMLFile.getPath());
            FileUtils.copyFile(templateFile, facesConfigXMLFile);
        }
        StringBuffer originalXML = new StringBuffer();
        originalXML.append(FileUtils.readFileToString(facesConfigXMLFile));
        String xslt = getXSLT(warPluginDescriptor);
        if (xslt == null) {
            facesConfigXMLFile.delete();
        } else {
            File facesConfigXSLT = new File(getPluginTmpDir(), "faces-configxml-xslt.xml");
            logger.debug("Write faces-config XSLT to " + facesConfigXSLT.getPath());
            FileUtils.writeStringToFile(facesConfigXSLT, xslt);
            String translatedXMLString = getTranslatedXML(originalXML.toString(), xslt);
            facesConfigXMLFile.getParentFile().mkdirs();
            logger.debug("Write translated faces-config.xml to " + facesConfigXMLFile.getPath());
            FileUtils.writeStringToFile(facesConfigXMLFile, translatedXMLString);
        }
    }
