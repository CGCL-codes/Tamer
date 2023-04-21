    public static boolean isResourceExists(URI uri, File cacheDir, UsernamePasswordCredentials creds) {
        String protocol = uri.getScheme();
        if ("file".equals(protocol)) {
            try {
                return new File(uri.toURL().getFile()).exists();
            } catch (MalformedURLException e) {
                logger.warn(e);
            }
        } else if ("http".equals(protocol)) {
            if (cacheDir != null) {
                try {
                    File localFile = RemoteFileManager.getRemoteCacheFile(uri.toURL(), cacheDir);
                    if (localFile.isFile()) {
                        return true;
                    }
                } catch (MalformedURLException e) {
                }
            }
            HttpClient httpclient = createHttpClient(creds);
            try {
                HttpHead httphead = new HttpHead(uri);
                HttpResponse response = httpclient.execute(httphead);
                if (response != null) {
                    StatusLine statusLine = response.getStatusLine();
                    int status = statusLine.getStatusCode() / 100;
                    if (status == 2) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
            } finally {
                httpclient.getConnectionManager().shutdown();
            }
        }
        return false;
    }
