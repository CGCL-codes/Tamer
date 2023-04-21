    @Override
    public void doWork() throws OperatorException {
        ExampleSet exampleSet = exampleSetInput.getData();
        Attributes attributes = exampleSet.getAttributes();
        List<Pair<String, Attribute>> urlPiecesList = generatePiecesList(attributes, getParameterAsString(PARAMETER_URL));
        int requestMethod = REQUEST_METHOD_GET;
        List<Pair<String, Attribute>> bodyPiecesList = null;
        if (REQUEST_METHODS[REQUEST_METHOD_POST].equals(getParameterAsString(PARAMETER_HTTP_METHOD)) && isParameterSet(PARAMETER_HTTP_BODY)) {
            bodyPiecesList = generatePiecesList(attributes, getParameterAsString(PARAMETER_HTTP_BODY));
            requestMethod = REQUEST_METHOD_POST;
        }
        boolean useSeparator = false;
        String separator = null;
        if (isParameterSet(PARAMETER_SEPARATOR)) {
            useSeparator = true;
            separator = getParameterAsString(PARAMETER_SEPARATOR);
        }
        int delay = getParameterAsInt(PARAMETER_DELAY);
        Charset encoding = Encoding.getEncoding(this);
        Map<String, Pair<Attribute, Query>> attributeQueryMap = QueryService.getAttributeQueryMap(this);
        addAttributesToExampleSet(exampleSet, attributeQueryMap);
        HashMap<String, String> properties = new LinkedHashMap<String, String>();
        properties.put("Content-Type", "text/xml;charset=" + encoding.displayName());
        if (requestMethod == REQUEST_METHOD_POST && isParameterSet(PARAMETER_WEB_SERVICE_METHOD)) {
            properties.put("SOAPAction", getParameterAsString(PARAMETER_WEB_SERVICE_METHOD));
        }
        List<String[]> propertiesList = getParameterList(PARAMETER_REQUEST_PROPERTIES);
        for (String[] property : propertiesList) {
            properties.put(property[0], property[1]);
        }
        FileReader reader = new XMLFileReader();
        boolean firstExample = true;
        for (Example example : exampleSet) {
            checkForStop();
            if (!firstExample) {
                if (delay > 0) try {
                    Thread.sleep(delay);
                } catch (InterruptedException e2) {
                }
            }
            firstExample = false;
            try {
                URL url = new URL(gluePiecesList(urlPiecesList, example));
                URLConnection connection = url.openConnection();
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    if (requestMethod == REQUEST_METHOD_POST) {
                        httpConnection.setRequestMethod("POST");
                        httpConnection.setDoOutput(true);
                        for (Entry<String, String> entry : properties.entrySet()) {
                            httpConnection.addRequestProperty(entry.getKey(), entry.getValue());
                        }
                        String body = gluePiecesList(bodyPiecesList, example);
                        byte[] bodyBytes = body.getBytes(encoding);
                        httpConnection.addRequestProperty("Content-Length", String.valueOf(bodyBytes.length));
                        OutputStream out = httpConnection.getOutputStream();
                        out.write(bodyBytes);
                        out.close();
                    }
                } else {
                    throw new UserError(this, "enrich_data_by_webservice.wrong_protocol");
                }
                String response = reader.readStream(connection.getInputStream(), true, encoding);
                Map<Query, StringTokenizer> queryTokenizerMap = new HashMap<Query, StringTokenizer>();
                for (String attributeName : attributeQueryMap.keySet()) {
                    Pair<Attribute, Query> pair = attributeQueryMap.get(attributeName);
                    Attribute attribute = pair.getFirst();
                    Query query = pair.getSecond();
                    Match queryResult = query.getFirstMatch(response);
                    String value;
                    if (queryResult != null) {
                        if (useSeparator) {
                            if (!queryTokenizerMap.containsKey(query)) {
                                queryTokenizerMap.put(query, new StringTokenizer(queryResult.getMatch(), separator));
                            }
                            StringTokenizer tokenizer = queryTokenizerMap.get(query);
                            if (tokenizer.hasMoreElements()) value = tokenizer.nextToken(); else value = null;
                        } else {
                            value = queryResult.getMatch();
                        }
                    } else {
                        value = null;
                    }
                    if (value != null) {
                        if (attribute.isNumerical()) {
                            try {
                                example.setValue(attribute, NumberParser.parse(value));
                            } catch (NumberFormatException e1) {
                                example.setValue(attribute, Double.NaN);
                            }
                        } else {
                            example.setValue(attribute, attribute.getMapping().mapString(value));
                        }
                    } else {
                        example.setValue(attribute, Double.NaN);
                    }
                }
            } catch (MalformedURLException e) {
                throw new UserError(this, 313);
            } catch (IOException e) {
                throw new UserError(this, e, 314, gluePiecesList(urlPiecesList, example));
            }
        }
        exampleSetOutput.deliver(exampleSet);
    }
