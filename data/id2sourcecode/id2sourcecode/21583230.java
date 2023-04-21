    public int getLatestVersion() {
        int version = -1;
        BufferedReader input = null;
        try {
            URL url = new URL(FlyShareApp.BASE_WEBSITE_URL + "/clientversion.txt");
            input = new BufferedReader(new InputStreamReader(url.openStream()));
            String rec = input.readLine();
            try {
                version = Integer.parseInt(rec);
            } catch (NumberFormatException e) {
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
        return version;
    }
