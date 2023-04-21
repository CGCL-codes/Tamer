    static Map<String, Object> httpRequest(String url, String method, String accept, String formPostData, int timeOutInMilliseconds) {
        HttpURLConnection hc = null;
        InputStream input = null;
        OutputStream output = null;
        try {
            hc = (HttpURLConnection) new URL(url).openConnection();
            hc.setRequestMethod(method);
            hc.setDoOutput("POST".equals(method));
            hc.setUseCaches(false);
            hc.setConnectTimeout(timeOutInMilliseconds);
            hc.setReadTimeout(timeOutInMilliseconds);
            hc.addRequestProperty("Accept", accept);
            hc.addRequestProperty("Accept-Encoding", "gzip");
            if (hc.getDoOutput()) {
                output = hc.getOutputStream();
                output.write(formPostData.getBytes("UTF-8"));
                output.flush();
            }
            hc.connect();
            int code = hc.getResponseCode();
            if (code != 200) throw new OpenIdException("Bad response code: " + code);
            boolean gzip = "gzip".equals(hc.getContentEncoding());
            input = hc.getInputStream();
            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            boolean overflow = false;
            int read = 0;
            byte[] buffer = new byte[1024];
            for (; ; ) {
                int n = input.read(buffer);
                if (n == (-1)) break;
                byteArrayOutput.write(buffer, 0, n);
                read += n;
                if (read > MAX_SIZE) {
                    overflow = true;
                    break;
                }
            }
            byteArrayOutput.close();
            if (overflow) throw new RuntimeException();
            byte[] data = byteArrayOutput.toByteArray();
            if (gzip) data = gunzip(data);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Cache-Control", hc.getHeaderField("Cache-Control"));
            map.put("Content-Type", hc.getHeaderField("Content-Type"));
            map.put("Content-Encoding", hc.getHeaderField("Content-Encoding"));
            map.put("Expires", hc.getHeaderField("Expires"));
            map.put(CONTENT, data);
            return map;
        } catch (IOException e) {
            throw new OpenIdException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
            if (hc != null) {
                hc.disconnect();
            }
        }
    }
