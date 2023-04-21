    private void runCreateVendorProfile() {
        DataStorage.clearVendorProfile();
        GenericUrl url = new GoogleUrl(EnterpriseMarketplaceUrl.generateVendorProfileUrl());
        VendorProfile body = buildVendorProfile();
        JsonHttpContent content = new JsonHttpContent();
        content.jsonFactory = jsonFactory;
        if (body != null) {
            content.data = body;
        }
        VendorProfile vendorProfile = null;
        try {
            HttpRequest request = requestFactory.buildPostRequest(url, content);
            request.addParser(jsonHttpParser);
            request.readTimeout = readTimeout;
            HttpResponse response = request.execute();
            vendorProfile = response.parseAs(VendorProfile.class);
            if (vendorProfile != null) {
                DataStorage.setVendorProfile(vendorProfile);
                operationStatus = true;
            }
            response.getContent().close();
        } catch (IOException e) {
            AppsMarketplacePluginLog.logError(e);
        }
    }
