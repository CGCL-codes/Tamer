        public InputStream open() throws IOException {
            URLConnection uc = url.openConnection();
            lastModified = uc.getLastModified();
            return new InputStreamImpl(uc.getInputStream(), uc.getContentLength());
        }
