    private void createUser(long userId, String userName, String userIp) {
        try {
            String data = URLEncoder.encode("USERID", "UTF-8") + "=" + URLEncoder.encode("" + userId, "UTF-8");
            data += "&" + URLEncoder.encode("USERNAME", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8");
            data += "&" + URLEncoder.encode("USERIP", "UTF-8") + "=" + URLEncoder.encode(userIp, "UTF-8");
            if (_log != null) _log.error("Voice: createUser = " + m_strUrl + "CreateUserServlet&" + data);
            URL url = new URL(m_strUrl + "CreateUserServlet");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            wr.close();
            rd.close();
        } catch (Exception e) {
            if (_log != null) _log.error("Voice error : " + e);
        }
    }
