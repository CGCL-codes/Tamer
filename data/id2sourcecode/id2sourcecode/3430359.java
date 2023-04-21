        public byte[] asByteArray() {
            if (buf == null) {
                ByteArrayBuffer baf = new ByteArrayBuffer();
                try {
                    baf.write(part.readOnce());
                } catch (IOException ioe) {
                    throw new WebServiceException(ioe);
                }
                buf = baf.toByteArray();
            }
            return buf;
        }
