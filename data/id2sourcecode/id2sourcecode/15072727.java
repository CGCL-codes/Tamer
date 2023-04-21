    private String existsUpdate(URL url) {
        try {
            ZReader bin = ZReader.newTextReader(url.openStream());
            if (bin != null) {
                String onlineVersion = bin.lineExc(false);
                String dateString = bin.lineExc();
                bin.close();
                if (SWGConstants.version.compareTo(onlineVersion) != 0) return (onlineVersion + "\n" + dateString);
            }
        } catch (Exception e) {
            SWGAide.printDebug("post", 1, "SWGPostLaunch:existsUpdate: " + e);
        }
        return null;
    }
