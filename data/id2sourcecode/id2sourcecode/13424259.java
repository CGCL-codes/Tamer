    public static AbstractAction getFedoraAction(osid.dr.InfoRecord infoRecord, osid.dr.DigitalRepository dr) throws osid.dr.DigitalRepositoryException {
        final DR mDR = (DR) dr;
        final tufts.oki.dr.fedora.InfoRecord mInfoRecord = (tufts.oki.dr.fedora.InfoRecord) infoRecord;
        try {
            AbstractAction fedoraAction = new AbstractAction(infoRecord.getId().getIdString()) {

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
            };
            return fedoraAction;
        } catch (Exception ex) {
            throw new osid.dr.DigitalRepositoryException("FedoraUtils.getFedoraAction " + ex.getMessage());
        }
    }
