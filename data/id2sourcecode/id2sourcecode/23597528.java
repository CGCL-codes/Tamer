    public void saveTemplateResourcesToBeehive(Template template) {
        boolean share = template.getShareTo() == Template.PUBLIC;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost();
        String beehiveRootRestURL = configuration.getBeehiveRESTRootUrl();
        String url = "";
        if (!share) {
            url = beehiveRootRestURL + "account/" + userService.getAccount().getOid() + "/template/" + template.getOid() + "/resource/";
        } else {
            url = beehiveRootRestURL + "account/0/template/" + template.getOid() + "/resource/";
        }
        try {
            httpPost.setURI(new URI(url));
            File templateZip = getTemplateZipResource(template);
            if (templateZip == null) {
                LOGGER.warn("There are no template resources for template \"" + template.getName() + "\"to save to beehive!");
                return;
            }
            FileBody resource = new FileBody(templateZip);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("resource", resource);
            this.addAuthentication(httpPost);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (200 != response.getStatusLine().getStatusCode()) {
                throw new BeehiveNotAvailableException("Failed to save template to Beehive, status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (NullPointerException e) {
            LOGGER.warn("There are no template resources for template \"" + template.getName() + "\"to save to beehive!");
        } catch (IOException e) {
            throw new BeehiveNotAvailableException("Failed to save template to Beehive", e);
        } catch (URISyntaxException e) {
            throw new IllegalRestUrlException("Invalid Rest URL: " + url + " to save template resource to beehive! ", e);
        }
    }
