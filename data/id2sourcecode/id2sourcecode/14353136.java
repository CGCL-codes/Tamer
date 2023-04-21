        @Override
        public long contentLength() {
            try {
                return url.openConnection().getContentLengthLong();
            } catch (IOException ex) {
                return 0;
            }
        }
