    private static void unzip(ZipInputStream zin, File destDir) throws Exception {
        log("unzip begin");
        double progress = 1.0D;
        int numEntries = 0;
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            numEntries++;
            if (ze.isDirectory()) {
                zin.closeEntry();
            } else {
                String fname = (new StringBuilder()).append(destDir.getAbsolutePath()).append(File.separator).append(ze.getName().replace('/', File.separatorChar)).toString();
                File f = new File(fname);
                if (f.getParent() != null) {
                    File parent = f.getParentFile();
                    parent.mkdirs();
                }
                byte buf[] = new byte[2048];
                FileOutputStream fos = new FileOutputStream(f);
                int len;
                while ((len = zin.read(buf)) != -1) fos.write(buf, 0, len);
                fos.close();
                zin.closeEntry();
                progress += 0.33333333333333331D;
                if (progress > 98D) progress = 98D;
            }
        }
        zin.close();
        log((new StringBuilder()).append("unzip end (").append(numEntries).append(" entries)").toString());
    }
