    private File getFileOfJarEntry(JarFile jf, JarEntry ent, File root) throws IOException {
        File input = new File(ent.getName());
        BufferedInputStream bis = new BufferedInputStream(jf.getInputStream(ent));
        File dir = new File(root.getParent());
        dir.mkdirs();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(input));
        for (int c; (c = bis.read()) != -1; ) bos.write((byte) c);
        bos.close();
        return input;
    }
