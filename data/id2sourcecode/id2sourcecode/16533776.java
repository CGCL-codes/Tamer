    public static void appendBinFile(OutputStream out, File inputFile, boolean enc64, boolean dec64) throws FileNotFoundException, IOException {
        BufferedOutputStream bout = new BufferedOutputStream(out);
        FileInputStream in = new FileInputStream(inputFile);
        Base64InputStream in64 = new Base64InputStream(in, enc64);
        InputStream input = (enc64 ^ dec64) ? in64 : in;
        byte buf[] = new byte[4096];
        byte outBuf[] = buf;
        int read = input.read(buf);
        while (read != -1) {
            bout.write(outBuf, 0, read);
            read = input.read(buf);
        }
        bout.close();
    }
