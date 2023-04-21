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
            ModuleException me = new ModuleException("XMLtoEDI Error: " + e.getMessage(), e);
            TRACE.throwing(SIGNATURE, me);
            throw me;
        }
        String cid = null;
        String schemaDefaultPath = null;
        Channel channel = null;
        try {
            schemaDefaultPath = (String) moduleContext.getContextData("schemaDefaultPath");
            cid = moduleContext.getChannelID();
            channel = (Channel) LookupManager.getInstance().getCPAObject(CPAObjectType.CHANNEL, cid);
            if (schemaDefaultPath == null) {
                TRACE.debugT(SIGNATURE, "Mode parameter is not set. Switch to '' as default.");
                schemaDefaultPath = "";
                ModuleException me = new ModuleException("XMLtoEDI Error: No schema default path!");
                TRACE.throwing(SIGNATURE, me);
                throw me;
            }
            TRACE.debugT(SIGNATURE, "Mode is set to {0}", new Object[] { schemaDefaultPath });
        } catch (Exception e) {
            TRACE.catching(SIGNATURE, e);
            TRACE.errorT(SIGNATURE, "Cannot read the module context and configuration data");
            ModuleException me = new ModuleException("XMLtoEDI Error: " + e.getMessage(), e);
            TRACE.throwing(SIGNATURE, me);
            throw me;
        }
        xmlPosition = 0;
        arXML = null;
        sbXMLPayload = new StringBuffer();
        sbEDI = new StringBuffer();
        dataSeparator = "+";
        componentSeparator = ":";
        decimalNotation = ".";
        releaseIndicator = "?";
        try {
            XMLPayload xmlpayload = msg.getDocument();
            if (xmlpayload != null) {
                sbXMLPayload.append(xmlpayload.getText().toString());
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("\r\n", ""));
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("\r", ""));
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&apos;", "'"));
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&quot;", "\""));
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&gt;", ">"));
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&lt;", "<"));
                sbXMLPayload = new StringBuffer(sbXMLPayload.toString().replaceAll("&amp;", "&"));
                xmlpayload.setContent(buildEDIfromXML(sbXMLPayload, schemaDefaultPath).toString().getBytes());
            }
            inputModuleData.setPrincipalData(msg);
            TRACE.debugT(SIGNATURE, "XMLtoEDI conversion finished sucessfully.");
        } catch (Exception e) {
            TRACE.catching(SIGNATURE, e);
            TRACE.errorT(SIGNATURE, "Cannot convert one of the payloads. Reason: {0}", new Object[] { e.getMessage() });
            ModuleException me = new ModuleException(new ModuleException("MessageKey: " + mk.toString() + " EDIValidate Error: " + e.getMessage()));
            TRACE.throwing(SIGNATURE, me);
            throw me;
        }
        TRACE.exiting(SIGNATURE);
        return inputModuleData;
    }
