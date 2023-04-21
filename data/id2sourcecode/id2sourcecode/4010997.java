    public String write(byte[] input) throws java.io.IOException {
        byte[] digest = digester.digest(input);
        StringBuilder filename = new StringBuilder();
        for (byte b : digest) {
            filename.append(Integer.toHexString(b & 0xFF));
        }
        File file = new File(dir, filename.toString());
        FileOutputStream fw = new FileOutputStream(file);
        fw.write(input);
        fw.close();
        return "file://" + file.getAbsolutePath();
    }
