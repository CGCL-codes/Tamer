    @Override
    public boolean deleteTemplate(long templateOid) {
        log.debug("Delete Template id: " + templateOid);
        String deleteRestUrl = configuration.getBeehiveRESTRootUrl() + "account/" + userService.getAccount().getOid() + "/template/" + templateOid;
        HttpDelete httpDelete = new HttpDelete();
        addAuthentication(httpDelete);
        try {
            httpDelete.setURI(new URI(deleteRestUrl));
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpDelete);
            if (200 == response.getStatusLine().getStatusCode()) {
                return true;
            } else {
                throw new BeehiveNotAvailableException("Failed to delete template");
            }
        } catch (Exception e) {
            throw new BeehiveNotAvailableException("Failed to delete template: " + e.getMessage(), e);
        }
    }
