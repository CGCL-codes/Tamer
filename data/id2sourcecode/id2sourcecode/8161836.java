    public static Map createFileFromScreen(DispatchContext dctx, Map serviceContext) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) serviceContext.get("locale");
        String screenLocation = (String) serviceContext.remove("screenLocation");
        Map screenContext = (Map) serviceContext.remove("screenContext");
        String contentType = (String) serviceContext.remove("contentType");
        String filePath = (String) serviceContext.remove("filePath");
        String fileName = (String) serviceContext.remove("fileName");
        if (UtilValidate.isEmpty(screenContext)) {
            screenContext = FastMap.newInstance();
        }
        screenContext.put("locale", locale);
        if (UtilValidate.isEmpty(contentType)) {
            contentType = "application/pdf";
        }
        try {
            MapStack screenContextTmp = MapStack.create();
            screenContextTmp.put("locale", locale);
            Writer writer = new StringWriter();
            ScreenRenderer screensAtt = new ScreenRenderer(writer, screenContextTmp, htmlScreenRenderer);
            screensAtt.populateContextForService(dctx, screenContext);
            screenContextTmp.putAll(screenContext);
            screensAtt.getContext().put("formStringRenderer", foFormRenderer);
            screensAtt.render(screenLocation);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FopFactory fopFactory = ApacheFopFactory.instance();
            Fop fop = fopFactory.newFop(contentType, baos);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            Reader reader = new StringReader(writer.toString());
            Source src = new StreamSource(reader);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
            baos.flush();
            baos.close();
            fopFactory.getImageFactory().clearCaches();
            fileName += UtilDateTime.nowAsString();
            if ("application/pdf".equals(contentType)) {
                fileName += ".pdf";
            } else if ("application/postscript".equals(contentType)) {
                fileName += ".ps";
            } else if ("text/plain".equals(contentType)) {
                fileName += ".txt";
            }
            if (UtilValidate.isEmpty(filePath)) {
                filePath = UtilProperties.getPropertyValue("content.properties", "content.output.path", "/output");
            }
            File file = new File(filePath, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.close();
        } catch (GeneralException ge) {
            String errMsg = "Error rendering [" + contentType + "]: " + ge.toString();
            Debug.logError(ge, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        } catch (IOException ie) {
            String errMsg = "Error rendering [" + contentType + "]: " + ie.toString();
            Debug.logError(ie, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        } catch (FOPException fe) {
            String errMsg = "Error rendering [" + contentType + "]: " + fe.toString();
            Debug.logError(fe, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        } catch (TransformerConfigurationException tce) {
            String errMsg = "FOP TransformerConfiguration Exception: " + tce.toString();
            return ServiceUtil.returnError(errMsg);
        } catch (TransformerException te) {
            String errMsg = "FOP transform failed: " + te.toString();
            return ServiceUtil.returnError(errMsg);
        } catch (SAXException se) {
            String errMsg = "Error rendering [" + contentType + "]: " + se.toString();
            Debug.logError(se, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        } catch (ParserConfigurationException pe) {
            String errMsg = "Error rendering [" + contentType + "]: " + pe.toString();
            Debug.logError(pe, errMsg, module);
            return ServiceUtil.returnError(errMsg);
        }
        Map result = ServiceUtil.returnSuccess();
        return result;
    }
