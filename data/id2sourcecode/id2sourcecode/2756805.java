    public void setCheckBoxesForConfig(List<ValueWatchConfig> enabledWatches) {
        for (Board currBoard : checkBoxesForBoard.keySet()) {
            Set<CheckBoxInfo> allCheckBoxInfos = checkBoxesForBoard.get(currBoard);
            for (CheckBoxInfo currBox : allCheckBoxInfos) {
                if (enabledWatches == null) {
                    currBox.getCheckBox().setSelected(true);
                    continue;
                }
                boolean enabled = false;
                for (ValueWatchConfig currWatchConfig : enabledWatches) {
                    if (currWatchConfig.getAddress() == currBoard.getAddress() && currWatchConfig.getCommChannelName() == currBoard.getCommChannel().getChannelName() && currWatchConfig.getSubchannel() == currBox.getSubchannel()) {
                        enabled = true;
                        break;
                    }
                }
                currBox.getCheckBox().setSelected(enabled);
            }
        }
    }
