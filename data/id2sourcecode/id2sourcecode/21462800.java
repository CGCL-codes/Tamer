    public static Map entityImport(DispatchContext dctx, Map context) {
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Security security = dctx.getSecurity();
        if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
            return ServiceUtil.returnError(UtilProperties.getMessage("WebtoolsUiLabels", "WebtoolsPermissionError", (Locale) context.get("locale")));
        }
        LocalDispatcher dispatcher = dctx.getDispatcher();
        List messages = new ArrayList();
        String filename = (String) context.get("filename");
        String fmfilename = (String) context.get("fmfilename");
        String fulltext = (String) context.get("fulltext");
        boolean isUrl = (String) context.get("isUrl") != null;
        String mostlyInserts = (String) context.get("mostlyInserts");
        String maintainTimeStamps = (String) context.get("maintainTimeStamps");
        String createDummyFks = (String) context.get("createDummyFks");
        Integer txTimeout = (Integer) context.get("txTimeout");
        if (txTimeout == null) {
            txTimeout = new Integer(7200);
        }
        InputSource ins = null;
        URL url = null;
        if (filename != null && filename.length() > 0) {
            try {
                url = isUrl ? new URL(filename) : UtilURL.fromFilename(filename);
                InputStream is = url.openStream();
                ins = new InputSource(is);
            } catch (MalformedURLException mue) {
                return ServiceUtil.returnError("ERROR: invalid file name (" + filename + "): " + mue.getMessage());
            } catch (IOException ioe) {
                return ServiceUtil.returnError("ERROR reading file name (" + filename + "): " + ioe.getMessage());
            } catch (Exception exc) {
                return ServiceUtil.returnError("ERROR: reading file name (" + filename + "): " + exc.getMessage());
            }
        }
        if (fulltext != null && fulltext.length() > 0) {
            StringReader sr = new StringReader(fulltext);
            ins = new InputSource(sr);
        }
        String s = null;
        if (UtilValidate.isNotEmpty(fmfilename) && ins != null) {
            FileReader templateReader = null;
            try {
                templateReader = new FileReader(fmfilename);
            } catch (FileNotFoundException e) {
                return ServiceUtil.returnError("ERROR reading template file (" + fmfilename + "): " + e.getMessage());
            }
            StringWriter outWriter = new StringWriter();
            Template template = null;
            try {
                Configuration conf = org.ofbiz.base.util.template.FreeMarkerWorker.makeDefaultOfbizConfig();
                template = new Template("FMImportFilter", templateReader, conf);
                Map fmcontext = new HashMap();
                NodeModel nodeModel = NodeModel.parse(ins);
                fmcontext.put("doc", nodeModel);
                BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
                TemplateHashModel staticModels = wrapper.getStaticModels();
                fmcontext.put("Static", staticModels);
                template.process(fmcontext, outWriter);
                s = outWriter.toString();
            } catch (Exception ex) {
                return ServiceUtil.returnError("ERROR processing template file (" + fmfilename + "): " + ex.getMessage());
            }
        }
        if (s != null || fulltext != null || url != null) {
            try {
                Map inputMap = UtilMisc.toMap("mostlyInserts", mostlyInserts, "createDummyFks", createDummyFks, "maintainTimeStamps", maintainTimeStamps, "txTimeout", txTimeout, "userLogin", userLogin);
                if (s != null) {
                    inputMap.put("xmltext", s);
                } else {
                    if (fulltext != null) {
                        inputMap.put("xmltext", fulltext);
                    } else {
                        inputMap.put("url", url);
                    }
                }
                Map outputMap = dispatcher.runSync("parseEntityXmlFile", inputMap);
                if (ServiceUtil.isError(outputMap)) {
                    return ServiceUtil.returnError("ERROR: " + ServiceUtil.getErrorMessage(outputMap));
                } else {
                    Long numberRead = (Long) outputMap.get("rowProcessed");
                    messages.add("Got " + numberRead.longValue() + " entities to write to the datasource.");
                }
            } catch (Exception ex) {
                return ServiceUtil.returnError("ERROR parsing Entity Xml file: " + ex.getMessage());
            }
        } else {
            messages.add("No filename/URL or complete XML document specified, doing nothing.");
        }
        Map resp = UtilMisc.toMap("messages", messages);
        return resp;
    }
