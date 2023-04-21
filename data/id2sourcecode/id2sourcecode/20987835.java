    public void send(String message) {
        String query = "id=" + _id + "&arg=" + URLEncoder.encode(message);
        URL url = null;
        try {
            url = new URL(_writeUrl);
        } catch (MalformedURLException e) {
            return;
        }
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            url = new URL(_writeUrl);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(query);
            out.flush();
            in = new DataInputStream(conn.getInputStream());
            String line = null;
            while ((line = in.readLine()) != null) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
