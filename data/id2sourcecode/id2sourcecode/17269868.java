    public void run() {
        refreshDownloadFileData = new RefreshDownloadFileData(this);
        refreshDownloadFileMeter = new RefreshDownloadFileMeter(this);
        try {
            byte[] buffer = new byte[1024];
            URL url = new URL(path + file);
            URLConnection urlc = url.openConnection();
            InputStream in = urlc.getInputStream();
            OutputStream out = new FileOutputStream(tmp.getTemp() + "/" + file);
            size = urlc.getContentLength();
            int n = 0;
            while (!(n < 0) && active) {
                n = in.read(buffer);
                if (!(n < 0)) {
                    out.write(buffer, 0, n);
                    if (size != -1) {
                        downloaded = downloaded + n;
                    }
                }
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MatrixController mc = MatrixController.getInstance();
        mc.addFileToProcess(file);
        refreshDownloadFileData.setActive(false);
        downloadFileView.close();
    }
