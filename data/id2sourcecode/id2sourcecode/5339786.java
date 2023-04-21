    static SubtitleDataBase.CreateAccountResponse osdbUserAdd(String user, String pass, String email) throws IOException, TimeoutException, InterruptedException {
        if (user.matches("^[a-zA-Z0-9_-]{3,20}$") == false) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_BAD_LOGIN;
        }
        if (pass.equals("")) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_BAD_PASS;
        }
        if (email.matches("^[a-zA-Z0-9\\-\\_]{1,30}@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$") == false) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_BAD_EMAIL;
        }
        URLConnection conn = null;
        ClientHttpRequest httpPost = null;
        InputStreamReader responseStream = null;
        URL url = new URL("http://www.opensubtitles.org/en/newuser");
        String response = "";
        String line;
        conn = url.openConnection(Global.getProxy());
        httpPost = new ClientHttpRequest(conn);
        httpPost.setParameter("email", "");
        httpPost.setParameter("password", "");
        httpPost.setParameter("UserNickname", user);
        httpPost.setParameter("UserMail", email);
        httpPost.setParameter("UserPassword", pass);
        httpPost.setParameter("UserPassword2", pass);
        httpPost.setParameter("Terms", "on");
        httpPost.setParameter("action", "newuser");
        responseStream = new InputStreamReader(httpPost.post(), "UTF-8");
        BufferedReader responseReader = new BufferedReader(responseStream);
        while ((line = responseReader.readLine()) != null) {
            response += line;
        }
        int index = response.indexOf("<div class=\"msg error\">");
        if (index == -1) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_OK;
        }
        int index2 = response.indexOf("</div>", index);
        if (index2 == -1) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_BAD_UNKNOWN;
        }
        response = response.substring(index + 23, index2);
        response = response.replace("<br />", "\n");
        response = response.replaceAll("<.{1,4}>", "");
        if (response.indexOf("is already taken") != -1) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_LOGIN_EXISTS;
        }
        if (response.indexOf("is already being used") != -1) {
            return SubtitleDataBase.CreateAccountResponse.ADD_USER_EMAIL_EXISTS;
        }
        return SubtitleDataBase.CreateAccountResponse.ADD_USER_BAD_UNKNOWN;
    }
