    private File download(Show show, String id) throws Exception {
        URLConnection urlConn;
        DataOutputStream printout;
        URL url = new URL("http://v3.newzbin.com/api/dnzb/");
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        printout = new DataOutputStream(urlConn.getOutputStream());
        String content = "username=" + this.username + "&password=" + this.password + "&reportid=" + id;
        printout.writeBytes(content);
        printout.flush();
        printout.close();
        BufferedReader nzbInput = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        File f = File.createTempFile("show", ".nzb");
        BufferedWriter out = new BufferedWriter(new FileWriter(f));
        String str;
        while (null != ((str = nzbInput.readLine()))) out.write(str);
        nzbInput.close();
        out.close();
        return f;
    }
