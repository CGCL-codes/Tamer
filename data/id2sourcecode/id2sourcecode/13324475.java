    public String getNewestVersion() {
        String version = Main.PROPERTIES.getProperty("project.version");
        int buildId = Integer.parseInt(Main.PROPERTIES.getProperty("buildId"));
        try {
            URL url = new URL("http://riad.de/jmemorize/vc/" + buildId);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String[] s = in.readLine().split(" ");
            int newestBuildID = Integer.parseInt(s[0]);
            String newestVersion = s[1];
            in.close();
            return newestBuildID > buildId ? newestVersion : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
