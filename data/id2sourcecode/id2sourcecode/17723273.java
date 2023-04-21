    public static void downloadSrc(String pkgName, String version, File destFolder) throws IOException {
        File sourceZip = new File(destFolder, pkgName + "_" + version + ".tar.gz");
        if (sourceZip.exists()) {
            System.out.println(sourceZip + ": already exists.");
        } else {
            System.out.println(sourceZip + ": downloading...");
            URL url = new URL(CRAN.CRAN_MIRROR + "src/contrib/" + sourceZip.getName());
            try {
                InputStream in = url.openStream();
                FileOutputStream out = new FileOutputStream(sourceZip);
                ByteStreams.copy(in, out);
                in.close();
                out.close();
            } catch (Exception e) {
                sourceZip.delete();
                e.printStackTrace();
            }
        }
    }
