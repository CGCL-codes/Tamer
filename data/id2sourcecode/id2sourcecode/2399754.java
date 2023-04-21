    public static void main(String[] args) throws IOException, Exception {
        initialize();
        fileUpload();
        refererURL = tmpserver + "/status.html?file=" + UploadID + "=" + file.getName();
        System.out.println("Referer URL : " + refererURL);
        uploadresponse = getData(refererURL);
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpPost httppost = new HttpPost("http://oron.com/");
        if (login) {
            httppost.setHeader("Cookie", logincookie + ";" + xfsscookie);
        }
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("op", "upload_result"));
        formparams.add(new BasicNameValuePair("fn", fnvalue));
        formparams.add(new BasicNameValuePair("st", "OK"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        uploadresponse = EntityUtils.toString(httpresponse.getEntity());
        downloadlink = parseResponse(uploadresponse, "Download Link", "\" class=");
        downloadlink = downloadlink.substring(downloadlink.indexOf("<a href=\""));
        downloadlink = downloadlink.replace("<a href=\"", "");
        System.out.println("Download Link : " + downloadlink);
        deletelink = parseResponse(uploadresponse, "Delete Link", "\"></td>");
        deletelink = deletelink.substring(deletelink.indexOf("value=\""));
        deletelink = deletelink.replace("value=\"", "");
        System.out.println("Delete Link : " + deletelink);
    }
