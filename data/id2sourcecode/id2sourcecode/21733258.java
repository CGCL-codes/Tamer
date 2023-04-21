    public BufferedInputStream getHttpInputStream(String input, int bufLen, long startPosition) {
        BufferedInputStream reader = null;
        Pattern p = Pattern.compile("(https*)://(\\S+):(\\S+)@(\\S+)");
        Matcher m = p.matcher(input);
        boolean result = m.find();
        String protocol = null;
        String user = null;
        String pass = null;
        String URL = input;
        if (result) {
            protocol = m.group(1);
            user = m.group(2);
            pass = m.group(3);
            URL = protocol + "://" + m.group(4);
        }
        URL urlObj = null;
        try {
            urlObj = new URL(URL);
            URLConnection urlConn = urlObj.openConnection();
            if (user != null && pass != null) {
                String userPassword = user + ":" + pass;
                String encoding = Base64.encodeBase64String(userPassword.getBytes());
                urlConn.setRequestProperty("Authorization", "Basic " + encoding);
            }
            urlConn.setRequestProperty("Range", "bytes=" + startPosition + "-");
            p = Pattern.compile("://([^/]+)/(\\S+)");
            m = p.matcher(URL);
            result = m.find();
            if (result) {
                String path = m.group(2);
                String[] paths = path.split("/");
                fileName = paths[paths.length - 1];
                this.size = urlConn.getContentLength();
                reader = new BufferedInputStream(urlConn.getInputStream(), bufLen);
            } else {
                return (null);
            }
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return (null);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return (null);
        }
        return reader;
    }
