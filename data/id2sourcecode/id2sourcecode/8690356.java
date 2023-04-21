    public static void initilize() throws Exception {
        if (uname.isEmpty() || pwd.isEmpty()) {
            System.out.println("Please give valid username,pwd");
            return;
        }
        try {
            url = new URL("http://mediafire.com/");
            conn = url.openConnection();
            ukeycookie = conn.getHeaderField("Set-Cookie");
            ukeycookie = ukeycookie.substring(0, ukeycookie.indexOf(";"));
            System.out.println(ukeycookie);
        } catch (Exception e) {
        }
        content = "login_email=" + URLEncoder.encode(uname, "UTF-8") + "&login_pass=" + pwd + "&submit_login.x=0&submit_login.y=0";
        System.out.println("Login to mediafire............");
        postData(content, "http://www.mediafire.com/dynamic/login.php");
        if (login) {
            System.out.println("Getting myfiles links........");
            getMyFilesLinks();
            System.out.println("Getting uploadkey value..........");
            getUploadKey();
            System.out.println("uploadkey " + uploadkey);
            System.out.println("Getting MFULConfig value........");
            getMFULConfig();
            postURL = "http://www.mediafire.com/douploadtoapi/?type=basic&" + ukeycookie + "&" + usercookie + "&uploadkey=" + uploadkey + "&filenum=0&uploader=0&MFULConfig=" + mfulconfig;
        } else {
            System.out.println("Invalid username or password. :( ");
        }
    }
