    public ModuleData process(ModuleContext moduleContext, ModuleData inputModuleData) throws ModuleException {
        String SIGNATURE = "process(ModuleContext moduleContext, ModuleData inputModuleData)";
        TRACE.entering(SIGNATURE, new Object[] { moduleContext, inputModuleData });
        Object obj = null;
        Message msg = null;
        MessageKey mk = null;
        try {
            obj = inputModuleData.getPrincipalData();
            msg = (Message) obj;
            mk = new MessageKey(((Message) inputModuleData.getPrincipalData()).getMessageId(), (((Message) inputModuleData.getPrincipalData()).getMessageDirection()));
        } catch (Exception e) {
            TRACE.catching(SIGNATURE, e);
            if (obj != null) TRACE.errorT(SIGNATURE, "Input ModuleData does not contain an object that implements the XI message interface. The object class is: {0}", new Object[] { obj.getClass().getName() }); else TRACE.errorT(SIGNATURE, "Input ModuleData contains only null as XI message");
            ModuleException me = new ModuleException("EDItoXML Error: " + e.getMessage(), e);
            TRACE.throwing(SIGNATURE, me);
            throw me;
        }
        String cid = null;
        Channel channel = null;
        String validateFlag = null;
        String basicXSDFolderPath = null;
        String defaultXSDFolder = null;
        String exceptionFlag = null;
        String originalMessageFlag = null;
        String encoding = null;
        validateFlag = (String) moduleContext.getContextData("validateFlag");
        if (validateFlag == null) {
            TRACE.debugT(SIGNATURE, "No Configuration parameter validateFlag is set!");
            ModuleException me = new ModuleException("No Configuration parameter validateFlag is set!");
            TRACE.throwing(SIGNATURE, me);
            throw me;
        }
        if (validateFlag.equals("true")) {
            try {
                basicXSDFolderPath = (String) moduleContext.getContextData("basicXSDFolderPath");
                defaultXSDFolder = (String) moduleContext.getContextData("defaultXSDFolder");
                exceptionFlag = (String) moduleContext.getContextData("exceptionFlag");
                originalMessageFlag = (String) moduleContext.getContextData("originalMessageFlag");
                encoding = (String) moduleContext.getContextData("encoding");
                cid = moduleContext.getChannelID();
                channel = (Channel) LookupManager.getInstance().getCPAObject(CPAObjectType.CHANNEL, cid);
                if (basicXSDFolderPath == null) {
                    TRACE.debugT(SIGNATURE, "XSD folder path parameter is not set. Switch to '' as default.");
                    basicXSDFolderPath = "";
                    ModuleException me = new ModuleException("EDIValidate Error: No basic xsd folder path set!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "The XSD folder path is set to {0}", new Object[] { basicXSDFolderPath });
                if (defaultXSDFolder == null) {
                    TRACE.debugT(SIGNATURE, "Default xsd folder is not set. Switch to '' as default.");
                    defaultXSDFolder = "";
                    ModuleException me = new ModuleException("EDIValidate Error: No default xsd folder set!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "The default xsd folder is set to {0}", new Object[] { defaultXSDFolder });
                if (exceptionFlag == null) {
                    TRACE.debugT(SIGNATURE, "Exception flag parameter is not set.");
                    exceptionFlag = "";
                    ModuleException me = new ModuleException("EDIValidate Error: No exception flag is set!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "The XSD folder path is set to {0}", new Object[] { basicXSDFolderPath });
                if (originalMessageFlag == null) {
                    TRACE.debugT(SIGNATURE, "Original Message Flag parameter is not set.");
                    originalMessageFlag = "";
                    ModuleException me = new ModuleException("EDIValidate Error: No Original Message Flag is set!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "The XSD folder path is set to {0}", new Object[] { basicXSDFolderPath });
                if (encoding == null) {
                    encoding = "";
                }
            } catch (Exception e) {
                TRACE.catching(SIGNATURE, e);
                TRACE.errorT(SIGNATURE, "Cannot read the module context and configuration data");
                ModuleException me = new ModuleException("EDIValidate process() Error: " + e.getMessage(), e);
                TRACE.throwing(SIGNATURE, me);
                throw me;
            }
        } else {
            try {
                basicXSDFolderPath = (String) moduleContext.getContextData("basicXSDFolderPath");
                defaultXSDFolder = (String) moduleContext.getContextData("defaultXSDFolder");
                exceptionFlag = (String) moduleContext.getContextData("exceptionFlag");
                originalMessageFlag = (String) moduleContext.getContextData("originalMessageFlag");
                encoding = (String) moduleContext.getContextData("encoding");
                cid = moduleContext.getChannelID();
                channel = (Channel) LookupManager.getInstance().getCPAObject(CPAObjectType.CHANNEL, cid);
                if (basicXSDFolderPath == null) {
                    TRACE.debugT(SIGNATURE, "Mode parameter is not set. Switch to '' as default.");
                    basicXSDFolderPath = "";
                    ModuleException me = new ModuleException("EDItoXML Error: No schema path!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "Mode is set to {0}", new Object[] { basicXSDFolderPath });
                if (defaultXSDFolder == null) {
                    TRACE.debugT(SIGNATURE, "EDItoXML Error: No Default Folder!");
                    basicXSDFolderPath = "";
                    ModuleException me = new ModuleException("EDItoXML Error: No Default Folder!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "Mode is set to {0}", new Object[] { defaultXSDFolder });
                if (exceptionFlag == null) {
                    TRACE.debugT(SIGNATURE, "Exception flag parameter is not set.");
                    exceptionFlag = "";
                    ModuleException me = new ModuleException("EDIValidate Error: No exception flag is set!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "The XSD folder path is set to {0}", new Object[] { basicXSDFolderPath });
                if (originalMessageFlag == null) {
                    TRACE.debugT(SIGNATURE, "Original Message Flag parameter is not set.");
                    originalMessageFlag = "";
                    ModuleException me = new ModuleException("EDIValidate Error: No Original Message Flag is set!");
                    TRACE.throwing(SIGNATURE, me);
                    throw me;
                }
                TRACE.debugT(SIGNATURE, "The XSD folder path is set to {0}", new Object[] { basicXSDFolderPath });
                if (encoding == null) {
                    encoding = "";
                }
            } catch (Exception e) {
                TRACE.catching(SIGNATURE, e);
                TRACE.errorT(SIGNATURE, "Cannot read the module context and configuration data");
                ModuleException me = new ModuleException("EDItoXML process() Error: " + e.getMessage(), e);
                TRACE.throwing(SIGNATURE, me);
                throw me;
            }
        }
        sbXMLPayload = new StringBuffer();
        if (basicXSDFolderPath.compareToIgnoreCase("noXSDFileNameSpecified") == 0) {
            TRACE.debugT(SIGNATURE, "Bypass EDI to XML conversion since 'schemaFileName' parameter was set to 'noXSDFileNameSpecified'.");
        } else {
            try {
                XMLPayload xmlpayload = msg.getDocument();
                if (xmlpayload != null) {
                    sbXMLPayload.append(new String(xmlpayload.getContent(), "ISO-8859-1"));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("\r\n", ""));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("\r", ""));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&apos;", "'"));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&quot;", "\""));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&gt;", ">"));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&lt;", "<"));
                    sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&amp;", "&"));
                    if (validateFlag.equals("true")) {
                        try {
                            DataProcessor converter = new DataProcessor(sbXMLPayload, basicXSDFolderPath, defaultXSDFolder, exceptionFlag, originalMessageFlag, encoding, new XITrace(TRACE));
                            xmlpayload.setContent(converter.convertAndValidate().toString().getBytes("ISO-8859-1"));
                        } catch (EDIException e) {
                            throw new ModuleException(e.getMessage(), e.getCause());
                        }
                    } else {
                        try {
                            DataProcessor converter = new DataProcessor(sbXMLPayload, basicXSDFolderPath, defaultXSDFolder, exceptionFlag, originalMessageFlag, encoding, new XITrace(TRACE));
                            xmlpayload.setContent(converter.convert().toString().getBytes("ISO-8859-1"));
                        } catch (EDIException e) {
                            throw new ModuleException(e.getMessage(), e.getCause());
                        }
                    }
                }
                inputModuleData.setPrincipalData(msg);
                TRACE.debugT(SIGNATURE, "EDIValidate conversion finished sucessfully.");
            } catch (Exception e) {
                TRACE.catching(SIGNATURE, e);
                TRACE.errorT(SIGNATURE, "Cannot convert one of the payloads. Reason: {0}", new Object[] { e.getMessage() });
                ModuleException me = new ModuleException(new ModuleException("MessageKey: " + mk.toString() + " EDIValidate Error: " + e.getMessage()));
                TRACE.throwing(SIGNATURE, me);
                throw me;
            }
        }
        TRACE.exiting(SIGNATURE);
        return inputModuleData;
    }
