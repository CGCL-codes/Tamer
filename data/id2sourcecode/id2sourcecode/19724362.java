        public InputStream getInputStream() {
            InputStream rslt = null;
            try {
                rslt = new URL("file:///" + url.toExternalForm()).openStream();
            } catch (Throwable t) {
                String msg = "Unable to read the specified file:  " + url.toExternalForm();
                throw new RuntimeException(msg, t);
            }
            return rslt;
        }
