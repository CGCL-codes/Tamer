    public static void extract(ZipInputStream zis, File dir, List filesToExtract) throws Exception {
        ZipEntry ze = null;
        OxygenUtils.mkdirs(dir);
        byte[] b = new byte[1024];
        int numread = -1;
        while ((ze = zis.getNextEntry()) != null) {
            String zename = ze.getName();
            if (filesToExtract != null && !filesToExtract.contains(zename)) {
                continue;
            }
            File f = new File(dir, zename);
            if (ze.isDirectory()) {
                OxygenUtils.mkdirs(f);
            } else {
                OxygenUtils.mkdirs(f.getParentFile());
                FileOutputStream fos = new FileOutputStream(f);
                while ((numread = zis.read(b, 0, b.length)) != -1) {
                    fos.write(b, 0, numread);
                }
                CloseUtils.close(fos);
            }
        }
    }
