    public byte[] readDataFully() throws WrapperException {
        int toRead = readInt();
        System.out.println("Should read " + toRead + " bytes");
        byte buf[] = new byte[org.magnesia.Constants.CHUNK_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int read = 0;
        while (read >= 0 && toRead > 0) {
            read = readData(buf, ((toRead >= buf.length) ? buf.length : toRead));
            toRead -= read;
            bos.write(buf, 0, read);
        }
        return bos.toByteArray();
    }
