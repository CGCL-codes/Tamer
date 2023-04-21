    public String downloadFile(URL location, String fileOutPath, String fileOutName) throws Exception {
        Boolean success = this.feedManager.createDirectory(fileOutPath);
        if (!success) {
            this.currentLabel.setForeground(Color.red);
            this.currentLabel.setText("ERROR: could not create directory for: " + fileOutPath);
            throw new Exception("Can not create directory");
        }
        HttpURLConnection urlConnection = null;
        FileOutputStream fileOut = null;
        InputStream inputStreamReader = null;
        String error = "";
        try {
            final File file = new File(location.getFile());
            if (fileOutName == null) {
                fileOutPath = fileOutPath + "\\" + file.getName();
            } else {
                fileOutPath = fileOutPath + "\\" + fileOutName;
            }
            System.out.println("Downloading file from: " + location.toString());
            System.out.println("Downloading file to: " + fileOutPath);
            fileOut = new FileOutputStream(fileOutPath);
            urlConnection = (HttpURLConnection) location.openConnection();
            urlConnection.connect();
            final int size = urlConnection.getContentLength();
            inputStreamReader = urlConnection.getInputStream();
            final int bufferSize = 2048;
            int byteCount = 0;
            final byte[] buf = new byte[bufferSize];
            int len;
            this.currentProgress.setValue(0);
            while ((len = inputStreamReader.read(buf)) > 0) {
                fileOut.write(buf, 0, len);
                byteCount += len;
                final float progress = 100 * (new Float(byteCount) / new Float(size));
                this.currentProgress.setValue(Math.min((int) progress, 100));
                if (this.stopDownload) {
                    fileOut.close();
                    inputStreamReader.close();
                    urlConnection.disconnect();
                    (new File(fileOutPath)).delete();
                    throw new Exception("Found Stop");
                }
            }
            fileOut.close();
            inputStreamReader.close();
            urlConnection.disconnect();
            this.currentProgress.setValue(100);
            return fileOutPath;
        } catch (Exception se) {
            error = se.toString();
            try {
                fileOut.close();
            } catch (Exception se1) {
                System.out.println("ingore exception");
            }
            try {
                inputStreamReader.close();
            } catch (Exception se2) {
                System.out.println("ingore exception");
            }
            try {
                urlConnection.disconnect();
            } catch (Exception se3) {
                System.out.println("ingore exception");
            }
        }
        throw new Exception("Error in downloading file " + error);
    }
