    @SuppressWarnings("unused")
    private static void jarFileCopy(File dest, String fileLoc) throws FileNotFoundException, IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream(fileLoc);
        int len = 32768;
        byte[] buff = new byte[(int) Math.min(len, 32768)];
        String outFile = dest.getAbsoluteFile() + File.separator + fileLoc.substring(fileLoc.lastIndexOf("/"), fileLoc.length());
        FileOutputStream fos = new FileOutputStream(outFile, false);
        while (0 < (len = stream.read(buff))) fos.write(buff, 0, len);
        fos.flush();
        fos.close();
        stream.close();
    }
