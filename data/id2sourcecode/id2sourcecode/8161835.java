    public static Map sendPrintFromScreen(DispatchContext dctx, Map serviceContext) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) serviceContext.get("locale");
        String screenLocation = (String) serviceContext.remove("screenLocation");
        Map screenContext = (Map) serviceContext.remove("screenContext");
        String contentType = (String) serviceContext.remove("contentType");
        String printerContentType = (String) serviceContext.remove("printerContentType");
        String printerName = (String) serviceContext.remove("printerName");
        if (UtilValidate.isEmpty(screenContext)) {
            screenContext = FastMap.newInstance();
        }
        screenContext.put("locale", locale);
        if (UtilValidate.isEmpty(contentType)) {
            contentType = "application/postscript";
        }
        if (UtilValidate.isEmpty(printerContentType)) {
            printerContentType = contentType;
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
            DocFlavor psInFormat = new DocFlavor.INPUT_STREAM(printerContentType);
            InputStream bais = new ByteArrayInputStream(baos.toByteArray());
            Doc myDoc = new SimpleDoc(bais, psInFormat, null);
            PrintServiceAttributeSet psaset = new HashPrintServiceAttributeSet();
            if (UtilValidate.isNotEmpty(printerName)) {
                try {
                    URI printerUri = new URI(printerName);
                    PrinterURI printerUriObj = new PrinterURI(printerUri);
                    psaset.add(printerUriObj);
                } catch (URISyntaxException ue) {
                    Debug.logWarning(ue, "Invalid URI for printer [" + printerName + "]", module);
                }
            }
            PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, null);
            PrintService printer = null;
            if (services.length > 0) {
                if (UtilValidate.isNotEmpty(printerName)) {
                    String sPrinterName = null;
                    for (int i = 0; i < services.length; i++) {
                        PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
                        sPrinterName = ((PrinterName) attr).getValue();
                        if (sPrinterName.toLowerCase().indexOf(printerName.toLowerCase()) >= 0) {
                            printer = services[i];
                            Debug.logInfo("Printer with name [" + sPrinterName + "] selected", module);
                            break;
                        }
                    }
                }
                if (UtilValidate.isEmpty(printer)) {
                    printer = services[0];
                }
            }
            if (UtilValidate.isNotEmpty(printer)) {
                PrintRequestAttributeSet praset = new HashPrintRequestAttributeSet();
                praset.add(new Copies(1));
                DocPrintJob job = printer.createPrintJob();
                job.print(myDoc, praset);
            } else {
                String errMsg = "No printer found with name: " + printerName;
                Debug.logError(errMsg, module);
                return ServiceUtil.returnError(errMsg);
            }
        } catch (PrintException pe) {
            String errMsg = "Error printing [" + contentType + "]: " + pe.toString();
            Debug.logError(pe, errMsg, module);
            return ServiceUtil.returnError(errMsg);
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
