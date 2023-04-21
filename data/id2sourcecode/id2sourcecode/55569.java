        public void down() {
            String sURL = "http://www.a3650.com/pos_update/TB_FSK_POS/FSK_POS_1.0.1.6.apk";
            int nStartPos = 0;
            int nRead = 0;
            String sName = "temp.apk";
            String sPath = "e://";
            try {
                URL url = new URL(sURL);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                long nEndPos = getFileSize(sURL);
                RandomAccessFile oSavedFile = new RandomAccessFile(sPath + "//" + sName, "rw");
                httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
                String sProperty = "bytes=" + nStartPos + "-";
                httpConnection.setRequestProperty("RANGE", sProperty);
                System.out.println(sProperty);
                InputStream input = httpConnection.getInputStream();
                byte[] b = new byte[1024];
                while ((nRead = input.read(b, 0, 1024)) > 0 && nStartPos < nEndPos) {
                    oSavedFile.write(b, 0, nRead);
                    nStartPos += nRead;
                }
                httpConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
