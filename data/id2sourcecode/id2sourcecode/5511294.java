    public static void main(String[] args) throws Exception {
        initialize();
        loginDivShare();
        System.out.println("Now getting upload url....");
        u = new URL("http://www.divshare.com/upload");
        uc = (HttpURLConnection) u.openConnection();
        uc.setInstanceFollowRedirects(false);
        uc.setRequestProperty("Cookie", cookie.toString());
        Map<String, List<String>> headerFields = uc.getHeaderFields();
        if (headerFields.containsKey("Location")) {
            List<String> header = headerFields.get("Location");
            for (int i = 0; i < header.size(); i++) {
                downURL = header.get(i);
            }
        }
        System.out.println("Upload URL : " + downURL);
        u = new URL(downURL);
        uc = (HttpURLConnection) u.openConnection();
        uc.setInstanceFollowRedirects(false);
        uc.setRequestProperty("Cookie", cookie.toString());
        br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String temp = "", k = "";
        while ((temp = br.readLine()) != null) {
            k += temp;
        }
        sid = parseResponse(k, "sid=", "\"");
        System.out.println("SID : " + sid);
        downURL = downURL.substring(0, downURL.lastIndexOf("upload"));
        System.out.println("Modified upload url : " + downURL);
        fileUpload();
        System.out.println("Now getting download links..............");
        HttpPost hp = new HttpPost(downURL + "scripts/ajax/upload_server_progress.php");
        hp.setHeader("Cookie", cookie.toString());
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("sid", sid));
        formparams.add(new BasicNameValuePair("_", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        hp.setEntity(entity);
        HttpResponse response = httpclient.execute(hp);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(resEntity));
        hp = new HttpPost(downURL + "upload.php");
        hp.setHeader("Cookie", cookie.toString());
        formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("upload_method", "progress"));
        formparams.add(new BasicNameValuePair("gallery_id", "0"));
        formparams.add(new BasicNameValuePair("gallery_title", ""));
        formparams.add(new BasicNameValuePair("gallery_password", ""));
        formparams.add(new BasicNameValuePair("email_to", "julie@gmail.com,patrick@aol.com"));
        formparams.add(new BasicNameValuePair("terms", "on"));
        formparams.add(new BasicNameValuePair("data_form_sid", sid));
        entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        hp.setEntity(entity);
        response = httpclient.execute(hp);
        resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        downloadlink = EntityUtils.toString(resEntity);
        System.out.println(downloadlink);
        downloadlink = parseResponse(downloadlink, "http://www.divshare.com/download/", "\"");
        downloadlink = "http://www.divshare.com/download/" + downloadlink;
        System.out.println("Download link : " + downloadlink);
    }
