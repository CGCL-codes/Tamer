                public URLConnection getResourceConnection(String s) throws ResourceException {
                    try {
                        ClassLoader xClsLoader = GroovyDRMConnector.class.getClassLoader();
                        URL url = xClsLoader.getResource(s);
                        if (url == null) {
                            url = xClsLoader.getResource("/WEB-INF/groovy/" + s);
                            if (url == null) {
                                throw new ResourceException("Resource " + s + " not found");
                            }
                        }
                        return url.openConnection();
                    } catch (IOException ioe) {
                        throw new ResourceException("Problem reading resource " + s + ": " + ioe.getMessage());
                    }
                }
