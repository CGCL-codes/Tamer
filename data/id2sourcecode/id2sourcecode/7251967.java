                public void actionPerformed(java.awt.event.ActionEvent e) {
                    page.setDelayAmount(getDelayTF().getText());
                    page.setCCOffset(getCcOffsetTF().getText());
                    page.setMidiChannel(getChannelTF().getText());
                    page.setHorizontal(getHorizontalCB().isSelected());
                    page.redrawDevice();
                }
