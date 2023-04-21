    public void perform() {
        state = RUNNING;
        StringBuilder str = new StringBuilder(target.getProtocol());
        str.append("://");
        str.append(target.getAuthority());
        str.append("/favicon.ico");
        System.err.println("Favicon is @ " + str.toString());
        HttpFetch favicon;
        try {
            favicon = new HttpFetch(str.toString());
        } catch (MalformedURLException e) {
            state = FAILED;
            return;
        }
        favicon.setTimeout(2000);
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            byte[] content = favicon.getBytes();
            if (content == null) {
                state = FAILED;
                return;
            }
            hash.update(content);
            byte[] digest = hash.digest();
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < digest.length; ++i) {
                int value = digest[i];
                if (value < 0) {
                    value += 256;
                }
                if (value <= 0x0f) {
                    res.append("0");
                }
                res.append(Integer.toHexString(value));
            }
            if (res.toString().equals("d41d8cd98f00b204e9800998ecf8427e")) {
                state = FAILED;
                testResults = "";
            } else {
                state = SUCCEEDED;
                testResults = res.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5 algorithm not available on this system, test skipped!");
            state = FAILED;
            return;
        }
    }
