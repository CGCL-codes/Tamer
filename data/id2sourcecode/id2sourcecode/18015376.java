    public Channel[] getChannelsForUser(String user) {
        Vector v = (Vector) users.get(user);
        if (v != null) {
            int numChannels = v.size();
            if (numChannels > 0) {
                Channel[] retArray = new Channel[numChannels];
                for (int x = 0; x < numChannels; x++) {
                    retArray[x] = getChannel((String) v.elementAt(x));
                }
                return retArray;
            }
        }
        return null;
    }
