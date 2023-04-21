    private void writeFile(DataOutputStream out, File file) throws IOException, FileNotFoundException {
        out.writeUTF(file.getName());
        out.writeLong(file.length());
        FileInputStream in = new FileInputStream(file);
        byte[] data = new byte[4096];
        int readLen = 0;
        while ((readLen = in.read(data)) != -1) {
            out.write(data, 0, readLen);
        }
    }
