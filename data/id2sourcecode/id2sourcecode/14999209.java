    public BufferedReader postServerCommand(String serverURL) {
        try {
            BufferedReader dI;
            URL url = new URL(serverURL);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            dI = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return dI;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
