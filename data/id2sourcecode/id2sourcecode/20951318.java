        private String getHttpLatestVersionString() {
            String line = null;
            BufferedReader dis = null;
            try {
                URL url;
                URLConnection urlConn;
                url = new URL(VERSION_FILE_DOWNLOAD_WEB_SITE);
                urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                dis = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                line = dis.readLine();
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String versionString = "";
            if (line != null) {
                String[] strs = line.split(" is the latest version.");
                if (strs.length >= 1) {
                    versionString = strs[0];
                }
            }
            return versionString;
        }
