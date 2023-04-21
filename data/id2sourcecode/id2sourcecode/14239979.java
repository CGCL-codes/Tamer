    private String newUploadBans(JProgressBar prog) throws SQLException, IOException, HTTPConnectionException {
        String newline = null;
        try {
            newline = System.getProperty("line.separator");
        } catch (Exception e) {
            newline = "\n";
        }
        String username = programState.getForumName();
        String password = programState.getForumPassword();
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
                    bans = bans + info[0] + "\t0\t0\t" + reason + "\n";
                } else {
                    bans = bans + info[0] + "\t0\t0\t" + reason;
                }
            }
            if (bans == null) {
                throw new IOException();
            }
            StringBuffer sb = new StringBuffer();
            URL url = null;
            String body = null;
            url = new URL(NEW_UPLOAD);
            body = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            body += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
                    sb.append(newline);
                }
                prog.setValue(prog.getValue() + 10);
                if (sb.toString().length() > 0) {
                    return sb.toString();
                }
                return null;
            } catch (IOException e) {
                InputStream is = conn.getErrorStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String l;
                StringBuffer errorSB = new StringBuffer();
                while ((l = r.readLine()) != null) {
                    errorSB.append(l);
                    errorSB.append(newline);
                }
                throw new HTTPConnectionException(conn.getResponseCode(), errorSB.toString());
            }
        }
    }
