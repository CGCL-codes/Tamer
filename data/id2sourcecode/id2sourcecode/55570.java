        public static long getFileSize(String sURL) {
            int nFileLength = -1;
            try {
                URL url = new URL(sURL);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
                int responseCode = httpConnection.getResponseCode();
                if (responseCode >= 400) {
                    System.err.println("Error Code : " + responseCode);
                    return -2;
                }
                System.out.println(httpConnection.getContentLength());
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(nFileLength);
            return nFileLength;
        }
