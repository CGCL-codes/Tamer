    public static void copyStreamToFileWithCheck(InputStream is, File out, boolean read, boolean write, boolean execute) throws FileNotFoundException, IOException, DirectoryDetectedException {
        if (is == null) throw new NullPointerException("is is null");
        if (out == null) throw new NullPointerException("out is null");
        if (!out.getParentFile().isDirectory()) if (!out.getParentFile().mkdirs()) throw new IOException("Can't create directories");
        if (!out.exists()) {
            copyStreamToFile(is, out, read, write, execute);
        } else {
            String sumIs = null;
            String sumTmp = null;
            File tmpFile = File.createTempFile("tmp", ".tmp");
            copyStreamToFile(is, tmpFile);
            sumIs = checksum(is);
            sumTmp = checksum(new FileInputStream(tmpFile));
            if (sumIs.equals(sumTmp)) {
                if (!tmpFile.delete()) throw new IOException("cannot delete file \"" + tmpFile.getAbsolutePath() + "\"");
            } else {
                if (!out.delete()) throw new IOException("cannot delete file \"" + out.getAbsolutePath() + "\"");
                copyFile(tmpFile, out, read, write, execute);
            }
        }
    }
