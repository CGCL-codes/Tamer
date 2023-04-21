        public InputStream openResource(Class cls, String resourceID) {
            try {
                URL url = new URL(resourceID);
                return url.openStream();
            } catch (MalformedURLException e) {
                LOG.debug("Unable to open url " + resourceID, e);
                return null;
            } catch (IOException e) {
                LOG.error("Unable to read url " + resourceID, e);
                return null;
            }
        }
