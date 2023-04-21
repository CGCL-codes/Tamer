    private static void sync(boolean start) {
        try {
            URL url = new URL(GlobalConfiguration.Paths.URLs.STATS);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.setDoOutput(true);
            connect.setDoInput(true);
            connect.setUseCaches(false);
            connect.setAllowUserInteraction(false);
            StringBuilder write = new StringBuilder("s=");
            if (start) {
                write.append("1");
                startTime = System.currentTimeMillis();
            } else {
                write.append("0&t=").append((System.currentTimeMillis() - startTime) / 60000);
            }
            Writer writer = new OutputStreamWriter(connect.getOutputStream(), "UTF-8");
            writer.write(write.toString());
            writer.flush();
            writer.close();
            connect.disconnect();
        } catch (IOException ignored) {
        }
    }
