    private boolean work() throws MalformedURLException, IOException {
        Thread.currentThread().setName("UpdateManager");
        URL url = new URL(ManagerOptions.MANAGER_CHECK_UPDATE_VERSIONS.trim());
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String str = in.readLine();
        in.close();
        if (str == null || str.trim().toLowerCase().contains("error") || Manager.getInstance().compareModsVersions(str.trim().split(" ")[1], "*-" + ManagerOptions.getInstance().getVersion().split(" ")[1])) {
            return false;
        }
        return true;
    }
