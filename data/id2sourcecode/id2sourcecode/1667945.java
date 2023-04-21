    private boolean hasWildcardListener(String channelName) {
        boolean returnValue = false;
        String wildcardString = "/";
        StringTokenizer tokenizer = new StringTokenizer(channelName, "/");
        while (tokenizer.hasMoreTokens() && !returnValue) {
            wildcardString = wildcardString + tokenizer.nextToken() + "/";
            returnValue = CometClientServlet.getBayeux().getChannel(wildcardString + "**") != null;
        }
        return returnValue;
    }
