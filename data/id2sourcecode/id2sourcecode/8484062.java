    public boolean checkForNewVersion(boolean notifyNoUpdate) {
        URL u = null;
        try {
            u = new URL("http://robocode.sourceforge.net/version/version.html");
        } catch (MalformedURLException e) {
            Utils.log("Unable to check for new version: " + e);
            if (notifyNoUpdate) {
                Utils.messageError("Unable to check for new version: " + e);
            }
            return false;
        }
        BufferedReader reader;
        try {
            URLConnection urlConnection = u.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                Utils.log("Update checking with http.");
                HttpURLConnection h = (HttpURLConnection) urlConnection;
                if (h.usingProxy()) {
                    Utils.log("http using proxy.");
                }
            }
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        } catch (IOException e) {
            Utils.log("Unable to check for new version: " + e);
            if (notifyNoUpdate) {
                Utils.messageError("Unable to check for new version: " + e);
            }
            return false;
        }
        String v = null;
        try {
            v = reader.readLine();
        } catch (IOException e) {
            Utils.log("Unable to check for new version: " + e);
            if (notifyNoUpdate) {
                Utils.messageError("Unable to check for new version: " + e);
            }
            return false;
        }
        String installurl = "http://robocode.sourceforge.net/installer";
        if (v.compareToIgnoreCase(getVersion()) > 0) {
            if (JOptionPane.showConfirmDialog(manager.getWindowManager().getRobocodeFrame(), "Version " + v + " of Robocode is now available.  Would you like to download it?", "Version " + v + " available", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    BrowserManager.openURL(installurl);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(manager.getWindowManager().getRobocodeFrame(), e.getMessage(), "Unable to open browser!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (!v.matches(".*([Aa][Ll][Ff]|[Bb][Ee][Tt])[Aa].*")) {
                JOptionPane.showMessageDialog(manager.getWindowManager().getRobocodeFrame(), "It is highly recommended that you always download the latest version.  You may get it at " + installurl, "Update when you can!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (notifyNoUpdate) {
            JOptionPane.showMessageDialog(manager.getWindowManager().getRobocodeFrame(), "You have version " + version + ".  This is the latest version of Robocode.", "No update available", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }
