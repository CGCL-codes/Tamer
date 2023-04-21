            public void actionPerformed(ActionEvent e) {
                Channel channel = ChannelFactory.defaultFactory().getChannel(prop.getChFilVoltSTB());
                channel.connectAndWait();
                double newVal = prop.getValFilVoltSTB();
                try {
                    if (prop.getCaputFlag()) {
                        channel.putVal(newVal);
                    } else {
                        System.out.println("test mode, no caput");
                    }
                } catch (ConnectionException e1) {
                    e1.printStackTrace();
                } catch (PutException e1) {
                    e1.printStackTrace();
                }
            }
