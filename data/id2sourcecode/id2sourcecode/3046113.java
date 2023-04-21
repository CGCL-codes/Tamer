    public GGPhotoInfo upload(File file, String provinceId, String municipalityId, String folderId, String licenseId, String sourceId, String title, String description, String titleEs, String descriptionEs, String titleEu, String descriptionEu, Date date, Boolean isDateUncertain, String photographer, String signature, Time time, Date startDate, Date endDate, String latitude, String longitude, String language, String tags) throws IllegalStateException, GGException, Exception {
        HttpPost httpPost = new HttpPost(UPLOAD_URL);
        FileBody photoFile = new FileBody(file);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("photo", photoFile);
        reqEntity.addPart("license_id", new StringBody(licenseId));
        if (null != provinceId) {
            reqEntity.addPart("province_id", new StringBody(provinceId));
        }
        if (null != municipalityId) {
            reqEntity.addPart("municipality_id", new StringBody(municipalityId));
        }
        if (null != folderId) {
            reqEntity.addPart("folder_id", new StringBody(folderId));
        }
        if (null != sourceId) {
            reqEntity.addPart("source_id", new StringBody(sourceId));
        }
        if (null != title) {
            reqEntity.addPart("title", new StringBody(title));
        }
        if (null != description) {
            reqEntity.addPart("description", new StringBody(description));
        }
        if (null != titleEs) {
            reqEntity.addPart("title_es", new StringBody(titleEs));
        }
        if (null != descriptionEs) {
            reqEntity.addPart("description_es", new StringBody(descriptionEs));
        }
        if (null != titleEu) {
            reqEntity.addPart("title_eu", new StringBody(titleEu));
        }
        if (null != descriptionEu) {
            reqEntity.addPart("description_eu", new StringBody(descriptionEu));
        }
        if (null != date) {
            String dateString = isoDate.format(date);
            reqEntity.addPart("date", new StringBody(dateString));
        }
        if (null != isDateUncertain) {
            String booleanString = isDateUncertain ? "true" : "false";
            reqEntity.addPart("is_date_uncertain", new StringBody(booleanString));
        }
        if (null != photographer) {
            reqEntity.addPart("photographer", new StringBody(photographer));
        }
        if (null != signature) {
            reqEntity.addPart("signature", new StringBody(signature));
        }
        if (null != time) {
            String timeString = isoTime.format(time);
            reqEntity.addPart("time", new StringBody(timeString));
        }
        if (null != startDate) {
            String dateString = isoDate.format(startDate);
            reqEntity.addPart("start_date", new StringBody(dateString));
        }
        if (null != endDate) {
            String dateString = isoDate.format(endDate);
            reqEntity.addPart("end_date", new StringBody(dateString));
        }
        if (null != latitude) {
            reqEntity.addPart("latitude", new StringBody(latitude));
        }
        if (null != longitude) {
            reqEntity.addPart("longitude", new StringBody(longitude));
        }
        if (null != language) {
            reqEntity.addPart("language", new StringBody(language));
        }
        if (null != tags) {
            reqEntity.addPart("tags", new StringBody(tags));
        }
        reqEntity.addPart("token", new StringBody(this.token));
        reqEntity.addPart("key", new StringBody(this.key));
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(httpPost);
        int status = response.getStatusLine().getStatusCode();
        errorCheck(response, status);
        InputStream content = response.getEntity().getContent();
        GGPhotoInfo photo = JAXB.unmarshal(content, GGPhotoInfo.class);
        return photo;
    }
