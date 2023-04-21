        public Reader getReader() throws IOException {
            if (file != null) {
                return new InputStreamReader(new FileInputStream(file), encoding);
            } else {
                return new InputStreamReader(url.openStream(), encoding);
            }
        }
