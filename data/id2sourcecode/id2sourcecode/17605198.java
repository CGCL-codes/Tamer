    public String sendFieldNotes(List<FieldNote> notes) throws Exception {
        final URL url = getFieldNotesURL(mUsername, mHashword, "a=add");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(mTimeout);
        conn.setConnectTimeout(mTimeout);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        StringBuilder p = new StringBuilder();
        for (FieldNote note : notes) {
            p.append(note.gcId);
            p.append(",");
            p.append(note.getDateAsISOString());
            p.append(",");
            p.append(note.logType.text);
            p.append(",\"");
            p.append(note.logText);
            p.append("\"\n");
        }
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(p.toString());
        out.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder b = new StringBuilder();
            InputStream in = conn.getInputStream();
            byte[] buf = new byte[512];
            int count = 0;
            while ((count = in.read(buf, 0, buf.length)) != -1) {
                String tmp = new String(buf, 0, count);
                b.append(tmp);
            }
            return b.toString();
        } else return null;
    }
