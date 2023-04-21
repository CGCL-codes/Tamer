    @Override
    public void run() {
        try {
            status = UploadStatus.INITIALISING;
            String fname = file.getName();
            if (fname.substring(fname.lastIndexOf(".") + 1).equals("txt")) {
                JOptionPane.showMessageDialog(neembuuuploader.NeembuuUploader.getInstance(), "<html><b>" + getClass().getSimpleName() + "</b> " + TranslationProvider.get("neembuuuploader.uploaders.filetypenotsupported") + ": <b>txt</b></html>", getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                uploadFailed();
                return;
            }
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            HttpGet httpget = new HttpGet("http://www.filedude.com");
            HttpResponse httpresponse = httpclient.execute(httpget);
            strResponse = EntityUtils.toString(httpresponse.getEntity());
            link = strResponse.substring(strResponse.indexOf("<form action=\"") + 14);
            link = link.substring(0, link.indexOf("\""));
            NULogger.getLogger().info(link);
            httppost = new HttpPost(link);
            MultipartEntity requestEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            requestEntity.addPart("Filedata", new MonitoredFileBody(file, uploadProgress));
            int randint = new Random().nextInt(10);
            requestEntity.addPart("x", new StringBody((randint * 100 + randint) + ""));
            requestEntity.addPart("y", new StringBody(randint + ""));
            httppost.setEntity(requestEntity);
            status = UploadStatus.UPLOADING;
            NULogger.getLogger().log(Level.INFO, "Going to upload {0}", file);
            httpresponse = httpclient.execute(httppost);
            strResponse = EntityUtils.toString(httpresponse.getEntity());
            status = UploadStatus.GETTINGLINK;
            downURL = strResponse.substring(strResponse.indexOf(start) + start.length());
            downURL = downURL.substring(0, downURL.indexOf("\""));
            NULogger.getLogger().log(Level.INFO, "Download Link: {0}", downURL);
            uploadFinished();
        } catch (Exception ex) {
            ex.printStackTrace();
            NULogger.getLogger().severe(ex.toString());
            uploadFailed();
        }
    }
