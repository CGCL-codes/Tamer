        public void setColor(Color c) {
            i5d.storeChannelProperties(cControl.currentChannel);
            i5d.getChannelDisplayProperties(cControl.currentChannel).setColorModel(ChannelDisplayProperties.createModelFromColor(c));
            i5d.restoreChannelProperties(cControl.currentChannel);
            i5d.updateAndDraw();
            cControl.updateChannelSelector();
            cColorCanvas.repaint();
        }
