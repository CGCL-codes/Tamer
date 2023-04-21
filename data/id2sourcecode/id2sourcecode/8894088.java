    public static void main(String[] args) {
        List<Map<String, String>> sites = getSites("");
        insertSitesIntoWebsiteschema(sites);
        List<Map<String, String>> channels = getChannels("");
        insertChannelsIntoWebsiteschema(channels);
    }
