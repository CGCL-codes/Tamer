    @SuppressWarnings("unchecked")
    private void setupBizCompConf(String xaHome) throws IOException, JDOMException, FileNotFoundException {
        String bizcomp_config = xaHome + "conf/instructions/bizcomp/bizcomp_config.xml";
        bcConfFile = FileUtils.getFile(bizcomp_config);
        bcConfBackupFile = FileUtils.getFile(bizcomp_config + ".bac");
        if (!bcConfFile.exists()) {
            fail(bizcomp_config + " does not exist");
        }
        FileUtils.copyFile(bcConfFile, bcConfBackupFile);
        Document bdConfDoc = FileUtils.getDocumentFromFile(bcConfFile);
        Element iSetElement = bdConfDoc.getRootElement();
        if (iSetElement == null) {
            fail(bizcomp_config + " does not contain the root element: InstructionSet");
        }
        Element e = iSetElement.getChild("element");
        Element bcAttrElement = null;
        List attrElements = e.getChildren("attribute");
        for (Object o : attrElements) {
            if (o instanceof Element) {
                bcAttrElement = (Element) o;
                String attrName = bcAttrElement.getAttributeValue("name");
                if (attrName != null && attrName.equals(XAwareConstants.BIZCOMPONENT_ATTR_TYPE)) {
                    break;
                } else {
                    bcAttrElement = null;
                }
            }
        }
        if (bcAttrElement == null) {
            fail("Unable to find the bizcomptype configuration");
        }
        Element sqlInst = null;
        List instElements = bcAttrElement.getChildren("instruction");
        for (Object o : instElements) {
            if (o instanceof Element) {
                sqlInst = (Element) o;
                String attrName = sqlInst.getAttributeValue("value");
                if (attrName != null && attrName.equals(XAwareConstants.XSL_BIZCOMPONENT_TYPE)) {
                    break;
                } else {
                    sqlInst = null;
                }
            }
        }
        if (sqlInst == null) {
            fail("unable to find the SQL bizcomp configuration");
        }
        Element instElement2 = new Element("instruction");
        instElement2.setAttribute("value", XAwareConstants.XSL_BIZCOMPONENT_TYPE);
        instElement2.setAttribute("beanName", "XslBizCompInst2");
        instElement2.setAttribute("class", "org.xaware.server.engine.instruction.bizcomps.XSLBizCompInst2");
        instElement2.setAttribute("version", "5.2");
        Element instElement3 = (Element) instElement2.clone();
        instElement3.setAttribute("value", XAwareConstants.XSL_BIZCOMPONENT_TYPE);
        instElement3.setAttribute("beanName", "XslBizCompInst3");
        instElement3.setAttribute("class", "org.xaware.server.engine.instruction.bizcomps.XSLBizCompInst3");
        instElement3.setAttribute("version", "5.4");
        bcAttrElement.addContent(instElement2);
        bcAttrElement.addContent(instElement3);
        FileOutputStream bizDocOutStream = null;
        try {
            final XMLOutputter outputter = new XMLOutputter();
            bizDocOutStream = new FileOutputStream(bcConfFile, false);
            outputter.output(bdConfDoc, bizDocOutStream);
        } finally {
            if (bizDocOutStream != null) {
                bizDocOutStream.close();
            }
        }
    }
