    public Set<String> getChannelSet() {
        StringTokenizer tokens = new StringTokenizer(channels);
        Set<String> set = new HashSet<String>();
        while (tokens.hasMoreTokens()) {
            set.add(tokens.nextToken());
        }
        return set;
    }
