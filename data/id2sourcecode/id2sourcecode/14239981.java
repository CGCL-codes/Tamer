    public String downloadBans(String list) throws IOException, MalformedURLException, BadTypeException, IncorrectLoginException {
        URLConnection connect = connect();
        String sessionKey = sessionKey(connect);
        StringBuffer sb = new StringBuffer();
        URL url = null;
        if (list != OTHERS && list != OWN) {
            throw new BadTypeException();
        }
        url = new URL(OLD_DOWNLOAD + list);
        String body = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(sessionKey, "UTF-8");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
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
            InputStream is = conn.getErrorStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String l;
            StringBuffer errorSB = new StringBuffer();
            while ((l = r.readLine()) != null) {
                errorSB.append(l);
                errorSB.append("\n");
            }
            return errorSB.toString();
        }
        return sb.toString();
    }
