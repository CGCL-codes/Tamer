    public void choiceMade(String choice) {
        if (choice.equals("shutter")) {
            p0 = shutter0;
            p1 = shutter1;
            p2 = shutter2;
        } else {
            p0 = test0;
            p1 = test1;
            p2 = test2;
        }
        ChannelFrame.filterPanel.getChannelDisplay().setPatternType(p0);
        ChannelFrame.filterPanel.getOutputPreviewPanel().setPatternType(p1);
        ChannelFrame.filterPanel.getOutputDisplayPanel().setPatternType(p2);
    }
