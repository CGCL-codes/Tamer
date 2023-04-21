    public Bitmap loadImage(String imageUrl) {
        HttpClient client = null;
        HttpGet get = new HttpGet(imageUrl);
        InputStream is = null;
        try {
            client = getHttpClient();
            HttpResponse response = client.execute(get);
            is = response.getEntity().getContent();
            return BitmapFactory.decodeStream(is);
        } catch (Throwable e) {
            LogUtil.error(e);
            return null;
        } finally {
            CommonUtil.closeInputStream(is);
            if (client != null) client.getConnectionManager().shutdown();
        }
    }
