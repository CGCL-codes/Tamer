        public BufferWriter write(BufferReader in, int length) {
            try {
                return write((BufferImpl) in, length);
            } catch (ClassCastException e) {
                return writeBytes(in.readBytes(length));
            }
        }
