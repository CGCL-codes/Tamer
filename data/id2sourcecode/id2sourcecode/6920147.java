    public boolean checkURL(String urlSpec) {
        boolean valid = false;
        try {
            URL url = new URL(urlSpec);
            if (url.getProtocol().compareToIgnoreCase("ftp") == 0) {
                valid = checkFTP(urlSpec);
            } else {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("HEAD");
                con.connect();
                con.disconnect();
                valid = true;
            }
        } catch (Exception e) {
            valid = false;
            if (mVerbose) System.out.println(e.getMessage());
        }
        return valid;
    }
