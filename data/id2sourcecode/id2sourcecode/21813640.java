        protected Object parse(String path, URL url) throws Exception {
            return readAll(url.openStream());
        }
