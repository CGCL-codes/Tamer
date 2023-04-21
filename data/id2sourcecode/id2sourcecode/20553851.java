            public void actionPerformed(ActionEvent e) {
                if (setStrobeRadioX.isSelected()) {
                    System.out.println("Strobe PV Name: " + theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE).getId());
                    theDoc.scanStuff.scanVariableQuadX.setStrobeChannel(theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE), 1);
                } else {
                    theDoc.scanStuff.scanVariableQuadX.removeStrobeChannel();
                }
            }
