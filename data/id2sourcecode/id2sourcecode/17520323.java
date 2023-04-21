    public void initilize() throws Exception {
        url = new URL("http://filefactory.com/");
        conn = url.openConnection();
        filecookie = conn.getHeaderField("Set-Cookie");
        NULogger.getLogger().info(filecookie);
        content = "redirect=%2F&email=" + URLEncoder.encode(getUsername(), "UTF-8") + "&password=" + getPassword();
        postData(content, "http://filefactory.com/member/login.php");
    }
