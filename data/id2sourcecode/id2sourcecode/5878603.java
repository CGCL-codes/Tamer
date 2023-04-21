    public void refreshAlpha(boolean displayOnly) {
        if (alpha >= 0.99) {
            alpha = 1.0f;
            if (ChannelFrame.channelGridPanel.getOutPutChannel().getChannelType().equals("clip")) {
                this.setOpaque(true);
                System.out.println("Opaque set to true");
            }
        }
        if (alpha < 0.99 && ChannelFrame.channelGridPanel.getOutPutChannel().getChannelType().equals("clip")) this.setOpaque(false);
        if (alpha > 0) setVisible(true);
        if (alpha <= 0 && !this.isOpaque()) {
            alpha = 0.0f;
            if (displayOnly) setVisible(false);
        }
        repaint();
    }
