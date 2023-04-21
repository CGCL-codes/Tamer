    public Version getLatestITDSMVersion() {
        BufferedReader in = null;
        String latestVersion = null;
        Version v = null;
        try {
            URL url = new URL(ONLINE_FILE_LOCATION);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(CURRENT_VERSION_COMMENT)) {
                    latestVersion = in.readLine();
                    v = parseVersion(latestVersion);
                } else if (inputLine.length() > 0) {
                    versionDetails += inputLine + "\n";
                }
            }
            log.log(Level.FINE, this.getClass().getName() + "\nVersion string parsed successfully");
        } catch (UnknownHostException e) {
            log.log(Level.SEVERE, this.getClass().getName(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("Unable to close BufferedReader object in Version class " + e);
                log.log(Level.SEVERE, this.getClass().getName(), e);
            }
            return v;
        }
    }
