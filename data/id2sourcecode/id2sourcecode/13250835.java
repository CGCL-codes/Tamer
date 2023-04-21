    public byte[] readBuffer() throws IOException {
        if (instream != null) {
            if (instream.available() == 0) return null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (; instream.available() > 0; stream.write(instream.read())) ;
            return stream.toByteArray();
        } else return streamin.readBuffer();
    }
