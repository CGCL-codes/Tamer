            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (getCCmdValue() == 1 && getChanStatValue() == 0) return;
                PFstopLabel.setText("");
                PFstopLabel.setVisible(true);
                PFstopLabel.validate();
                MsgPanel.validate();
                try {
                    ccmdWrapper.getChannel().putVal(1);
                } catch (ConnectionException e) {
                    System.err.println("Unable to connect to channel access.");
                } catch (PutException e) {
                    System.err.println("Unable to set process variables.");
                }
                task.go(0);
                timer.start();
            }
