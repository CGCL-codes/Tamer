    private void setupBizCompScriptConfFile(String xaHome) throws IOException, JDOMException {
        String script_config = xaHome + "conf/common/spring/BizComponentConfig.xml";
        scriptConfFile = FileUtils.getFile(script_config);
        scriptConfBackupFile = FileUtils.getFile(script_config + ".bac");
        if (!scriptConfFile.exists()) {
            fail(script_config + " does not exist");
        }
        FileUtils.copyFile(scriptConfFile, scriptConfBackupFile);
        Document bdConfDoc = FileUtils.getDocumentFromFile(scriptConfFile);
        Element root = bdConfDoc.getRootElement();
        if (root == null) {
            fail(script_config + " does not contain a root element");
        }
        Element xslBean = new Element("bean", root.getNamespace());
        xslBean.setAttribute("id", "XslBizCompInst2");
        xslBean.setAttribute("class", "org.xaware.server.engine.instruction.bizcomps.XSLBizCompInst2");
        xslBean.setAttribute("scope", "prototype");
        root.addContent(xslBean);
        xslBean = new Element("bean", root.getNamespace());
        xslBean.setAttribute("id", "XslBizCompInst3");
        xslBean.setAttribute("class", "org.xaware.server.engine.instruction.bizcomps.XSLBizCompInst3");
        xslBean.setAttribute("scope", "prototype");
        root.addContent(xslBean);
        FileOutputStream outStream = null;
        try {
            final XMLOutputter outputter = new XMLOutputter();
            outStream = new FileOutputStream(scriptConfFile, false);
            outputter.output(bdConfDoc, outStream);
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
    }
