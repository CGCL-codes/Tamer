    private long getFileSize() {
        if (url == null) return -1;
        int nFileLength = -1;
        try {
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.00; Windows 98)");
            uc.setRequestProperty("JCache-Controlt", "no-cache");
            if (uc instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) uc;
                httpsConnection.setHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String host, SSLSession session) {
                        return true;
                    }
                });
            }
            int responseCode = uc.getResponseCode();
            if (responseCode >= 400) {
                return -2;
            }
            String sHeader;
            for (int i = 1; ; i++) {
                sHeader = uc.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {
                        nFileLength = Integer.parseInt(uc.getHeaderField(sHeader));
                        break;
                    }
                } else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nFileLength;
    }
