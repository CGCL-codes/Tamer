    private static void getPreDownloadLink() throws IOException {
        DefaultHttpClient d = new DefaultHttpClient();
        HttpPost h = new HttpPost("http://uploading.com/files/generate/?ajax");
        h.setHeader("Cookie", sidcookie + ";" + ucookie + ";" + timecookie + ";" + cachecookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("name", file.getName()));
        formparams.add(new BasicNameValuePair("size", String.valueOf(file.length())));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        h.setEntity(entity);
        HttpResponse r = d.execute(h);
        HttpEntity e = r.getEntity();
        downloadlink = EntityUtils.toString(e);
        fileID = parseResponse(downloadlink, "file_id\":\"", "\"");
        downloadlink = parseResponse(downloadlink, "\"link\":\"", "\"");
        System.out.println("File ID : " + fileID);
        System.out.println("Download link : " + downloadlink);
    }
