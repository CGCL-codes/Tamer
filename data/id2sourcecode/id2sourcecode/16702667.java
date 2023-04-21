    public static ArrayList<LoadSeries> buildDicomSeriesFromXml(URI uri, final DicomModel model) {
        ArrayList<LoadSeries> seriesList = new ArrayList<LoadSeries>();
        XMLStreamReader xmler = null;
        InputStream stream = null;
        try {
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            String path = uri.getPath();
            URL url = uri.toURL();
            LOGGER.info("Downloading WADO references: {}", url);
            if (path.endsWith(".gz")) {
                stream = GzipManager.gzipUncompressToStream(url);
            } else if (path.endsWith(".xml")) {
                stream = url.openStream();
            } else {
                File outFile = File.createTempFile("wado_", "", AbstractProperties.APP_TEMP_DIR);
                if (FileUtil.writeFile(url, outFile) == -1) {
                    if (MimeInspector.isMatchingMimeTypeFromMagicNumber(outFile, "application/x-gzip")) {
                        stream = new BufferedInputStream((new GZIPInputStream(new FileInputStream((outFile)))));
                    } else {
                        stream = url.openStream();
                    }
                }
            }
            File tempFile = null;
            if (uri.toString().startsWith("file:") && path.endsWith(".xml")) {
                tempFile = new File(path);
            } else {
                tempFile = File.createTempFile("wado_", ".xml", AbstractProperties.APP_TEMP_DIR);
                FileUtil.writeFile(stream, new FileOutputStream(tempFile));
            }
            xmler = xmlif.createXMLStreamReader(new FileReader(tempFile));
            Source xmlFile = new StAXSource(xmler);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                Schema schema = schemaFactory.newSchema(DownloadManager.class.getResource("/config/wado_query.xsd"));
                Validator validator = schema.newValidator();
                validator.validate(xmlFile);
                LOGGER.info("[Validate with XSD schema] wado_query is valid");
            } catch (SAXException e) {
                LOGGER.error("[Validate with XSD schema] wado_query is NOT valid");
                LOGGER.error("Reason: {}", e.getLocalizedMessage());
            } catch (Exception e) {
                LOGGER.error("Error when validate XSD schema. Try to update JRE");
                e.printStackTrace();
            }
            xmler = xmlif.createXMLStreamReader(new FileReader(tempFile));
            int eventType;
            if (xmler.hasNext()) {
                eventType = xmler.next();
                switch(eventType) {
                    case XMLStreamConstants.START_ELEMENT:
                        String key = xmler.getName().getLocalPart();
                        if (WadoParameters.TAG_DOCUMENT_ROOT.equals(key)) {
                            String wadoURL = getTagAttribute(xmler, WadoParameters.TAG_WADO_URL, null);
                            boolean onlySopUID = Boolean.valueOf(getTagAttribute(xmler, WadoParameters.TAG_WADO_ONLY_SOP_UID, "false"));
                            String additionnalParameters = getTagAttribute(xmler, WadoParameters.TAG_WADO_ADDITIONNAL_PARAMETERS, "");
                            String overrideList = getTagAttribute(xmler, WadoParameters.TAG_WADO_OVERRIDE_TAGS, null);
                            String webLogin = getTagAttribute(xmler, WadoParameters.TAG_WADO_WEB_LOGIN, null);
                            final WadoParameters wadoParameters = new WadoParameters(wadoURL, onlySopUID, additionnalParameters, overrideList, webLogin);
                            int pat = 0;
                            MediaSeriesGroup patient = null;
                            while (xmler.hasNext()) {
                                eventType = xmler.next();
                                switch(eventType) {
                                    case XMLStreamConstants.START_ELEMENT:
                                        if (TagW.DICOM_LEVEL.Patient.name().equals(xmler.getName().getLocalPart())) {
                                            patient = readPatient(model, seriesList, xmler, wadoParameters);
                                            pat++;
                                        } else if (WadoParameters.TAG_HTTP_TAG.equals(xmler.getName().getLocalPart())) {
                                            String httpkey = getTagAttribute(xmler, "key", null);
                                            String httpvalue = getTagAttribute(xmler, "value", null);
                                            wadoParameters.addHttpTag(httpkey, httpvalue);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (pat == 1) {
                                final MediaSeriesGroup uniquePatient = patient;
                                GuiExecutor.instance().execute(new Runnable() {

                                    @Override
                                    public void run() {
                                        synchronized (UIManager.VIEWER_PLUGINS) {
                                            for (final ViewerPlugin p : UIManager.VIEWER_PLUGINS) {
                                                if (uniquePatient.equals(p.getGroupID())) {
                                                    p.setSelectedAndGetFocus();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            for (LoadSeries loadSeries : seriesList) {
                                String modality = (String) loadSeries.getDicomSeries().getTagValue(TagW.Modality);
                                boolean ps = modality != null && ("PR".equals(modality) || "KO".equals(modality));
                                if (!ps) {
                                    loadSeries.startDownloadImageReference(wadoParameters);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            FileUtil.safeClose(xmler);
            FileUtil.safeClose(stream);
        }
        return seriesList;
    }
