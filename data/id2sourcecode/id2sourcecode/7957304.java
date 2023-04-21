    public static void uploadPicture(String verify, String albumid, String title, File file) throws ContentException, NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpPost post = new HttpPost(HttpUtil.KAIXIN_PIC_UPLOAD_URL + HttpUtil.KAIXIN_PARAM_VERIFY + verify);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT);
        try {
            reqEntity.addPart("albumid", new StringBody(albumid, Charset.forName("UTF-8")));
            reqEntity.addPart("title", new StringBody(title, Charset.forName("UTF-8")));
            reqEntity.addPart("file", new FileBody(file, "image/jpeg"));
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            HTTPUtil.consume(response.getEntity());
            if (!HTTPUtil.isHttp200(response)) throw new ContentException("upload picture failed");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
