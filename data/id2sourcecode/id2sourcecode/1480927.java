    private static void getDownloadLink() throws Exception {
        System.out.println("Now Getting Download link...");
        HttpClient client = new DefaultHttpClient();
        HttpGet h = new HttpGet(uploadresponse);
        h.setHeader("Referer", postURL);
        h.setHeader("Cookie", sidcookie + ";" + mysessioncookie);
        HttpResponse res = client.execute(h);
        HttpEntity entity = res.getEntity();
        linkpage = EntityUtils.toString(entity);
        linkpage = linkpage.replaceAll("\n", "");
        downloadlink = parseResponse(linkpage, "value=\"", "\"");
        deletelink = parseResponse(linkpage, "delete.html?", "\"");
        deletelink = "http://www.zshare.net/delete.html?" + deletelink;
        System.out.println("Download link : " + downloadlink);
        System.out.println("Delete Link : " + deletelink);
    }
