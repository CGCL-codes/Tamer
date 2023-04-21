        @Override
        public InputStream getInputStream() throws IOException {
            if (!isDirectory()) {
                URL url = getClass().getResource(path);
                return url.openStream();
            } else {
                throw new IOException("Cannot list");
            }
        }
