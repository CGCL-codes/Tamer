    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String warPluginId = commandLine.getOptionValue(CMD_LINE_WAR_PLUGIN_OPTION);
        String destDirname = commandLine.getOptionValue(CMD_LINE_DESTDIR_OPTION);
        PluginDescriptor warPD = getManager().getRegistry().getPluginDescriptor(warPluginId);
        File webXMLFile = new File(destDirname, "web.xml");
        logger.debug(webXMLFile.getPath() + " does not exist, so processing is required.");
        if (!webXMLFile.exists()) {
            logger.debug(webXMLFile.getPath() + " does not exist");
            String templateFilename = getDescriptor().getAttribute(ATTRIBUTE_TEMPLATE_WEBXML).getValue();
            File templateFile = getFilePath(templateFilename);
            if (!templateFile.exists()) {
                throw new RuntimeException("Could not locate: '" + templateFile.getPath());
            }
            logger.debug("Copy " + templateFile + " to " + webXMLFile.getPath());
            FileUtils.copyFile(templateFile, webXMLFile);
        }
        StringBuffer originalXML = new StringBuffer();
        logger.debug("Read " + webXMLFile.getPath());
        originalXML.append(FileUtils.readFileToString(webXMLFile));
        String xslt = getXSLT(warPD);
        File xsltFile = new File(getPluginTmpDir(), "webxml-xslt.xml");
        logger.debug("Write web.xml xslt to " + xsltFile.getPath());
        FileUtils.writeStringToFile(new File(getPluginTmpDir(), "webxml-xslt.xml"), xslt);
        String translatedXMLString = getTranslatedXML(originalXML.toString(), xslt);
        webXMLFile.getParentFile().mkdirs();
        logger.debug("Write translated web.xml file to " + webXMLFile.getPath());
        FileUtils.writeStringToFile(webXMLFile, translatedXMLString);
    }
