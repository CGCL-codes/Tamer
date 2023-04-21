    private void requestCpeConnection(String cpeurl) {
        try {
            URL url = new URL(cpeurl);
            URLConnection httpconn = url.openConnection();
            httpconn.setReadTimeout(5000);
            httpconn.getContent();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(cpeurl + " is malformed.");
        } catch (UnknownServiceException e) {
        } catch (IOException ex) {
            throw new RuntimeException(cpeurl + " problem." + ex.getMessage() + " " + ex.getClass().getName());
        }
    }
