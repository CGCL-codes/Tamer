        public void updatePreviewImage() {
            if ((node.getChannel() != null) && (node.getChannel().chechkInputChannels())) {
                if (previewImage == null) previewImage = ChannelUtils.createAndComputeImage(node.getChannel(), 64, 64, null, 0); else ChannelUtils.computeImage(node.getChannel(), previewImage, null, 0);
            } else {
                previewImage = null;
            }
            repaint();
        }
