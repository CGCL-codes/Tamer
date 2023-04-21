    public void uploadBans(String sessionKey) {
        String bans = "TEST\tTEST REASON\t0";
        StringBuffer sb = new StringBuffer();
        URL url = null;
        String body = null;
        try {
            url = new URL("http://www.banlist.nl/banlist_upload.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Something's messed up...");
        }
        try {
            body = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(sessionKey, "UTF-8");
            body += "&" + URLEncoder.encode("bans", "UTF-8") + "=" + URLEncoder.encode(bans, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Uhh, UTF-8 isn't recognized?  Issues");
        }
        String newline = null;
        try {
            newline = System.getProperty("line.separator");
        } catch (Exception e) {
            newline = "\n";
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-length", Integer.toString(body.length()));
            OutputStream rawOutStream = conn.getOutputStream();
            PrintWriter pw = new PrintWriter(rawOutStream);
            pw.print(body);
            pw.flush();
            pw.close();
            InputStream rawInStream = conn.getInputStream();
            BufferedReader rdr = new BufferedReader(new InputStreamReader(rawInStream));
            String line;
            while ((line = rdr.readLine()) != null) {
                sb.append(line);
                sb.append(newline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
