            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (getCCmdValue() == 0 && getChanStatValue() == 1) {
                    stopButton.setEnabled(false);
                    ;
                    return;
                }
                PFstopLabel.setText("");
                PFstopLabel.setVisible(false);
                PFstopLabel.validate();
                MsgPanel.validate();
                try {
                    ccmdWrapper.getChannel().putVal(2);
                } catch (ConnectionException e) {
                    System.err.println("Unable to connect to channel access.");
                } catch (PutException e) {
                    System.err.println("Unable to set process variables.");
                }
                try {
                    ccmdWrapper.getChannel().putVal(0);
                    if ((getCCmdLabel().equals("ON") && getChanStatValue() != 1) || getCCmdLabel().equals("?")) {
                        PFstopLabel.setHorizontalTextPosition(JLabel.CENTER);
                        PFstopLabel.setText(FailedStatus);
                    } else PFstopLabel.setText("Passed");
                    PFstopLabel.validate();
                    MsgPanel.validate();
                } catch (ConnectionException e) {
                    System.err.println("Unable to connect to channel access.");
                } catch (PutException e) {
                    System.err.println("Unable to set process variables.");
                }
                task.go(1);
                timer.start();
            }
