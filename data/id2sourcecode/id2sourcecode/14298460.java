    private boolean validateNodeNameAttr(final String nodeNameAttr) {
        if (nodeNameAttr == null || nodeNameAttr.equals("")) {
            return false;
        }
        String[] names = Cytoscape.getNodeAttributes().getAttributeNames();
        boolean exist = false;
        for (String name : names) {
            if (name.equals(nodeNameAttr)) {
                exist = true;
                break;
            }
        }
        if (exist) {
            int ret = Messenger.confirmWarning("Clustering node name attribute already exist, overwrite?");
            if (ret == JOptionPane.OK_OPTION) {
                return true;
            } else {
                cancelDialog = true;
                return false;
            }
        }
        return true;
    }
