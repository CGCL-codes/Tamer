        public InputStream getInputStream(String name) throws IOException {
            URL url = getURL(name);
            return (url == null) ? null : url.openStream();
        }
