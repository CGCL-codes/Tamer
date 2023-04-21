    @Override
    public byte[] getPicture() {
        final String apikey = FlickrConnectorSettingView.apikey;
        final String yahooID = FlickrConnectorSettingView.yahooID;
        if (apikey == null || apikey.equals("") || yahooID == null || yahooID.equals("")) {
            return null;
        }
        Flickr flickr = new Flickr(apikey);
        User user = null;
        try {
            user = flickr.getPeopleInterface().findByEmail(yahooID);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        String id = user.getId();
        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters param = new SearchParameters();
        param.setUserId(id);
        PhotoList photoList = null;
        try {
            photoList = photos.search(param, 100, 0);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        final int size = photoList.size();
        if (size == 0) {
            return null;
        }
        this.index++;
        if (this.index >= size) {
            this.index = 0;
        }
        Photo photo = (Photo) photoList.get(this.index);
        String url = photo.getSmallUrl();
        try {
            InputStream is = new URL(url).openStream();
            return getImageByte(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
