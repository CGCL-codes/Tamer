    public List getSpecialChannelSequence(String channelStr) {
        List ls = getChannelSequence(channelStr);
        List result = new ArrayList();
        String shortcut = null;
        String recentUpdate = null;
        for (int i = 0; i < ls.size(); i++) {
            String str = (String) ls.get(i);
            if (str.indexOf(PersonalHomePreference.TOOLS) >= 0 && i < (ls.size() - 2)) {
                shortcut = (String) ls.get(i);
            } else if (str.indexOf(PersonalHomePreference.RECENTLY_ACCESSED_DOC) >= 0 && i < (ls.size() - 1)) {
                recentUpdate = (String) ls.get(i);
            } else {
                result.add(str);
            }
        }
        if (!Utility.isEmpty(shortcut)) {
            result.add(shortcut);
        }
        if (!Utility.isEmpty(recentUpdate)) {
            result.add(recentUpdate);
        }
        return result;
    }
