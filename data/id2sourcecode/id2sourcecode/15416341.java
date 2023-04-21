    private void askFileAndExportTexture(int resX, int resY) {
        TextureEditor.INSTANCE.m_TextureFileChooser_SaveLoadImage.setDialogTitle("Export Texture to " + resX + "x" + resY + " Image...");
        if (TextureEditor.INSTANCE.m_TextureFileChooser_SaveLoadImage.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String name = TextureEditor.INSTANCE.m_TextureFileChooser_SaveLoadImage.getSelectedFile().getAbsolutePath();
            if (!name.endsWith(".png")) name += ".png";
            boolean useCache = ChannelUtils.useCache;
            try {
                ChannelUtils.useCache = false;
                ImageIO.write(ChannelUtils.createAndComputeImage(graph.selectedNodes.lastElement().getChannel(), resX, resY, TextureEditor.INSTANCE.m_ProgressDialog, 3), "png", new File(name));
                Logger.log(this, "Saved image to " + name + ".");
            } catch (IOException exc) {
                exc.printStackTrace();
                Logger.logError(this, "IO Exception while exporting image: " + exc);
            }
            ChannelUtils.useCache = useCache;
        }
    }
