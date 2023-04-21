    private boolean loginUser(long userId, String ses) {
        try {
            String data = URLEncoder.encode("USERID", "UTF-8") + "=" + URLEncoder.encode("" + userId, "UTF-8");
            data += "&" + URLEncoder.encode("SESSION", "UTF-8") + "=" + URLEncoder.encode(ses, "UTF-8");
            if (_log != null) _log.error("Voice: loginUser = " + m_strUrl + "LoginUserServlet&" + data);
            URL url = new URL(m_strUrl + "LoginUserServlet");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line = rd.readLine();
            int result = Integer.parseInt(line);
            wr.close();
            rd.close();
            if (result == 1) {
                return true;
            }
        } catch (Exception e) {
            if (_log != null) _log.error("Voice error : " + e);
        }
        return false;
    }
