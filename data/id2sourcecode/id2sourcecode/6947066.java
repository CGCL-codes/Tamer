    public void start(PipelineContext context) {
        try {
            ProcessorInput dataInput = getInputByName(INPUT_DATA);
            SAXStore store = new SAXStore();
            dataInput.getOutput().read(context, store);
            URL url = (URL) readCacheInputAsObject(context, getInputByName(INPUT_CONFIG), new org.orbeon.oxf.processor.CacheableInputReader() {

                public Object read(PipelineContext context, ProcessorInput input) {
                    try {
                        Document doc = readInputAsDOM4J(context, input);
                        String url = XPathUtils.selectStringValueNormalize(doc, "/config/url");
                        return URLFactory.createURL(url.trim());
                    } catch (MalformedURLException e) {
                        throw new OXFException(e);
                    }
                }
            });
            if (Handler.PROTOCOL.equals(url.getProtocol())) {
                ContentHandler handler = ResourceManagerWrapper.instance().getWriteContentHandler(url.getFile());
                store.replay(handler);
            } else {
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                TransformerHandler identity = TransformerUtils.getIdentityTransformerHandler();
                identity.setResult(new StreamResult(os));
                store.replay(identity);
                os.close();
                if (conn instanceof HttpURLConnection) ((HttpURLConnection) conn).disconnect();
            }
        } catch (Exception e) {
            throw new OXFException(e);
        }
    }
