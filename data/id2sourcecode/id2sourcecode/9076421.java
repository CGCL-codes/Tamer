                public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                    try {
                        String fedoraUrl = mInfoRecord.getInfoField(new PID(getFedoraProperty(mDR, "DisseminationURLInfoPartId"))).getValue().toString();
                        URL url = new URL(fedoraUrl);
                        URLConnection connection = url.openConnection();
                        System.out.println("FEDORA ACTION: Content-type:" + connection.getContentType() + " for url :" + fedoraUrl);
                        tufts.Util.openURL(fedoraUrl);
                    } catch (Exception ex) {
                    }
                }
