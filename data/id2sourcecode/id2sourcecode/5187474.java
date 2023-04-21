    public void run() {
        if (System.currentTimeMillis() - scheduledExecutionTime() > 5000) {
            return;
        }
        try {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setConnectTimeout(1000);
            huc.setReadTimeout(1000);
            int code = huc.getResponseCode();
            if (updater != null) updater.isAlive(true);
        } catch (Exception e) {
            if (updater != null) updater.isAlive(false);
        }
    }
