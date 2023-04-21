    private synchronized void init() {
        java.net.URL url = null;
        java.net.URLConnection urlConnection = null;
        java.net.HttpURLConnection httpUrlConnection = null;
        int length = 0;
        String type = null;
        String[] parts = null;
        String t = null;
        int index = 0;
        java.io.InputStream inputStream = null;
        try {
            url = new java.net.URL(this.urlString);
            urlConnection = url.openConnection();
            if (!(urlConnection instanceof java.net.HttpURLConnection)) {
                throw new java.lang.IllegalArgumentException(WebDocumentDownloader.EXCEPTION_MESSAGE);
            }
            httpUrlConnection = (java.net.HttpURLConnection) urlConnection;
            httpUrlConnection.setConnectTimeout(WebDocumentDownloader.connectionTimeout);
            httpUrlConnection.setReadTimeout(WebDocumentDownloader.readTimeout);
            httpUrlConnection.setInstanceFollowRedirects(true);
            httpUrlConnection.setRequestProperty("User-Agent", "Opera/9.80 (Windows NT 5.1; U; en-GB) Presto/2.2.15 Version/10.00");
            httpUrlConnection.setRequestProperty("Accept", "text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
            httpUrlConnection.setRequestProperty("Accept-Language", "sk-SK,sk;q=0.9,en;q=0.8");
            httpUrlConnection.setRequestProperty("Accept-Charset", "iso-8859-1, utf-8, utf-16, *;q=0.1");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive, TE");
            httpUrlConnection.setRequestProperty("TE", "deflate, gzip, chunked, identity, trailers");
            httpUrlConnection.connect();
            this.responseHeader = httpUrlConnection.getHeaderFields();
            this.responseCode = httpUrlConnection.getResponseCode();
            this.responseUrl = httpUrlConnection.getURL();
            length = httpUrlConnection.getContentLength();
            type = httpUrlConnection.getContentType();
            if (type != null) {
                parts = type.split(";");
                this.mimeType = parts[0].trim();
                for (int i = 1; i < parts.length && this.charset == null; i++) {
                    t = parts[i].trim();
                    index = t.toLowerCase().indexOf("charset=");
                    if (index != -1) {
                        this.charset = t.substring(index + 8);
                    }
                }
            }
            inputStream = httpUrlConnection.getErrorStream();
            if (inputStream != null) {
                this.content = this.readStream(length, inputStream);
            } else if ((this.content = httpUrlConnection.getContent()) != null && this.content instanceof java.io.InputStream) {
                this.content = this.readStream(length, (java.io.InputStream) this.content);
            }
            httpUrlConnection.disconnect();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
