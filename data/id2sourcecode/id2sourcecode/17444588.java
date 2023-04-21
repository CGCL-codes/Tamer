            public void actionPerformed(ActionEvent e) {
                String bcmchoice = new String("RTBT_Diag:BCM25");
                DecimalFormat decfor = new DecimalFormat("0.###E0");
                double charge = 0.0;
                Channel bcmChargeCh = ChannelFactory.defaultFactory().getChannel(bcmchoice + ":Q");
                try {
                    charge = Math.abs(bcmChargeCh.getValDbl());
                } catch (ConnectionException ce) {
                    System.out.println("Cannot connect to " + bcmChargeCh.getId());
                } catch (GetException ce) {
                    System.out.println("Cannot get value from " + bcmChargeCh.getId());
                }
                Channel.flushIO();
                ppp = charge / 1.602e-19;
                Np.setText((String) decfor.format(ppp));
                System.out.println("Charge is " + charge + " " + ppp);
                doc.charge = ppp;
            }
