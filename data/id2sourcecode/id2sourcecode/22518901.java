    private InputStream openWithConnection(URL url, String userId, String password, long offset) throws IOException {
        InputStream dis = null;
        String userAgent = "Cortado";
        URLConnection uc = url.openConnection();
        uc.setRequestProperty("Connection", "Keep-Alive");
        String range;
        if (offset != 0 && contentLength != -1) {
            range = "bytes=" + offset + "-" + (contentLength - 1);
        } else if (offset != 0) {
            range = "bytes=" + offset + "-";
        } else {
            range = null;
        }
        if (range != null) {
            Debug.info("doing range: " + range);
            uc.setRequestProperty("Range", range);
        }
        uc.setRequestProperty("User-Agent", userAgent);
        if (userId != null && password != null) {
            String userPassword = userId + ":" + password;
            String encoding = Base64Converter.encode(userPassword.getBytes());
            uc.setRequestProperty("Authorization", "Basic " + encoding);
        }
        uc.setRequestProperty("Content-Type", "application/octet-stream");
        dis = uc.getInputStream();
        String responseRange = uc.getHeaderField("Content-Range");
        if (responseRange == null) {
            Debug.info("Response contained no Content-Range field, assuming offset=0");
            responseOffset = 0;
        } else {
            try {
                MessageFormat format = new MessageFormat("bytes {0,number}-{1,number}", Locale.US);
                java.lang.Object parts[] = format.parse(responseRange);
                responseOffset = ((Number) parts[0]).longValue();
                if (responseOffset < 0) {
                    responseOffset = 0;
                }
                Debug.debug("Stream successfully with offset " + responseOffset);
            } catch (Exception e) {
                Debug.info("Error parsing Content-Range header");
                responseOffset = 0;
            }
        }
        contentLength = uc.getHeaderFieldInt("Content-Length", -1) + responseOffset;
        return dis;
    }
