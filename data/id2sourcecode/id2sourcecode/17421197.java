    public static void initilize() throws Exception {
        url = new URL("http://filefactory.com/");
        conn = url.openConnection();
        cookie = conn.getHeaderField("Set-Cookie");
        System.out.println(cookie);
        content = "redirect=%2F&email=" + URLEncoder.encode(uname, "UTF-8") + "&password=" + pwd;
        postData(content, "http://filefactory.com/member/login.php");
    }
