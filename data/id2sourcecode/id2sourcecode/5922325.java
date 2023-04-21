    private void uploadLogin() {
        try {
            status = UploadStatus.INITIALISING;
            if (file.length() > 1073741824) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.maxfilesize") + ": <b>1GB</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            status = UploadStatus.GETTINGCOOKIE;
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to EasyShare");
            HttpPost httppost = new HttpPost("http://www.easy-share.com/accounts/login");
            httppost.setHeader("Referer", "http://www.easy-share.com/");
            httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("login", easyShareAccount.username));
            formparams.add(new BasicNameValuePair("password", easyShareAccount.password));
            formparams.add(new BasicNameValuePair("remember", "1"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Finding cookie to add..");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                if (escookie.getName().equalsIgnoreCase("PHPSESSID")) {
                    NULogger.getLogger().log(Level.INFO, "PHPSESSID:{0}", escookie.getValue());
                    break;
                }
            }
            EntityUtils.consume(httpresponse.getEntity());
            NULogger.getLogger().info("Finding user param");
            HttpGet httpget = new HttpGet("http://www.easy-share.com/accounts/upload");
            httpresponse = httpclient.execute(httpget);
            String str = EntityUtils.toString(httpresponse.getEntity());
            str = str.substring(str.indexOf("user\": \"") + 8);
            str = str.substring(0, str.indexOf("\""));
            NULogger.getLogger().log(Level.INFO, "User param: {0}", str);
            EntityUtils.consume(httpresponse.getEntity());
            NULogger.getLogger().info("Uploading...");
            httppost = new HttpPost("http://upload.easy-share.com/accounts/upload_backend/perform/ajax");
            httppost.setHeader("User-Agent", "Shockwave Flash");
            MultipartEntity requestEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            requestEntity.addPart("Filename", new StringBody(file.getName()));
            requestEntity.addPart("PHPSESSID", new StringBody(escookie.getValue()));
            requestEntity.addPart("user", new StringBody(str));
            requestEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
            requestEntity.addPart("Upload", new StringBody("Submit Query"));
            httppost.setEntity(requestEntity);
            status = UploadStatus.UPLOADING;
            httpresponse = httpclient.execute(httppost);
            String strResponse = EntityUtils.toString(httpresponse.getEntity());
            status = UploadStatus.GETTINGLINK;
            downURL = strResponse.substring(strResponse.indexOf("value=\"http://www.easy-share.com/") + 7);
            downURL = downURL.substring(0, downURL.indexOf("\""));
            delURL = strResponse.substring(strResponse.lastIndexOf("javascript:;\">") + 14);
            delURL = delURL.substring(0, delURL.indexOf("</a>"));
            NULogger.getLogger().log(Level.INFO, "Download Link: {0}", downURL);
            NULogger.getLogger().log(Level.INFO, "Delete link: {0}", delURL);
            uploadFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
            NULogger.getLogger().severe(ex.toString());
            uploadFailed();
        }
    }
