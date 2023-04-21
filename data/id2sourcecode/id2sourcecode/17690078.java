        public void itemStateChanged(ItemEvent e) {
            if (e.getItemSelectable() == displayGrayBox) {
                i5d.storeChannelProperties(cControl.currentChannel);
                i5d.getChannelDisplayProperties(cControl.currentChannel).setDisplayedGray(displayGrayBox.getState());
                i5d.restoreChannelProperties(cControl.currentChannel);
                i5d.updateImageAndDraw();
                cColorCanvas.setEnabled(!displayGrayBox.getState());
                editColorButton.setEnabled(!displayGrayBox.getState());
                editLUTButton.setEnabled(!displayGrayBox.getState());
            }
        }
