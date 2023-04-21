    public boolean downloadSubtitleZip(String subDownloadLink, File subtitleFile) {
        try {
            URL url = new URL(subDownloadLink);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            connection.setRequestProperty("Cookie", cookieHeader);
            logger.finest("cookieHeader:" + cookieHeader);
            InputStream inputStream = connection.getInputStream();
            String contentType = connection.getContentType();
            logger.finest("contentType:" + contentType);
            if (!contentType.equals("application/x-zip-compressed")) {
                logger.severe("********** Error - Sratim subtitle download limit may have been reached. Suspending subtitle download.");
                subtitleDownload = false;
                return false;
            }
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(inputStream);
            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) {
                String entryName = zipentry.getName();
                logger.finest("ZIP entryname: " + entryName);
                if (entryName.toUpperCase().endsWith(".SRT")) {
                    int n;
                    FileOutputStream fileoutputstream;
                    fileoutputstream = new FileOutputStream(subtitleFile);
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
                    fileoutputstream.close();
                    zipinputstream.close();
                    return true;
                }
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();
            }
            zipinputstream.close();
        } catch (Exception e) {
            logger.severe("Error : " + e.getMessage());
            return false;
        }
        return false;
    }
