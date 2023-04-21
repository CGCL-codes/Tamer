                public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                    try {
                        String fedoraUrl = mInfoRecord.getInfoField(new PID(FedoraUtils.getFedoraProperty(mDR, "DisseminationURLInfoPartId"))).getValue().toString();
                        URL url = new URL(fedoraUrl);
                        URLConnection connection = url.openConnection();
                        System.out.println("FEDORA ACTION: Content-type:" + connection.getContentType() + " for url :" + fedoraUrl);
                        VueUtil.openURL(fedoraUrl);
                    } catch (Exception ex) {
                    }
                }
