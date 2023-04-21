    @Override
    protected List<ImageInfo> doInBackground() {
        String searchURL = String.format("%s&api_key=%s" + "&per_page=%s" + "&text=%s", SEARCH_URL, key, MAX_IMAGES, search);
        Object strResults = null;
        InputStream is = null;
        URL url = null;
        List<ImageInfo> infoList = null;
        try {
            url = new URL(searchURL);
            is = url.openStream();
            infoList = parseImageInfo(is);
            retrieveAndProcessThumbnails(infoList);
        } catch (MalformedURLException mfe) {
            JOptionPane.showMessageDialog(null, "No Working Internet Connection/Or low bandwidth", "Error while Connecting", JOptionPane.ERROR_MESSAGE);
            mfe.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Working Internet Connection/Or low bandwidth", "Error while Connecting", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "No Working Internet Connection/Or low bandwidth", "Error while Connecting", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return infoList;
    }
