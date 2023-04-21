            public void run() {
                try {
                    URL url = new URL("http://code.google.com/p/tripleamapcreator/wiki/Program_Update_Information");
                    URLConnection urlC = url.openConnection();
                    InputStream is = urlC.getInputStream();
                    int oneChar;
                    StringBuilder builder = new StringBuilder();
                    while ((oneChar = is.read()) != -1) {
                        builder.appendCodePoint(oneChar);
                    }
                    is.close();
                    String updateInfoSection = builder.toString().split("-~=")[1].trim().replace("&#x27;", "'").replace("<p>", "").replace("</p>", "");
                    String[] lines = updateInfoSection.split("-eol!");
                    String reconstructedInfoSection = "";
                    for (String line : lines) {
                        if (reconstructedInfoSection.length() == 0) reconstructedInfoSection = "    " + line; else reconstructedInfoSection = reconstructedInfoSection + "\r\n" + "    " + line;
                    }
                    Properties updateProps = new Properties();
                    updateProps.load(new StringReader(reconstructedInfoSection));
                    Version latestVersion = new Version(updateProps.getProperty("LatestVersion"));
                    if (latestVersion.compareTo(Constants.ProgramVersion) > 0) {
                        String niceLookingVInfo = reconstructedInfoSection.replace("LatestVersion", "Latest Version").replace("LatestStableVersion", "Latest Stable Version");
                        String message = "An update to the map creator has been found. Here's the latest versioning information:\r\n\r\n" + niceLookingVInfo + "\r\n\r\nDo you want to go to the download page?";
                        int result = JOptionPane.showConfirmDialog(WalkthroughWindow.this, message, "Updates Available", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (result == JOptionPane.YES_OPTION) BrowserLauncher.openURL("http://code.google.com/p/tripleamapcreator/downloads/list");
                    } else JOptionPane.showMessageDialog(WalkthroughWindow.this, "Your version is up to date: " + Constants.ProgramVersion.toString(), "No Updates Available", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(WalkthroughWindow.this, "Unable to check for updates: " + ex.toString(), "Update Check Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
