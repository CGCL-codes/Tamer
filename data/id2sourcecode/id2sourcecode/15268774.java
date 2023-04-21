        public int read(long position, byte[] b, int off, int len) throws IOException {
            ByteBuffer bb = ByteBuffer.wrap(b, off, len);
            try {
                return fis.getChannel().read(bb, position);
            } catch (IOException e) {
                throw new FSError(e);
            }
        }
