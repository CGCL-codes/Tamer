    public static Map sendMailFromScreen(DispatchContext dctx, Map serviceContext) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String webSiteId = (String) serviceContext.remove("webSiteId");
        String bodyText = (String) serviceContext.remove("bodyText");
        String bodyScreenUri = (String) serviceContext.remove("bodyScreenUri");
        String xslfoAttachScreenLocation = (String) serviceContext.remove("xslfoAttachScreenLocation");
        String attachmentName = (String) serviceContext.remove("attachmentName");
        Map bodyParameters = (Map) serviceContext.remove("bodyParameters");
        Locale locale = (Locale) serviceContext.get("locale");
        if (!bodyParameters.containsKey("locale")) bodyParameters.put("locale", locale); else locale = (Locale) bodyParameters.get("locale");
        String partyId = (String) bodyParameters.get("partyId");
        NotificationServices.setBaseUrl(dctx.getDelegator(), webSiteId, bodyParameters);
        String contentType = (String) serviceContext.remove("contentType");
        if (UtilValidate.isEmpty(attachmentName)) {
            attachmentName = "Details.pdf";
        }
        StringWriter bodyWriter = new StringWriter();
        MapStack screenContext = MapStack.create();
        screenContext.put("locale", locale);
        ScreenRenderer screens = new ScreenRenderer(bodyWriter, screenContext, htmlScreenRenderer);
        screens.populateContextForService(dctx, bodyParameters);
        screenContext.putAll(bodyParameters);
        if (bodyScreenUri != null) {
            try {
                screens.render(bodyScreenUri);
            } catch (GeneralException e) {
                String errMsg = "Error rendering screen for email: " + e.toString();
                Debug.logError(e, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (IOException e) {
                String errMsg = "Error rendering screen for email: " + e.toString();
                Debug.logError(e, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (SAXException e) {
                String errMsg = "Error rendering screen for email: " + e.toString();
                Debug.logError(e, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (ParserConfigurationException e) {
                String errMsg = "Error rendering screen for email: " + e.toString();
                Debug.logError(e, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            }
        }
        boolean isMultiPart = false;
        if (UtilValidate.isNotEmpty(xslfoAttachScreenLocation)) {
            isMultiPart = true;
            try {
                Writer writer = new StringWriter();
                MapStack screenContextAtt = MapStack.create();
                ScreenRenderer screensAtt = new ScreenRenderer(writer, screenContext, htmlScreenRenderer);
                screensAtt.populateContextForService(dctx, bodyParameters);
                screenContextAtt.putAll(bodyParameters);
                screensAtt.render(xslfoAttachScreenLocation);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FopFactory fopFactory = ApacheFopFactory.instance();
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, baos);
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                Reader reader = new StringReader(writer.toString());
                Source src = new StreamSource(reader);
                Result res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(src, res);
                baos.flush();
                baos.close();
                fopFactory.getImageFactory().clearCaches();
                List bodyParts = FastList.newInstance();
                if (bodyText != null) {
                    bodyText = FlexibleStringExpander.expandString(bodyText, screenContext, locale);
                    bodyParts.add(UtilMisc.toMap("content", bodyText, "type", "text/html"));
                } else {
                    bodyParts.add(UtilMisc.toMap("content", bodyWriter.toString(), "type", "text/html"));
                }
                bodyParts.add(UtilMisc.toMap("content", baos.toByteArray(), "type", "application/pdf", "filename", attachmentName));
                serviceContext.put("bodyParts", bodyParts);
            } catch (GeneralException ge) {
                String errMsg = "Error rendering PDF attachment for email: " + ge.toString();
                Debug.logError(ge, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (IOException ie) {
                String errMsg = "Error rendering PDF attachment for email: " + ie.toString();
                Debug.logError(ie, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (FOPException fe) {
                String errMsg = "Error rendering PDF attachment for email: " + fe.toString();
                Debug.logError(fe, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (TransformerConfigurationException tce) {
                String errMsg = "FOP TransformerConfiguration Exception: " + tce.toString();
                return ServiceUtil.returnError(errMsg);
            } catch (TransformerException te) {
                String errMsg = "FOP transform failed: " + te.toString();
                return ServiceUtil.returnError(errMsg);
            } catch (SAXException se) {
                String errMsg = "Error rendering PDF attachment for email: " + se.toString();
                Debug.logError(se, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            } catch (ParserConfigurationException pe) {
                String errMsg = "Error rendering PDF attachment for email: " + pe.toString();
                Debug.logError(pe, errMsg, module);
                return ServiceUtil.returnError(errMsg);
            }
        } else {
            isMultiPart = false;
            if (bodyText != null) {
                bodyText = FlexibleStringExpander.expandString(bodyText, screenContext, locale);
                serviceContext.put("body", bodyText);
            } else {
                serviceContext.put("body", bodyWriter.toString());
            }
            if (contentType != null && contentType.equalsIgnoreCase("text/plain")) {
                serviceContext.put("contentType", "text/plain");
            } else {
                serviceContext.put("contentType", "text/html");
            }
        }
        String subject = (String) serviceContext.remove("subject");
        subject = FlexibleStringExpander.expandString(subject, screenContext, locale);
        serviceContext.put("subject", subject);
        serviceContext.put("partyId", partyId);
        if (Debug.verboseOn()) Debug.logVerbose("sendMailFromScreen sendMail context: " + serviceContext, module);
        Map result = ServiceUtil.returnSuccess();
        try {
            if (isMultiPart) {
                dispatcher.runSync("sendMailMultiPart", serviceContext);
            } else {
                dispatcher.runSync("sendMail", serviceContext);
            }
        } catch (Exception e) {
            String errMsg = "Error send email :" + e.toString();
            Debug.logError(e, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        }
        result.put("body", bodyWriter.toString());
        return result;
    }
