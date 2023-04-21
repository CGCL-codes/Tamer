        public void channelChanged() {
            cColorCanvas.repaint();
            displayGrayBox.setState(i5d.getChannelDisplayProperties(cControl.currentChannel).isDisplayedGray());
            cColorCanvas.setEnabled(!displayGrayBox.getState());
            editColorButton.setEnabled(!displayGrayBox.getState());
            editLUTButton.setEnabled(!displayGrayBox.getState());
        }
