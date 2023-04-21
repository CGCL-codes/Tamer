    public static void copyStreamToFile(InputStream is, File out, boolean read, boolean write, boolean execute) throws IOException, DirectoryDetectedException {
        File outFile = null;
        if (out.isDirectory()) {
            throw new DirectoryDetectedException();
        } else {
            outFile = out;
        }
        OutputStream os = new FileOutputStream(outFile);
        int c;
        byte block[] = new byte[65536];
        while ((c = is.read(block, 0, 65536)) != -1) {
            os.write(block, 0, c);
        }
        if (c != -1) {
            os.write(block, 0, c);
        }
        os.close();
        outFile.setReadable(read);
        outFile.setWritable(write);
        outFile.setExecutable(execute);
    }
