    private static String getChannelRegistrationName(JChannel c, String domain, String clusterName) {
        return domain + ":type=channel,cluster=" + clusterName;
    }
