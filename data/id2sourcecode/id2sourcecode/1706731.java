    public String downloadBans(String list, String sessionKey) {
        StringBuffer sb = new StringBuffer();
        URL url = null;
        String body = null;
        if (list != "others" && list != "own") {
            JOptionPane.showMessageDialog(null, "There's some bad programming going on here, the download URL is mistyped");
        }
        try {
            url = new URL("http://www.banlist.nl/banlist_download.php?list=" + list);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Something's messed up...");
        }
        try {
            body = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(sessionKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Uhh, UTF-8 isn't recognized?  Issues");
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
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error downloading bans!");
            e.printStackTrace();
        }
        return sb.toString();
    }
