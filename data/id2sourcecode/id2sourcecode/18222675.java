    public void download(Downloader glloader) {
        if (downloaded || url == null) {
            return;
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String charset = "UTF-8";
        StringBuffer body = new StringBuffer();
        int contentLength = 0;
        int downloadedSize = 0;
        try {
            int BUFSIZE = 2048;
            char[] buf = new char[BUFSIZE];
            int l;
            connection = (HttpURLConnection) url.openConnection();
            if (timeZone != null) {
                StringBuilder str = new StringBuilder();
                str.append("timeZone=");
                str.append(timeZone.getID());
                connection.setRequestProperty("Cookie", str.toString());
            }
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            contentLength = connection.getContentLength();
            if (glloader != null) {
                glloader.firePropertyChange("lenght", 0, contentLength);
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            while ((l = reader.read(buf, 0, BUFSIZE)) != -1) {
                int olddownloaded = downloadedSize;
                downloadedSize += l;
                body.append(buf, 0, l);
                if (glloader != null) {
                    glloader.firePropertyChange("downloded", olddownloaded, downloadedSize);
                }
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Page.download : SocketTimeoutException:" + e);
            body = new StringBuffer();
        } catch (IOException e) {
            System.err.println("Page.download : IOException:" + e);
            body = new StringBuffer();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (body.length() == 0) {
            downloaded = false;
        } else {
            downloaded = true;
            setHtml(body.toString());
        }
        System.out.println("Page.download: done");
    }
