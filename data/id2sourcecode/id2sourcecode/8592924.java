    @Override
    public void run() {
        try {
            URL url = new URL(this.uri);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("PUT");
            huc.connect();
            if (huc.getResponseCode() == 200) {
                System.out.println("Harvested: " + this.uri);
            } else if (huc.getResponseCode() > 200) {
                System.out.println("Not Harvested: " + this.uri + " error: " + huc.getResponseCode());
            }
            huc.disconnect();
        } catch (MalformedURLException e) {
        } catch (ProtocolException e) {
        } catch (IOException e) {
        }
    }
