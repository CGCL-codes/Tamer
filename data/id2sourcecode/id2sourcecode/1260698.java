    private boolean sendDoneMsg(String userId) {
        BufferedReader input = null;
        try {
            URL url = new URL(WebServicesImpl.BASE_WEBSITE_URL + "/admin/regtask.php?pw=kujukudu&action=done&userid=" + userId);
            input = new BufferedReader(new InputStreamReader(url.openStream()));
            String result = input.readLine();
            if (result != null && result.indexOf("OK") != -1) return true;
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
        return false;
    }
