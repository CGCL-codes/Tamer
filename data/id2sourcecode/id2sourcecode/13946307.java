    private void downloadFile(final ResourceLoaderStatus status, BaseModel documentModel) {
        String fileName = (String) documentModel.getAttribValue(DOWNLOAD_FILENAME);
        String fileDate = (String) documentModel.getAttribValue(DOWNLOAD_FILEDATE);
        long lastModified = Long.MAX_VALUE;
        if ((fileDate != null) && (fileDate.trim().length() > 0)) {
            try {
                Date date = sdf.parse(fileDate.trim());
                lastModified = date.getTime();
            } catch (Exception ex) {
                System.out.println(fileDate);
                ex.printStackTrace();
            }
        }
        String srcFileName = getSourceFileName(fileName);
        File targetFile = getTargetFile(fileName, true);
        int fileSize = 0;
        try {
            fileSize = Integer.parseInt((String) documentModel.getAttribValue(DOCUMENT_FILESIZE));
        } catch (Exception ex) {
        }
        int bytesSofar = 0;
        try {
            if (!targetFile.exists() || (targetFile.lastModified() < lastModified)) {
                DebugLogger.trace("Downloading file: " + srcFileName);
                URL url = new URL(srcFileName);
                InputStream in = url.openStream();
                OutputStream out = new FileOutputStream(targetFile);
                byte[] buf = new byte[4096];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    bytesSofar += len;
                    documentModel.setAttribValue(DOWNLOAD_PROGRESS, Integer.toString(bytesSofar * 100 / fileSize));
                    status.updateFileProgress();
                }
                out.flush();
                in.close();
                out.close();
            } else documentModel.setAttribValue(DOWNLOAD_PROGRESS, "100");
            urlClassLoader.addURL(targetFile.toURL());
        } catch (Exception e) {
            if (bytesSofar == 0) documentModel.setAttribValue(DOWNLOAD_PROGRESS, "-10000"); else documentModel.setAttribValue(DOWNLOAD_PROGRESS, Integer.toString(-bytesSofar * 100 / fileSize));
            status.updateFileProgress();
            e.printStackTrace();
        }
    }
