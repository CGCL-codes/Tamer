    public String getEDSSessionToken() {
        String sess = null;
        BufferedReader br = null;
        try {
            URL url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return br.readLine().trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                }
            }
        }
        return sess;
    }
