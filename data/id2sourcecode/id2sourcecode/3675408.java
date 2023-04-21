    public void run() {
        if (Validator.isNull(PropsValues.BROWSER_LAUNCHER_URL)) {
            return;
        }
        for (int i = 0; i < 300; i++) {
            try {
                Thread.sleep(Time.SECOND * 1);
            } catch (InterruptedException ie) {
            }
            try {
                URL url = new URL(PropsValues.BROWSER_LAUNCHER_URL);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                int responseCode = urlc.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try {
                        launchBrowser();
                    } catch (Exception e2) {
                    }
                    break;
                }
            } catch (Exception e1) {
            }
        }
    }
