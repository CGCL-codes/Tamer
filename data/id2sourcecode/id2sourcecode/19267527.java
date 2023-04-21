    private void downloadInkNoteImage(String guid, String authToken) {
        String urlBase = noteStoreUrl.replace("/edam/note/", "/shard/") + "/res/" + guid + ".ink?slice=";
        Integer slice = 1;
        Resource r = conn.getNoteTable().noteResourceTable.getNoteResource(guid, false);
        conn.getInkImagesTable().expungeImage(r.getGuid());
        int sliceCount = 1 + ((r.getHeight() - 1) / 480);
        HttpClient http = new DefaultHttpClient();
        for (int i = 0; i < sliceCount; i++) {
            String url = urlBase + slice.toString();
            HttpPost post = new HttpPost(url);
            post.getParams().setParameter("auth", authToken);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("auth", authToken));
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            try {
                HttpResponse response = http.execute(post);
                HttpEntity resEntity = response.getEntity();
                InputStream is = resEntity.getContent();
                QByteArray data = writeToFile(is);
                conn.getInkImagesTable().saveImage(guid, slice, data);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            slice++;
        }
        http.getConnectionManager().shutdown();
        noteSignal.noteChanged.emit(r.getNoteGuid(), null);
    }
