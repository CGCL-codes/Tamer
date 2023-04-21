    public static String httpPost(String style) throws Exception {
        URL url;
        URLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        url = new URL("http://beer.tzo.com/beer/asp/item_recipe.asp");
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        printout = new DataOutputStream(urlConn.getOutputStream());
        String content = "category=" + URLEncoder.encode(style, "UTF-8") + "&DB=" + URLEncoder.encode("beer") + "&VersionNum=" + URLEncoder.encode("20020916");
        printout.writeBytes(content);
        printout.flush();
        printout.close();
        input = new DataInputStream(urlConn.getInputStream());
        String res = "";
        String str;
        while (null != ((str = input.readLine()))) {
            res += str;
        }
        input.close();
        return res;
    }
