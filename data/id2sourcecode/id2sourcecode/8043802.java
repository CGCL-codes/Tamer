    public void doLogin() throws ContactsException {
        try {
            String encode_email = URLEncoder.encode(email, "UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            String md5_pwd = byteArrayToHexString(md.digest(password.getBytes()));
            long time = Calendar.getInstance().getTimeInMillis();
            String encodeBeforeLoginUrl = beforeLoginUrl.replaceFirst("%email", encode_email).replaceFirst("%md5_pwd", md5_pwd).replaceFirst("%time", "" + time);
            doGet(encodeBeforeLoginUrl, "http://mail.sohu.com/");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 36);
            client.getState().addCookie(new Cookie(".sohu.com", "crossdomain", "" + calendar.getTimeInMillis(), "/", calendar.getTime(), false));
            doGet(loginUrl, "http://mail.sohu.com");
            contactsUrl = lastUrl + "#addressList";
        } catch (Exception e) {
            throw new ContactsException("sohu protocol has changed", e);
        }
    }
