    public static boolean publish(String torrentPath, String trackerName, String username, String pwd, String torrentRename, String info, String comment, String catid) {
        URL url;
        URLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        int tracker = 1;
        if (trackerName.equalsIgnoreCase("")) {
            tracker = new Integer(IOManager.readUserInput("On which tracker do you want your " + "torrent to be published?\r\n1. " + "smartorrent.com\r\n\t2.Localhost\r\nYour choice: ")).intValue();
        } else if (trackerName.contains("smartorrent")) {
            tracker = 1;
        } else tracker = 2;
        ClientHttpRequest c = null;
        try {
            switch(tracker) {
                case 2:
                    c = new ClientHttpRequest(trackerName);
                    c.setParameter("name", torrentRename);
                    c.setParameter("torrent", new File(torrentPath));
                    c.setParameter("info", info);
                    c.setParameter("comment", comment);
                    c.post();
                    break;
                case 1:
                default:
                    url = new URL("http://www.smartorrent.com/?page=login");
                    urlConn = url.openConnection();
                    Map<String, List<String>> headers = urlConn.getHeaderFields();
                    List<String> values = headers.get("Set-Cookie");
                    String cookieValue = null;
                    for (Iterator iter = values.iterator(); iter.hasNext(); ) {
                        String v = (((String) iter.next()).split(";"))[0];
                        if (cookieValue == null) cookieValue = v; else cookieValue = cookieValue + ";" + v;
                    }
                    url = new URL("http://www.smartorrent.com/?page=login");
                    urlConn = url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConn.setRequestProperty("Referer", "http://www.smartorrent.com/?page=upload");
                    urlConn.setRequestProperty("Cookie", cookieValue);
                    printout = new DataOutputStream(urlConn.getOutputStream());
                    String data = URLEncoder.encode("loginreturn", "UTF-8") + "=" + URLEncoder.encode("/?page=login", "UTF-8");
                    data += "&" + URLEncoder.encode("loginusername", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("loginpassword", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");
                    printout.writeBytes(data);
                    printout.flush();
                    printout.close();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while (rd.readLine() != null) ;
                    rd.close();
                    c = new ClientHttpRequest("http://www.smartorrent.com/?page=upload");
                    String[] cookie = cookieValue.split("=");
                    c.setCookie(cookie[0], cookie[1]);
                    c.postCookies();
                    c.setParameter("name", torrentRename);
                    c.setParameter("torrent", new File(torrentPath));
                    c.setParameter("catid", catid);
                    c.setParameter("info", info);
                    c.setParameter("comment", comment);
                    c.setParameter("submit", "Upload");
                    c.post();
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
