    private long walkinURL(URL testurl, Node baseNode) {
        long ret = 0;
        InputStream list = null;
        StringBuilder toAnalyse = new StringBuilder(BUFFER_SIZE * 2);
        try {
            long resolutionstart = System.currentTimeMillis();
            URLConnection conn = testurl.openConnection();
            long resolutionend = System.currentTimeMillis();
            ret += resolutionend - resolutionstart;
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) conn;
                if (http.getContentLength() > MAX_SIZE) {
                    Message = "This is bad news cause it's may not be a mirror";
                } else {
                    String charset = http.getContentEncoding();
                    if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        list = http.getInputStream();
                        long start = System.currentTimeMillis();
                        byte buffer[] = new byte[BUFFER_SIZE];
                        int read = list.read(buffer);
                        while (read >= 0) {
                            SizeOfDownload += read;
                            if (charset != null) {
                                toAnalyse.append(new String(buffer, 0, read, charset));
                            } else {
                                toAnalyse.append(new String(buffer, 0, read));
                            }
                            buildHttpNodes(toAnalyse, baseNode, testurl);
                            read = list.read(buffer);
                        }
                        long end = System.currentTimeMillis();
                        ret += end - start;
                    } else {
                        Message = http.getResponseMessage();
                    }
                }
                http.disconnect();
            }
        } catch (IOException e) {
            Message = e.getMessage();
        } finally {
            if (list != null) {
                try {
                    list.close();
                } catch (IOException e) {
                }
            }
        }
        return ret;
    }
