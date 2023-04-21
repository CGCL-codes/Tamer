    public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
        File f = new File("codapack.conf");
        if (f.exists()) {
            LocalConfiguration.loadConfiguration();
            try {
                URL url = new URL(LocalConfiguration.HTTP_ROOT + "codapack-updater.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                serverData = new JSONObject(br.readLine());
                br.close();
                if (LocalConfiguration.updateNeeded(serverData.getString("codapack-version"))) new UpdatingProcess();
            } catch (IOException ex) {
                System.out.println("Problems trying to connect to IMA server");
            }
        }
    }
