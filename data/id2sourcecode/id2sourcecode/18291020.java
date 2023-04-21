    private void readEPSImage(BufferedInputStream fis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] file;
        byte[] readBuf = new byte[20480];
        int bytes_read;
        int index = 0;
        boolean cont = true;
        try {
            while ((bytes_read = fis.read(readBuf)) != -1) {
                baos.write(readBuf, 0, bytes_read);
            }
        } catch (java.io.IOException ex) {
            throw new IOException("Error while loading EPS image " + ex.getMessage());
        }
        file = baos.toByteArray();
        if (isAscii) {
            rawEps = null;
            epsFile = new byte[file.length];
            System.arraycopy(file, 0, epsFile, 0, epsFile.length);
        } else {
            rawEps = new byte[file.length];
            epsFile = new byte[(int) psLength];
            System.arraycopy(file, 0, rawEps, 0, rawEps.length);
            System.arraycopy(rawEps, (int) psStart, epsFile, 0, (int) psLength);
        }
    }
