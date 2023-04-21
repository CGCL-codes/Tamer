    private boolean checkForNewVersion() {
        boolean result = true;
        if ("0".equals(programVersion)) {
            result = false;
        }
        if (result) {
            urlstr = updateURL + versionFile;
            boolean foundNewVersion = false;
            BufferedReader buffReader;
            String currentLine;
            String remoteVersion;
            String localVersion;
            try {
                final URL url = new URL(urlstr);
                if (url != null) {
                    URLConnection con;
                    try {
                        con = url.openConnection();
                        con.setConnectTimeout(5000);
                        con.setReadTimeout(30000);
                        buffReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        currentLine = buffReader.readLine();
                        remoteVersion = currentLine.replaceFirst(pointPattern, dividerPattern).replaceAll(pointPattern, "").replaceFirst(dividerPattern, pointPattern);
                        localVersion = programVersion.replaceFirst(pointPattern, dividerPattern).replaceAll(pointPattern, "").replaceFirst(dividerPattern, pointPattern);
                        if (Double.valueOf(remoteVersion).compareTo(Double.valueOf(localVersion)) > 0) {
                            newVersion = currentLine;
                            foundNewVersion = true;
                        }
                        buffReader.close();
                    } catch (IOException e1) {
                        Logger.err(className + "Error while retrieving " + urlstr + " (possibly no connection to the internet)");
                    }
                }
            } catch (MalformedURLException e) {
                Logger.err(className + "URL invalid: " + urlstr);
            }
            result = foundNewVersion;
        }
        return result;
    }
