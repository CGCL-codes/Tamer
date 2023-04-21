    private boolean validateCentersNameAttr(final String centersNameAttr) {
        if (centersNameAttr == null || centersNameAttr.equals("")) {
            return false;
        }
        String[] names = Cytoscape.getNodeAttributes().getAttributeNames();
        boolean exist = false;
        for (String name : names) {
            if (name.equals(centersNameAttr)) {
                exist = true;
                break;
            }
        }
        if (exist) {
            int ret = Messenger.confirmWarning("Clustering centers name attribute already exist, overwrite?");
            if (ret == JOptionPane.OK_OPTION) {
                return true;
            } else {
                cancelDialog = true;
                return false;
            }
        }
        return true;
    }
