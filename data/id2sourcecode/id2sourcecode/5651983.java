        @Override
        public InputStream openStream() {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
