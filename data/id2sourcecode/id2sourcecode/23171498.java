    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {
        FrameOrganizer.closeRoomCreationFrame();
        TabOrganizer.getChannelPanel(channel).enableButtons();
    }
