    protected boolean downloadImage(String url, String outputDirectory, String filename) {
        boolean success = true;
        boolean found = false;
        int attempts = 0;
        log.info("Requesting image URL " + url);
        while (success && !found && (attempts < 5)) {
            found = true;
            try {
                GetMethod get = new GetMethod(url);
                get.setRequestHeader("Accept-Language", "en");
                get.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)");
                get.setRequestHeader("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                get.setRequestHeader("Pragma", "no-cache");
                client.executeMethod(get);
                if ((get.getStatusCode() == 301) || (get.getStatusCode() == 302)) {
                    url = get.getResponseHeader("location").getValue();
                    found = false;
                } else {
                    InputStream in = get.getResponseBodyAsStream();
                    byte[] data = new byte[1024];
                    int length;
                    java.io.FileOutputStream out = new java.io.FileOutputStream(outputDirectory + File.separator + filename);
                    while ((length = in.read(data)) >= 0) out.write(data, 0, length);
                    out.flush();
                    in.close();
                    out.close();
                    get.releaseConnection();
                }
            } catch (Exception e) {
                log.error("Failed to get image", e);
                success = false;
            }
            attempts++;
        }
        return (success);
    }
