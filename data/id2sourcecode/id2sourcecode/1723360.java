    @Override
    public List<Contact> getContactList() {
        List<Contact> plist = new ArrayList<Contact>();
        try {
            String encodedToken = URLEncoder.encode(token, "UTF-8");
            String u = "http://api.oscar.aol.com/presence/get?f=json&bl=1&k=" + dev_id + "&a=" + encodedToken;
            URL connURL = new URL(u);
            HttpURLConnection urlConn = (HttpURLConnection) connURL.openConnection();
            urlConn.setRequestProperty("referer", "http://localhost:8080/AuthApp/");
            StringBuffer responseBuf = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBuf.append(inputLine);
            }
            LOG.debug(responseBuf.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plist;
    }
