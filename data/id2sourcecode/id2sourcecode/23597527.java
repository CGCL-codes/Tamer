    @SuppressWarnings("all")
    public void saveResourcesToBeehive(Collection<Panel> panels) {
        PathConfig pathConfig = PathConfig.getInstance(configuration);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost();
        String beehiveRootRestURL = configuration.getBeehiveRESTRootUrl();
        this.addAuthentication(httpPost);
        String url = beehiveRootRestURL + "account/" + userService.getAccount().getOid() + "/openremote.zip";
        try {
            httpPost.setURI(new URI(url));
            FileBody resource = new FileBody(getExportResource(panels));
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("resource", resource);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (200 != response.getStatusLine().getStatusCode()) {
                throw new BeehiveNotAvailableException("Failed to save resource to Beehive, status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (NullPointerException e) {
            LOGGER.warn("There no resource to upload to beehive at this time. ");
        } catch (IOException e) {
            throw new BeehiveNotAvailableException(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new IllegalRestUrlException("Invalid Rest URL: " + url + " to save modeler resource to beehive! ", e);
        }
    }
