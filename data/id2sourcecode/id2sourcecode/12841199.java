    public void assembly(VideoStack vdStack) {
        Dimension dimScreen = vdStack.getRootComponent().getSize();
        ChannelLabel chLabel = this.chSelector.getChannelLabel();
        Component chLabelComp = chLabel.getComponent();
        vdStack.getTVControlContainer().add(chLabelComp);
        Dimension chLabelDim = chLabelComp.getMinimumSize();
        int x = dimScreen.width - chLabelDim.width - 25;
        int y = dimScreen.height - chLabelDim.height - 10;
        chLabelComp.setBounds(x, y, chLabelDim.width, chLabelDim.height);
        chLabelComp.setVisible(false);
    }
