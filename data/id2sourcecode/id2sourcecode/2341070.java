    private synchronized TransformerHandler getTransformerHandler(String stylesheetName) throws ServletException {
        try {
            if (saxTransformerFactory == null) {
                saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
                saxTransformerFactory.setAttribute("http://xml.apache.org/xalan/features/incremental", Boolean.FALSE);
                saxTransformerFactory.setURIResolver(new URIResolver() {

                    public Source resolve(String href, String base) throws TransformerException {
                        try {
                            URL url = filterConfig.getServletContext().getResource("/WEB-INF/" + href);
                            URLConnection connection = url.openConnection();
                            return new SAXSource(new InputSource(connection.getInputStream()));
                        } catch (IOException e) {
                            filterConfig.getServletContext().log("Exception while resolving URL", e);
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                });
            }
            URL url = filterConfig.getServletContext().getResource(stylesheetName);
            URLConnection connection = url.openConnection();
            try {
                long lastModified = connection.getLastModified();
                TemplatesInfo templatesInfo = (TemplatesInfo) templatesCache.get(stylesheetName);
                if (templatesInfo == null || lastModified > templatesInfo.getLastModified()) {
                    SAXSource source = new SAXSource(new InputSource(connection.getInputStream()));
                    source.setSystemId(stylesheetName);
                    templatesInfo = new TemplatesInfo(lastModified, saxTransformerFactory.newTemplates(source));
                    templatesCache.put(stylesheetName, templatesInfo);
                }
                TransformerHandler transformerHandler = saxTransformerFactory.newTransformerHandler(templatesInfo.getTemplates());
                return transformerHandler;
            } finally {
                if (connection != null) connection.getInputStream().close();
            }
        } catch (Exception e) {
            throw new ServletException("Exception caught while getting SAX transformer", e);
        }
    }
