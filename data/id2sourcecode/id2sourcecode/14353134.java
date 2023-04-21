        @Override
        public long lastModified() {
            try {
                return url.openConnection().getLastModified();
            } catch (IOException ex) {
                return 0;
            }
        }
