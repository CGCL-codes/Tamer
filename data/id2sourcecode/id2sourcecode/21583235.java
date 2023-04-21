    public String getServiceMap() throws ServiceMapException {
        BufferedReader input = null;
        String serviceListing = null;
        try {
            URL url = new URL(FlyShareApp.BASE_WEBSITE_URL + "/servicemap.txt");
            input = new BufferedReader(new InputStreamReader(url.openStream()));
            serviceListing = StringUtils.trimToEmpty(input.readLine());
            if (serviceListing.length() == 0) throw new ServiceMapException();
            LOG.info("Using service " + serviceListing);
        } catch (MalformedURLException e) {
            throw new ServiceMapException();
        } catch (IOException e) {
            throw new ServiceMapException();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
        return serviceListing;
    }
