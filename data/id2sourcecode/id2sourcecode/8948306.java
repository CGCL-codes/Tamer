    public long getFileSize() {
        int nFileLength = -1;
        try {
            URL url = new URL(task.getFileUrl());
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "RCP Get");
            int responseCode = httpConnection.getResponseCode();
            if (responseCode >= 400) {
                return -2;
            }
            String sHeader;
            for (int i = 1; ; i++) {
                sHeader = httpConnection.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {
                        nFileLength = Integer.parseInt(httpConnection.getHeaderField(sHeader));
                        break;
                    }
                } else break;
            }
            httpConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nFileLength;
    }
