    public final boolean filterHasChannel(int index, String chan) {
        if (filtUseAllChannels[index]) {
            if (getChannels() == null || getChannels().length <= 0) {
                return false;
            } else {
                for (int i = 0; i < getChannels().length; i++) {
                    if (getChannels()[i].equalsIgnoreCase(chan)) return true;
                }
            }
        } else {
            if (filterChannels == null || filterChannels.length <= 0) {
                return false;
            } else {
                for (int i = 0; i < filterChannels[index].length; i++) {
                    if (filterChannels[index][i][0] != null && filterChannels[index][i][0].equalsIgnoreCase(chan)) return true;
                }
            }
        }
        return false;
    }
