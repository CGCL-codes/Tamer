    private static boolean checkUpdate() {
        try {
            URL url = new URL(strURLofXML);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            BufferedReader httpIn = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String str;
            while ((str = httpIn.readLine()) != null) {
                Pattern pat = Pattern.compile("<Version>.*</Version>");
                Matcher matcher = pat.matcher(str);
                if (matcher.find()) {
                    String tempStr = matcher.group();
                    tempStr = tempStr.replace("<Version>", "");
                    tempStr = tempStr.replace("</Version>", "");
                    strLatestVersion = tempStr;
                    log.debug("Latest Version:" + strLatestVersion);
                }
                pat = Pattern.compile("<Date>.*</Date>");
                matcher = pat.matcher(str);
                if (matcher.find()) {
                    String tempStr = matcher.group();
                    tempStr = tempStr.replace("<Date>", "");
                    tempStr = tempStr.replace("</Date>", "");
                    strReleaseDate = tempStr;
                    log.debug("Release Date:" + strReleaseDate);
                }
                pat = Pattern.compile("<DownloadURL>.*</DownloadURL>");
                matcher = pat.matcher(str);
                if (matcher.find()) {
                    String tempStr = matcher.group();
                    tempStr = tempStr.replace("<DownloadURL>", "");
                    tempStr = tempStr.replace("</DownloadURL>", "");
                    strDownloadURL = tempStr;
                    log.debug("Download URL:" + strDownloadURL);
                }
                pat = Pattern.compile("<WindowsInstallerURL>.*</WindowsInstallerURL>");
                matcher = pat.matcher(str);
                if (matcher.find()) {
                    String tempStr = matcher.group();
                    tempStr = tempStr.replace("<WindowsInstallerURL>", "");
                    tempStr = tempStr.replace("</WindowsInstallerURL>", "");
                    strWindowsInstallerURL = tempStr;
                    log.debug("Windows Installer URL:" + strWindowsInstallerURL);
                }
            }
            httpIn.close();
            httpCon.disconnect();
        } catch (Exception e) {
            log.error("Failed to get latest version data", e);
            return false;
        }
        return true;
    }
