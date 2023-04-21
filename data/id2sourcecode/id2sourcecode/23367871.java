    public void start() {
        super.start();
        clipBoardLayer = new ALayer();
        ALayer originalLayer = getFocussedClip().getSelectedLayer();
        for (int i = 0; i < originalLayer.getNumberOfChannels(); i++) {
            AChannelSelection chs = originalLayer.getChannel(i).getSelection();
            if (chs.isSelected()) {
                clipBoardLayer.add(new AChannel(chs));
            }
        }
    }
