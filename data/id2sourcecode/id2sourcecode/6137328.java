    private String getIp() {
        try {
            URL url = new URL("http://whatismyip.org");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = br.readLine();
            br.close();
            return ((ip != null) && (ip.length() < 20)) ? ip : null;
        } catch (Exception e) {
            return null;
        }
    }
