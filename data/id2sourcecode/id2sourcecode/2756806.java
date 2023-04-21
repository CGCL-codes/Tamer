    public ArrayList<ValueWatchConfig> getEnabledWatches() {
        ArrayList<ValueWatchConfig> enabledWatches = new ArrayList<ValueWatchConfig>();
        for (Board currBoard : checkBoxesForBoard.keySet()) {
            Set<CheckBoxInfo> allCheckBoxInfos = checkBoxesForBoard.get(currBoard);
            for (CheckBoxInfo currBox : allCheckBoxInfos) {
                if (currBox.getCheckBox().isSelected()) {
                    ValueWatchConfig newConfig = new ValueWatchConfig();
                    newConfig.setAddress(currBoard.getAddress());
                    newConfig.setCommChannelName(currBoard.getCommChannel().getChannelName());
                    newConfig.setSubchannel(currBox.getSubchannel());
                    enabledWatches.add(newConfig);
                }
            }
        }
        return enabledWatches;
    }
