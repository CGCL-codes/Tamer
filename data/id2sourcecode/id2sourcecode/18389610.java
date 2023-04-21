    public HashMap[] processResponse(HashMap[] vars, byte[] xmlParam) throws NoAttachmentException, ProcessingException, SpecificationViolationException {
        try {
            Element root = getRootNode(xmlParam);
            Element responseProcessing = root.getChild("responseProcessing", ns[0]);
            if (responseProcessing == null) responseProcessing = root.getChild("responseProcessing", ns[1]);
            if (responseProcessing == null) throw new SpecificationViolationException("Couldn't locate responseProcessing node!");
            if (responseProcessing.getChildren().size() == 0) {
                byte[] xml = null;
                String templateLocation = responseProcessing.getAttributeValue("templateLocation");
                if (templateLocation != null) {
                    try {
                        URL url = new URL(templateLocation);
                        xml = StreamCopier.copyToByteArray(url.openStream());
                    } catch (MalformedURLException e) {
                        throw new SpecificationViolationException("Invalid response processing template location URL", e);
                    } catch (IOException e) {
                        throw new SpecificationViolationException("Error reading from remote response processing template", e);
                    }
                } else {
                    String template = responseProcessing.getAttributeValue("template");
                    if (template != null) {
                        String filename = null;
                        try {
                            if (template.matches("http://www.imsglobal.org/question/qti_v2p[01]/rptemplates/match_correct")) {
                                filename = PropertiesLoader.getProperty("r2q2.processing.responsetemplates.matchCorrect");
                            } else if (template.matches("http://www.imsglobal.org/question/qti_v2p[01]/rptemplates/map_response")) {
                                filename = PropertiesLoader.getProperty("r2q2.processing.responsetemplates.mapResponse");
                            } else if (template.matches("http://www.imsglobal.org/question/qti_v2p[01]/rptemplates/map_response_point")) {
                                filename = PropertiesLoader.getProperty("r2q2.processing.responsetemplates.mapResponsePoint");
                            } else {
                                throw new ProcessingException("Unsupported response processing template");
                            }
                        } catch (IOException e) {
                            throw new ProcessingException("Couldn't read properties file", e);
                        } catch (NoSuchPropertyException e) {
                            throw new ProcessingException("Couldn't find template location property", e);
                        }
                        try {
                            xml = StreamCopier.copyToByteArray(new FileInputStream(new File(filename)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            throw new ProcessingException("Couldn't find template", e);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new ProcessingException("Couldn't read template", e);
                        }
                    } else throw new SpecificationViolationException("No response processing rules!");
                }
                try {
                    responseProcessing = DocumentBuilder.getRootElement(xml);
                    if (!responseProcessing.getName().equals("responseProcessing")) throw new SpecificationViolationException("Response template not valid!");
                    if (responseProcessing.getChildren().size() < 1) throw new ProcessingException("No content in template - either a spec violation or an unsupported nested template");
                } catch (JDOMException e) {
                    throw new SpecificationViolationException("Error building DOM from response processing template", e);
                } catch (NoSuchPropertyException e) {
                    throw new ProcessingException(e);
                } catch (IOException e) {
                    throw new SpecificationViolationException("Error reading from response processing template", e);
                }
            }
            HashMap<String, ItemVariable> objectVars = null;
            try {
                objectVars = populateObjectMaps(vars);
            } catch (ConversionException ce) {
                throw new ProcessingException(ce);
            }
            HashMap<String, ItemVariable> responses = null;
            try {
                Iterator it = responseProcessing.getChildren().iterator();
                while (it.hasNext()) responses = processResponseElement((Element) it.next(), objectVars);
            } catch (InvalidArgumentsException iae) {
                iae.printStackTrace();
                throw new ProcessingException(iae);
            }
            try {
                Iterator it = responses.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    Object test = responses.get(key);
                    if (test instanceof ResponseDeclaration) {
                        ResponseDeclaration rd = (ResponseDeclaration) test;
                        boolean correct = true;
                        if (rd.value != null && rd.correctResponse != null) {
                            Vector<Object> candResps;
                            Vector<Object> corrResps;
                            if (rd.card == Cardinality.single) {
                                Object candResp = rd.value;
                                Object corrResp = rd.correctResponse.value;
                                candResps = new Vector();
                                candResps.add(candResp);
                                corrResps = new Vector();
                                corrResps.add(corrResp);
                            } else {
                                candResps = (Vector<Object>) rd.value;
                                corrResps = (Vector<Object>) rd.correctResponse.value;
                                corrResps = (Vector<Object>) corrResps.clone();
                            }
                            if (rd.varType == BaseType.point) {
                                corrResps = new Vector();
                                corrResps = (Vector) rd.areaMapping.areaMapEntries;
                            }
                            if (candResps.size() == corrResps.size()) {
                                if (rd.varType == BaseType.point) {
                                    for (int i = 0; i < candResps.size(); i++) {
                                        Point point = (Point) candResps.get(i);
                                        boolean found = false;
                                        for (int j = 0; j < corrResps.size(); j++) {
                                            AreaMapEntry corr = (AreaMapEntry) corrResps.get(j);
                                            Double[] coordwrap = corr.coords;
                                            double[] coord = new double[coordwrap.length];
                                            for (int x = 0; x < coordwrap.length; x++) {
                                                coord[x] = coordwrap[x].doubleValue();
                                            }
                                            switch(corr.shape) {
                                                case circle:
                                                    found = Inside.insideCircle((Point2D) point, coord);
                                                    break;
                                                case ellipse:
                                                    found = Inside.insideEllipse((Point2D) point, coord);
                                                    break;
                                                case poly:
                                                    found = Inside.insidePolygon((Point2D) point, coord);
                                                    break;
                                                case rect:
                                                    found = Inside.insideRect((Point2D) point, coord);
                                                    break;
                                            }
                                            if (found == true) {
                                                corrResps.remove(j);
                                                break;
                                            }
                                        }
                                        if (!found) {
                                            correct = false;
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < candResps.size(); i++) {
                                        Object cand = candResps.get(i);
                                        boolean found = false;
                                        if (rd.card == Cardinality.ordered) {
                                            Object corr = corrResps.get(i);
                                            if (cand.equals(corr)) {
                                                found = true;
                                            }
                                        } else {
                                            for (int j = 0; j < corrResps.size(); j++) {
                                                Object corr = corrResps.get(j);
                                                if (cand.equals(corr)) {
                                                    found = true;
                                                    corrResps.remove(j);
                                                    break;
                                                }
                                            }
                                        }
                                        if (!found) {
                                            correct = false;
                                        }
                                    }
                                }
                            } else {
                                correct = false;
                            }
                        } else {
                            correct = false;
                        }
                        rd.correct = correct;
                    }
                }
                return rePopulateHashArrays(responses);
            } catch (ConversionException ce) {
                throw new ProcessingException("Error: could not repopulate portable legacy objects correctly, aborting", ce);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
