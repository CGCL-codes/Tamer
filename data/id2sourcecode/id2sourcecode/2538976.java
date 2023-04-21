    private void updateWebsiteStatus(int numUsers, int numFiles) {
        BufferedReader input = null;
        try {
            URL url = new URL(WebServicesImpl.BASE_WEBSITE_URL + "/admin/status.php?pw=kujukudu&users=" + numUsers + "&files=" + numFiles);
            input = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            LOG.severe(e.getMessage());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
    }
