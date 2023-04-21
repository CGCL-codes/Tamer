        @Override
        public InputStream openRead() {
            try {
                return url.openStream();
            } catch (IOException ex) {
                throw ResourceException.readFail(this);
            }
        }
