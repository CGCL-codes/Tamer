    public Person doLogin(String username, String password) throws UnsupportedEncodingException, IOException, ParseException, Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        LoginRequest request = new LoginRequest();
        if (username == null && password == null) {
            request.setLogin(RuntimeAccess.getInstance().getRequest().getParameter("username"));
            request.setPassword(RuntimeAccess.getInstance().getRequest().getParameter("key"));
        } else {
            request.setLogin(username);
            request.setPassword(password);
        }
        XStream xwriter = new XStream();
        xwriter.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        xwriter.alias("LoginRequest", LoginRequest.class);
        XStream xreader = new XStream();
        xreader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        xreader.alias("LoginResponse", LoginResponse.class);
        String strRequest = URLEncoder.encode(xwriter.toXML(request), "UTF-8");
        HttpGet httpget = new HttpGet(MewitProperties.getMewitUrl() + "/resources/login?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        LoginResponse loginResponse = null;
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            log(INFO, "XML=" + result);
            loginResponse = (LoginResponse) xreader.fromXML(result);
            if (loginResponse.getErrors() != null) {
                throw new Exception();
            } else {
                RuntimeAccess.getInstance().getSession().setAttribute("SESSION_ID", loginResponse.getSessionId());
                RuntimeAccess.getInstance().getSession().setAttribute("USER_ID", loginResponse.getPerson().getUsername());
                RuntimeAccess.getInstance().getSession().setAttribute("PERSON", loginResponse.getPerson());
                return loginResponse.getPerson();
            }
        }
        return null;
    }
