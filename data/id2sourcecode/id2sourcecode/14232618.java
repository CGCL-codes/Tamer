    private static void addFile(File file, File top, JarOutputStream jaroutput) throws FileNotFoundException, IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        jaroutput.putNextEntry(makeJarEntry(file, top));
        byte[] readBuf = new byte[READ_BUF_SIZE];
        int readCount;
        while ((readCount = input.read(readBuf, 0, READ_BUF_SIZE)) != -1) jaroutput.write(readBuf, 0, readCount);
        input.close();
    }
