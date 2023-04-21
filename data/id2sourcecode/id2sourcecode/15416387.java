        void updatePreviewImage() {
            if (previewNode != null) {
                if (previewNode.getChannel().chechkInputChannels()) {
                    if (zoom == 1.0f) {
                        ChannelUtils.computeImage(previewNode.getChannel(), previewNodeImage, null, 0);
                    } else if (zoom > 1.0f) {
                        ChannelUtils.computeImage(previewNode.getChannel(), previewNodeImage, null, 0, (int) (previewNodeImage.getWidth() * zoom), (int) (previewNodeImage.getHeight() * zoom), 0, 0);
                    } else if (zoom < 1.0f) {
                        int lx = (int) (previewNodeImage.getWidth() * zoom);
                        int ly = (int) (previewNodeImage.getHeight() * zoom);
                        if (tempImage == null || tempImage.getWidth() != lx || tempImage.getHeight() != ly) {
                            tempImage = new BufferedImage(lx, ly, BufferedImage.TYPE_INT_RGB);
                        }
                        ChannelUtils.computeImage(previewNode.getChannel(), tempImage, null, 0);
                        Graphics g = previewNodeImage.getGraphics();
                        for (int py = 0; py < previewNodeImage.getHeight(); py += ly) {
                            for (int px = 0; px < previewNodeImage.getWidth(); px += lx) {
                                g.drawImage(tempImage, px, py, null);
                            }
                        }
                    }
                } else {
                    Graphics g = previewNodeImage.getGraphics();
                    g.fillRect(0, 0, previewNodeImage.getWidth(), previewNodeImage.getHeight());
                }
            }
        }
