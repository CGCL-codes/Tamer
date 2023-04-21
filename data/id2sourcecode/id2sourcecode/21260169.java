    public IOObject[] apply() throws OperatorException {
        String genericURL = getParameterAsString(PARAMETER_URL);
        String separators = getParameterAsString(PARAMETER_SEPARATORS);
        int delay = getParameterAsInt(PARAMETER_DELAY);
        ExampleSet es = getInput(ExampleSet.class);
        AttributeQueryMap aqMap = null;
        try {
            aqMap = FeatureExtractionUtil.getAttributeQueryMap(getParameters());
        } catch (ExtractionException e3) {
            UserError error = e3.getUserError();
            error.setOperator(this);
            throw error;
        }
        es.getExampleTable().addAttributes(aqMap.getAttributes());
        for (Attribute attribute : aqMap.getAttributes()) es.getAttributes().addRegular(attribute);
        for (Example e : es) {
            String urlStr = null;
            try {
                urlStr = rewriteURL(genericURL, e);
            } catch (URISyntaxException e3) {
                throw new UserError(this, 212, new Object[] { urlStr, e3 });
            }
            TextExtractionWrapper wrapper = null;
            try {
                URL url = new URL(urlStr);
                URLConnection connection = url.openConnection();
                wrapper = new TextExtractionWrapper(connection.getInputStream(), TextExtractionWrapper.CONTENT_TYPE_XML, false);
            } catch (ExtractionException e2) {
                UserError error = e2.getUserError();
                error.setOperator(this);
                throw error;
            } catch (MalformedURLException e2) {
                throw new UserError(this, 212, new Object[] { urlStr, e2 });
            } catch (IOException e2) {
                throw new UserError(this, 302, new Object[] { urlStr, e2 });
            }
            if (wrapper != null) {
                Map<Object, StringTokenizer> queryMap = new HashMap<Object, StringTokenizer>();
                for (Attribute att : aqMap.getAttributes()) {
                    TextExtractor query = aqMap.getQuery(att);
                    StringTokenizer tokenizer = queryMap.get(query);
                    if (tokenizer == null) {
                        Iterator<String> values = null;
                        try {
                            values = wrapper.getValues(query);
                        } catch (ExtractionException e1) {
                            logWarning("Could not extract values from xml:\n" + e1);
                        }
                        if ((values != null) && values.hasNext()) {
                            String result = values.next();
                            if (result != null) {
                                tokenizer = new StringTokenizer(result, separators);
                                queryMap.put(query, tokenizer);
                            }
                        }
                    }
                    if ((tokenizer != null) && tokenizer.hasMoreElements()) {
                        String value = tokenizer.nextToken();
                        if (!att.isNominal()) {
                            double numericalValue;
                            try {
                                numericalValue = NumberParser.parse(value);
                            } catch (NumberFormatException e1) {
                                numericalValue = Double.NaN;
                            }
                            e.setValue(att, numericalValue);
                        } else {
                            e.setValue(att, att.getMapping().mapString(value));
                        }
                    } else {
                        e.setValue(att, Double.NaN);
                    }
                }
            }
            try {
                if (delay > 0) Thread.sleep(delay);
            } catch (InterruptedException e2) {
            }
        }
        return new IOObject[0];
    }
