    public BitmapDrawable getHotelImage(String id) {
        BitmapDrawable image = null;
        String url = "http://labos.diee.unica.it/hotel/Image/view.htm?type=hotel&id=" + id;
        HttpGet httpget = new HttpGet(url);
        try {
            HttpResponse rsp = client.execute(httpget);
            InputStream is = rsp.getEntity().getContent();
            image = new BitmapDrawable(is);
        } catch (Exception e) {
        }
        return image;
    }
