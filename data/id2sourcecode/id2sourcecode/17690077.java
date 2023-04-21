        public void actionPerformed(ActionEvent e) {
            ColorModel cm = i5d.getChannelDisplayProperties(cControl.currentChannel).getColorModel();
            i5d.storeChannelProperties(cControl.currentChannel);
            if (e.getSource() == editLUTButton) {
                new LUT_Editor().run("");
                cm = i5d.getProcessor(cControl.currentChannel).getColorModel();
            } else if (e.getSource() == editColorButton) {
                Color c = new Color(i5d.getProcessor(cControl.currentChannel).getColorModel().getRGB(255));
                ColorChooser cc = new ColorChooser("Channel Color", c, false);
                c = cc.getColor();
                if (c != null) {
                    cm = ChannelDisplayProperties.createModelFromColor(c);
                }
            }
            i5d.getChannelDisplayProperties(cControl.currentChannel).setColorModel(cm);
            i5d.restoreChannelProperties(cControl.currentChannel);
            i5d.updateAndDraw();
            cControl.updateChannelSelector();
            cColorCanvas.repaint();
        }
