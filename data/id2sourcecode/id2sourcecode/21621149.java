    private static void zip(File src, String parentPath, ZipOutputStream out) throws IOException {
        String name = src.getName();
        String path = (parentPath == "") ? name : parentPath + "/" + name;
        if (src.isDirectory()) {
            File[] kids = list(src);
            for (int i = 0; kids != null && i < kids.length; ++i) zip(kids[i], path, out);
        } else {
            out.putNextEntry(new ZipEntry(path));
            InputStream in = new BufferedInputStream(new FileInputStream(src));
            try {
                pipe(in, out);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            out.closeEntry();
        }
    }
