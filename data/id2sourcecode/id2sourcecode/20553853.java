            public void actionPerformed(ActionEvent e) {
                if (setStrobeRadioY.isSelected()) {
                    System.out.println("Strobe PV Name: " + theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE).getId());
                    theDoc.scanStuff.scanVariableQuadY.setStrobeChannel(theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE), 1);
                } else {
                    theDoc.scanStuff.scanVariableQuadY.removeStrobeChannel();
                }
            }
