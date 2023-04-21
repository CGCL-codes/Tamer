    public void autoUpdate() {
        Thread autoUpdateThread = new Thread() {

            public void run() {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    String installDir = FileUtil.getWorkingDir();
                    String updateJarFile = installDir + "EasyPhoto.jar.update";
                    URL url = new URL(jarFileURL);
                    bis = new BufferedInputStream(url.openStream());
                    bos = new BufferedOutputStream(new FileOutputStream(updateJarFile));
                    byte[] buffArr = new byte[1024 * 1024];
                    int n = 0;
                    while ((n = bis.read(buffArr)) != -1) {
                        bos.write(buffArr, 0, n);
                    }
                    updateDone = true;
                    logger.fine("downloaded new jar file for update");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        autoUpdateThread.start();
    }
