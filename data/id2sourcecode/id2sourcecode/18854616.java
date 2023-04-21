    public void addJarElement(JarElement jarelement) throws IOException {
        jarOutputStream.putNextEntry(jarelement.getJarEntry());
        byte abyte0[] = new byte[1024];
        int i;
        while ((i = jarelement.getInputStream().read(abyte0)) >= 0) jarOutputStream.write(abyte0, 0, i);
    }
