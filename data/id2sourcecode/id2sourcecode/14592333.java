    private static byte[] getFileBytes(JarFile jar, JarEntry entry) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream((int) entry.getSize());
        byte[] buf = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(jar.getInputStream(entry));
        int count;
        while ((count = in.read(buf)) > -1) stream.write(buf, 0, count);
        in.close();
        stream.close();
        return stream.toByteArray();
    }
