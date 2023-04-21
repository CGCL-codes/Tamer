        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            String file = systemId.substring(systemId.lastIndexOf('/') + 1);
            URL url;
            if (m_engine.getServletContext() == null) {
                ClassLoader cl = WebContainerAuthorizer.class.getClassLoader();
                url = cl.getResource("WEB-INF/dtd/" + file);
            } else {
                url = m_engine.getServletContext().getResource("/WEB-INF/dtd/" + file);
            }
            if (url != null) {
                InputSource is = new InputSource(url.openStream());
                log.log(Level.INFO, "Resolved systemID=" + systemId + " using local file " + url);
                return is;
            }
            log.log(Level.INFO, "Please note: There are no local DTD references in /WEB-INF/dtd/" + file + "; falling back to default behaviour." + " This may mean that the XML parser will attempt to connect to the internet to find the DTD." + " If you are running JSPWiki locally in an unconnected network, you might want to put the DTD files in place to avoid nasty UnknownHostExceptions.");
            return null;
        }
