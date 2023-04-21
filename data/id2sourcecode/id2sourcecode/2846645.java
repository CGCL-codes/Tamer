    public void run() {
        byte[] buf = new byte[32768];
        int read;
        InputStream stream;
        try {
            logger.fine("Open connection");
            if (proxy != null) urlConnection = u.openConnection(proxy); else urlConnection = u.openConnection();
            urlConnection.setConnectTimeout(10000);
            int cl = urlConnection.getContentLength();
            logger.fine("Content-Length = " + cl);
            synchronized (this) {
                contentLength = cl;
            }
            downloadCallback.initProgress(contentLength);
            stream = urlConnection.getInputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
            while ((read = stream.read(buf)) > 0) {
                bos.write(buf, 0, read);
                synchronized (this) {
                    contentActual += read;
                }
                downloadCallback.progress(contentActual);
            }
            bos.close();
            downloadCallback.ready(null, destFile.getCanonicalPath());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception while downloading a file.", e);
            downloadCallback.ready(e, null);
        }
    }
