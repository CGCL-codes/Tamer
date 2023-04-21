    public Document getEndpointDescriptor(ServiceEndpoint endpoint) throws JBIException {
        if (endpoint instanceof ServiceEndpointImpl) {
            Map<String, ?> props = ((ServiceEndpointImpl) endpoint).getProperties();
            if (props != null) {
                String url = (String) props.get(Endpoint.WSDL_URL);
                if (url != null) {
                    InputStream is = null;
                    try {
                        is = new URL(url).openStream();
                        return DOMUtil.parseDocument(is);
                    } catch (Exception e) {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e2) {
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
