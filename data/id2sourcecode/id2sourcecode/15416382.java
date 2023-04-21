        public PreviewWindow(TextureGraphNode node) {
            previewNode = node;
            node.getChannel().addChannelChangeListener(this);
            previewNodeImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
            updatePreviewImage();
            miniButtons.add(new MiniButton(256 - 16, 0, 16, 16, "X"));
            miniButtons.add(new MiniButton(0, 0, 16, 16, "+"));
            miniButtons.add(new MiniButton(16, 0, 16, 16, "-"));
        }
