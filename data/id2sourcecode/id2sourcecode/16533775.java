    public static void appendBinFile(OutputStream out, File inputFile) throws FileNotFoundException, IOException {
        BufferedOutputStream bout = new BufferedOutputStream(out);
        FileInputStream in = new FileInputStream(inputFile);
        byte buf[] = new byte[4096];
        int read = in.read(buf);
        while (read != -1) {
            bout.write(buf, 0, read);
            read = in.read(buf);
        }
        bout.close();
    }
