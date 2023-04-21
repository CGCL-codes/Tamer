    private void opaqueCheckActionPerformed(java.awt.event.ActionEvent evt) {
        ChannelFrame.filterPanel.getChannelDisplay().setOpaque(opaqueCheck.isSelected());
        ChannelFrame.filterPanel.getOutputDisplayPanel().setOpaque(opaqueCheck.isSelected());
        ChannelFrame.filterPanel.getOutputPreviewPanel().setOpaque(opaqueCheck.isSelected());
        ChannelFrame.filterPanel.getChannelDisplay().refreshAlpha(false);
        ChannelFrame.filterPanel.getOutputDisplayPanel().refreshAlpha(true);
        ChannelFrame.filterPanel.getOutputPreviewPanel().refreshAlpha(true);
    }
