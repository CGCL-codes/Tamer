            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String chanName = pvNameText.getText();
                Channel chan = ChannelFactory.defaultFactory().getChannel(chanName);
                chan.connectAndWait();
                try {
                    double[] valArr = chan.getArrDbl();
                    PVTableCell cell = getNewCell(chan);
                    PVs.add(cell);
                    updatePVsTable();
                } catch (Exception excp) {
                    pvNameText.setText(null);
                    pvNameText.setText("Bad Channel Name. Try again!");
                }
            }
