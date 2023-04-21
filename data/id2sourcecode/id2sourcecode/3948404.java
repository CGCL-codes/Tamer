    protected File generateTempBundleFile(InputStream stream) throws Exception {
        final String tmpDir = System.getProperty("java.io.tmpdir");
        File extwindFolder = new File(tmpDir, "/extwind/tempbundlefiles");
        if (!extwindFolder.exists()) {
            extwindFolder.mkdirs();
        }
        File tmpBundleFile = new File(extwindFolder, Long.toString(System.currentTimeMillis()));
        logger.debug("Create temp bundle file - " + tmpBundleFile.getAbsolutePath());
        tmpBundleFile.createNewFile();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(stream);
            bos = new BufferedOutputStream(new FileOutputStream(tmpBundleFile));
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
        return tmpBundleFile;
    }
