        public InputStream streamContent() throws IOException {
            if (binaryContent == null) {
                inputStream = url.openStream();
            }
            return super.streamContent();
        }
