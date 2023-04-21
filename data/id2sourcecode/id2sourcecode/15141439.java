    public void run() {
        try {
            HttpURLConnection httpurlconnection = (HttpURLConnection) (new URL((new StringBuilder()).append("https://login.minecraft.net/session?name=").append(mc.session.username).append("&session=").append(mc.session.sessionId).toString())).openConnection();
            httpurlconnection.connect();
            if (httpurlconnection.getResponseCode() == 400 && this == null) {
                Minecraft.hasPaidCheckTime = System.currentTimeMillis();
            }
            httpurlconnection.disconnect();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
