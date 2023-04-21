    private static String readFile(File inputFile) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(inputFile);
        byte[] line = new byte[16384];
        int bytes = -1;
        while ((bytes = in.read(line)) != -1) out.write(line, 0, bytes);
        in.close();
        out.close();
        return out.toString();
    }
