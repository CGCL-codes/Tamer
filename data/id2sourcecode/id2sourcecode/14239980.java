    private String oldUploadBans(JProgressBar prog) throws SQLException, IOException, IncorrectLoginException, HTTPConnectionException {
        URLConnection keyConn = connect();
        String sessionKey = sessionKey(keyConn);
        ArrayList<String> banlist = b.getBans(programState.getHostID());
        if (banlist.size() == 0) {
            return "You have no bans to upload";
        } else {
            String bans = "";
            for (int x = 0; x < banlist.size(); x++) {
                String[] info = banlist.get(x).split("\t");
                String reason;
                try {
                    reason = info[1];
                } catch (Exception e) {
                    reason = "leaver";
                }
                if (x != banlist.size() - 1) {
                    bans = bans + info[0] + "\t" + reason + "\t0" + "\r\n";
                } else {
                    bans = bans + info[0] + "\t0\t0\t" + reason + "\t0";
                }
            }
            if (bans == null) {
                throw new IOException();
            }
            StringBuffer sb = new StringBuffer();
            URL url = new URL(OLD_UPLOAD);
            String body = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(sessionKey, "UTF-8");
            body += "&" + URLEncoder.encode("bans", "UTF-8") + "=" + URLEncoder.encode(bans, "UTF-8");
            prog.setValue(prog.getValue() + 10);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-length", Integer.toString(body.length()));
            prog.setValue(prog.getValue() + 10);
            OutputStream rawOutStream = conn.getOutputStream();
            PrintWriter pw = new PrintWriter(rawOutStream);
            pw.print(body);
            pw.flush();
            pw.close();
            prog.setValue(prog.getValue() + 10);
            InputStream rawInStream;
            try {
                rawInStream = conn.getInputStream();
                BufferedReader rdr = new BufferedReader(new InputStreamReader(rawInStream));
                String line;
                while ((line = rdr.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                prog.setValue(100);
                if (sb.toString().length() > 0) {
                    return sb.toString();
                } else {
                    return null;
                }
            } catch (IOException e) {
                InputStream is = conn.getErrorStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String l;
                StringBuffer errorSB = new StringBuffer();
                while ((l = r.readLine()) != null) {
                    errorSB.append(l);
                    errorSB.append("\n");
                }
                throw new HTTPConnectionException(conn.getResponseCode(), errorSB.toString());
            }
        }
    }
