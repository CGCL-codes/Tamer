    public static void streamCopy(InputStream istream, FileOutputStream fos) throws IOException {
        byte[] readBytes = new byte[1024];
        int nBytes;
        while (true) {
            nBytes = istream.read(readBytes, 0, readBytes.length);
            if (nBytes == -1 || nBytes == 0) break;
            fos.write(readBytes, 0, nBytes);
        }
    }
