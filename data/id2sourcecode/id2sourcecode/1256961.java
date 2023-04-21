    @Override
    public void channelChanged(Channel source) {
        if (node == null) {
            previewImage = null;
        } else if (node.getChannel().chechkInputChannels()) {
            if (previewImage == null) previewImage = ChannelUtils.createAndComputeImage(node.getChannel(), previewImageSize, previewImageSize, null, previewImageMode); else ChannelUtils.computeImage(node.getChannel(), previewImage, null, previewImageMode);
            benchmarkLabel.setText("Benchmark: " + ChannelUtils.lastComputationTime);
        } else {
            previewImage = null;
        }
        repaint();
    }
