    private static void addDir(ZipOutputStream zos, File dir, String root) throws IOException {
        String name = dir.getPath();
        name = name.substring(root.length());
        ZipEntry ze = new ZipEntry(name + "/");
        zos.putNextEntry(ze);
        File[] kids = dir.listFiles();
        for (int i = 0; i < kids.length; i++) {
            if (kids[i].isDirectory()) addDir(zos, kids[i], root); else add(zos, kids[i], root);
        }
    }
