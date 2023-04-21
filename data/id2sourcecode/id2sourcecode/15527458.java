    public static void copyResourceFile(Class<?> clazz, String sourceResourceFileName, String destFileName) throws URISyntaxException, IOException {
        InputStream srcFileStream = clazz.getResourceAsStream(sourceResourceFileName);
        if (srcFileStream == null) {
            throw new IOException("Resource file " + sourceResourceFileName + " not found");
        }
        File destFile = new File(destFileName);
        if (destFile.isDirectory()) {
            destFile = new File(destFile + File.separator + sourceResourceFileName.substring(sourceResourceFileName.lastIndexOf('/') + 1));
        }
        FileChannel out = null;
        try {
            out = new FileOutputStream(destFile).getChannel();
            byte[] buf = new byte[srcFileStream.available()];
            while (srcFileStream.available() > 0) {
                int size = srcFileStream.read(buf);
                out.write(ByteBuffer.wrap(buf, 0, size));
            }
        } finally {
            srcFileStream.close();
            if (out != null) out.close();
        }
    }
