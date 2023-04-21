    public HashMap<String, String> getResource() throws WebResourceException {
        try {
            String data = URLEncoder.encode(POST_USERNAME, "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data = data + "&" + URLEncoder.encode(POST_PASSWORD, "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            URL url = new URL(LOGIN_TO_AUTHORISE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            conn.connect();
            String ss = "";
            String m = null;
            BufferedReader dis = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((ss = dis.readLine()) != null) {
                if (ss.contains("Invalid")) {
                    throw new WebResourceException("Invalid Login Credentials");
                }
                if (ss.contains("does not refresh ")) {
                    m = ss.substring(12, ss.indexOf(">Please click") - 1);
                    m = m.substring(0, m.indexOf("&amp"));
                    break;
                }
            }
            wr.close();
            dis.close();
            if (m == null) {
                throw new WebResourceException("Unable to find destination URL");
            }
            data = URLEncoder.encode(POST_REMEMBER, "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
            data = data + "&" + URLEncoder.encode(POST_SUBMIT, "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8");
            url = new URL(m);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
            String headerName = null;
            HashMap<String, String> cookies = new HashMap<String, String>();
            for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equalsIgnoreCase(SET_COOKIE)) {
                    StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), COOKIE_VALUE_DELIMITER);
                    if (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        String name = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR));
                        String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length());
                        cookies.put(name, value);
                    }
                }
            }
            return cookies;
        } catch (Exception ex) {
            return null;
        }
    }
