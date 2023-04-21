    private void getPreDownloadLink() throws Exception {
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
        fileID = CommonUploaderTasks.parseResponse(downloadlink, "file_id\":\"", "\"");
        downloadlink = CommonUploaderTasks.parseResponse(downloadlink, "\"link\":\"", "\"");
        NULogger.getLogger().log(Level.INFO, "File ID : {0}", fileID);
        NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
    }
