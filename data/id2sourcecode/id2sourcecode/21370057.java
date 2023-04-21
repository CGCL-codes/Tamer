    private void downloadFile(String remoteUrl, String savePath) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(remoteUrl);
            String fileName = savePath + "/" + getShortFileName(remoteUrl);
            showProgress("Opening connection to " + remoteUrl);
            out = new BufferedOutputStream(new FileOutputStream(fileName));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                showProgress("Downloaded " + numWritten + " bytes of file " + getShortFileName(remoteUrl));
            }
        } catch (Exception e) {
            getInstaller().showError(e.getMessage());
            e.printStackTrace();
        }
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        getInstaller().showProgress(getShortFileName(remoteUrl) + " download complete.");
    }
