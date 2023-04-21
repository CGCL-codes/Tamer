        public InputSource getInputSource() throws IOException {
            return new InputSource(url.openStream());
        }
