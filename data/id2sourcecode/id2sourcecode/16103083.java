    private void install() {
        class writeRadio extends Thread {

            public void run() {
                ATRadioFile rf = new ATRadioFile();
                setRadioURL();
                rf.setRadioURL(radioURL);
                rf.setUsername(Configuration.getProperty(Configuration.KEY_NET_USERNAME));
                try {
                    rf.setPassword(JTP.decrypt(Configuration.getProperty(Configuration.KEY_NET_PASSWORD)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                rf.setHost(Configuration.getProperty(Configuration.KEY_RADIO_HOST));
                rf.setTbUsername(Configuration.getProperty(Configuration.KEY_RADIO_USERNAME));
                if (rf.getHost() == null || rf.getPassword() == null || rf.getUsername() == null) {
                    JPop j = new JPop("Not enough Information", "Network Resrouces Incomplete\r\nPlease Check the Radio section in Preferences");
                    return;
                }
                if (rf.getHost().isEmpty() || rf.getPassword().isEmpty() || rf.getUsername().isEmpty()) {
                    JPop j = new JPop("Not enough Information", "Network Resrouces Incomplete\r\nPlease Check the Radio section in Preferences");
                    return;
                }
                boolean v = true;
                int rows = radioTable.getRowCount();
                for (int i = 0; i < rows - 1; i++) {
                    if (showExtra) {
                        System.out.print(i + "...");
                    }
                    StationEntry se = new StationEntry(radioTable.getValueAt(i, 3).toString(), radioTable.getValueAt(i, 1).toString(), radioTable.getValueAt(i, 2).toString(), radioTable.getValueAt(i, 4).toString(), v);
                    se.Validate();
                    if (showExtra) {
                        System.out.println(se.toString());
                    }
                    rf.addStation(se);
                }
                rptText.setForeground(JTP.parseRGB("0,0,0"));
                rptText.setText("Installing New Radio.txt file...");
                try {
                    rf.writeRadioFile();
                } catch (Exception e) {
                    JPop j = new JPop("Rats...", "URL: " + radioURL);
                    rptText.setForeground(Color.RED);
                    rptText.setText("Installation Failed...");
                    return;
                }
                rptText.setForeground(new Color(31, 156, 67));
                rptText.setText("New Stations Installed");
            }
        }
        writeRadio r = new writeRadio();
        r.start();
        rptText.setForeground(JTP.parseRGB("0,0,0"));
        rptText.setText("Generating new Radio.txt...");
    }
