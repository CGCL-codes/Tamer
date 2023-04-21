                public void run() {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL(m_url).openConnection());
                        urlc.setRequestMethod("HEAD");
                        urlc.connect();
                        String contentType = urlc.getContentType();
                        if (contentType != null && Utilities.getLiteralProperty(new Resource(m_url), Constants.s_dc_format, m_source) == null) {
                            int semicolon = contentType.indexOf(';');
                            if (semicolon != -1) {
                                contentType = contentType.substring(0, semicolon);
                            }
                            m_infoSource.add(new Statement(new Resource(m_url), Constants.s_dc_format, new Literal(contentType)));
                        }
                    } catch (Exception e) {
                        s_logger.error("Failed to get head info", e);
                    }
                }
