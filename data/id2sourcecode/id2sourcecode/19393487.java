        public void write(OutputStream outputStream) throws IOException, CMSException {
            InputStream s = sap.getRangeStream();
            int read = 0;
            while ((read = s.read(buf, 0, buf.length)) > 0) {
                outputStream.write(buf, 0, read);
            }
        }
