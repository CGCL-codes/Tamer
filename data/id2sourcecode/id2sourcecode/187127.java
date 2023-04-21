    static void add(JarOutputStream j, File base, File file) throws IOException {
        File f = new File(base, file.getPath());
        if (f.isDirectory()) {
            JarEntry e = new JarEntry(new String(file.getPath() + File.separator).replace('\\', '/'));
            e.setSize(file.length());
            j.putNextEntry(e);
            String[] children = f.list();
            if (children != null) {
                for (String c : children) {
                    add(j, base, new File(file, c));
                }
            }
        } else {
            JarEntry e = new JarEntry(file.getPath().replace('\\', '/'));
            e.setSize(f.length());
            j.putNextEntry(e);
            j.write(read(f));
            j.closeEntry();
        }
    }
