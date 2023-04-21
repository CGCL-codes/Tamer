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
