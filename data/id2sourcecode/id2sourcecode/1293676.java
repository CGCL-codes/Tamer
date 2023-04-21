        private boolean sendDataToServer(String dataParsed) throws IOException {
            URL url = null;
            HttpURLConnection http = null;
            try {
                url = new URL("http://jmusicmanager.sourceforge.net/jmmm_db_process.php");
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setUseCaches(false);
                http.setDoOutput(true);
                http.setDoInput(true);
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                http.setRequestProperty("Content-Length", Integer.toString(dataParsed.getBytes().length));
                DataOutputStream wr = new DataOutputStream(http.getOutputStream());
                wr.writeBytes(dataParsed);
                wr.flush();
                wr.close();
            } catch (IOException ex) {
                Logger.getLogger(Reporter.class.getName()).log(Level.SEVERE, null, ex);
                http.disconnect();
                return false;
            }
            if (http != null) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String s = bf.readLine();
                bf.close();
                if (s != null && (s.startsWith("inserted") || s.startsWith("updated"))) {
                    http.disconnect();
                    return true;
                } else {
                    http.disconnect();
                    return false;
                }
            } else {
                http.disconnect();
                return false;
            }
        }
