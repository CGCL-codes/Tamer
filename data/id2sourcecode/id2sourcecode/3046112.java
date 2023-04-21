    public GGPhotoSearch photosSearch(String language, String userId, String tags, String tagsMode, String text, String license, Date minUploadedDate, Date maxUploadedDate, Date minTakenDate, Date maxTakenDate, Date minStartDate, Date maxStartDate, Date minEndDate, Date maxEndDate, String page, String perPage) throws IllegalStateException, GGException, Exception {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("method", "gg.photos.search"));
        qparams.add(new BasicNameValuePair("key", this.key));
        if (null != language) {
            qparams.add(new BasicNameValuePair("language", language));
        }
        if (null != userId) {
            qparams.add(new BasicNameValuePair("user_id", userId));
        }
        if (null != tags) {
            qparams.add(new BasicNameValuePair("tags", tags));
        }
        if (null != tagsMode) {
            qparams.add(new BasicNameValuePair("tags_mode", tagsMode));
        }
        if (null != text) {
            qparams.add(new BasicNameValuePair("text", text));
        }
        if (null != license) {
            qparams.add(new BasicNameValuePair("license", license));
        }
        if (null != minUploadedDate) {
            String minUploadedDateString = isoDate.format(minUploadedDate);
            qparams.add(new BasicNameValuePair("min_uploaded_date", minUploadedDateString));
        }
        if (null != maxUploadedDate) {
            String maxUploadedDateString = isoDate.format(maxUploadedDate);
            qparams.add(new BasicNameValuePair("max_uploaded_date", maxUploadedDateString));
        }
        if (null != minTakenDate) {
            String minTakenDateString = isoDate.format(minTakenDate);
            qparams.add(new BasicNameValuePair("min_taken_date", minTakenDateString));
        }
        if (null != maxTakenDate) {
            String maxTakenDateString = isoDate.format(maxTakenDate);
            qparams.add(new BasicNameValuePair("max_taken_date", maxTakenDateString));
        }
        if (null != minStartDate) {
            String minStartDateString = isoDate.format(minStartDate);
            qparams.add(new BasicNameValuePair("min_start_date", minStartDateString));
        }
        if (null != maxStartDate) {
            String maxStartDateString = isoDate.format(maxStartDate);
            qparams.add(new BasicNameValuePair("max_start_date", maxStartDateString));
        }
        if (null != minEndDate) {
            String minEndDateString = isoDate.format(minEndDate);
            qparams.add(new BasicNameValuePair("min_end_date", minEndDateString));
        }
        if (null != maxEndDate) {
            String maxEndDateString = isoDate.format(maxEndDate);
            qparams.add(new BasicNameValuePair("max_end_date", maxEndDateString));
        }
        if (null != page) {
            qparams.add(new BasicNameValuePair("page", page));
        }
        if (null != perPage) {
            qparams.add(new BasicNameValuePair("per_page", perPage));
        }
        String url = REST_URL + "?" + URLEncodedUtils.format(qparams, "UTF-8");
        URI uri = new URI(url);
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        errorCheck(response, status);
        InputStream content = response.getEntity().getContent();
        GGPhotoSearch photoSearch = JAXB.unmarshal(content, GGPhotoSearch.class);
        return photoSearch;
    }
