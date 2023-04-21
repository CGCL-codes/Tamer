    public String getFilename() {
        filename = "";
        if (huc == null) {
            try {
                huc = (HttpURLConnection) new URL(urlString).openConnection();
                huc.setRequestProperty("Connection", "Keep-Alive");
                if (!isDirect) {
                    int code = huc.getResponseCode();
                    if (code == 200) {
                        String sHeader;
                        for (int i = 1; ; i++) {
                            sHeader = huc.getHeaderFieldKey(i);
                            if (sHeader != null) {
                                if (sHeader.toLowerCase().equals("content-disposition")) {
                                    String val = huc.getHeaderField(sHeader);
                                    int sind = val.toLowerCase().indexOf("filename=");
                                    if (sind != -1) {
                                        String sub = val.substring(sind);
                                        filename = myTrim(sub);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }
