            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    if (tflimit.getValue() >= 0.2) myDoc.getSelector().cavTableModel.setValueAt(tflimit.getValue(), i, 13);
                    sclpv = ((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16);
                    update();
                }
            }
