    public void beforeNavigate(String url) {
        if (url.toLowerCase().startsWith("urn:")) {
            ((IBrowserWindow) m_context.getProperty(OzoneConstants.s_browserWindow)).navigate(new Resource(url), null, true);
            return;
        }
        try {
            new Thread() {

                void run(String url) {
                    m_url = url;
                    start();
                }

                String m_url;

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
            }.run(url);
            m_source.remove(new Statement(WEB_DESTINATION_TEXT, TEXT, Utilities.generateWildcardResource(1)), Utilities.generateWildcardResourceArray(1));
            m_source.add(new Statement(WEB_DESTINATION_TEXT, TEXT, new Literal(url)));
        } catch (RDFException e) {
        }
        try {
            m_interpreter.callMethod(ADD_PROGRESS_ITEM, new Object[] { WEB_PROGRESS_ITEM }, m_denv);
        } catch (AdenineException e) {
            s_logger.error("Failed to add web progress item", e);
        }
    }
