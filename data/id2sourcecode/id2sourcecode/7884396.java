    public static boolean shouldOverwriteIfPresent(XDocumentInfo xDocumentInfo) {
        try {
            String userFieldValue = xDocumentInfo.getUserFieldValue(SignItConstants.META_DATA_ARG0);
            if (!userFieldValue.isEmpty()) {
                int showConfirmDialog = JOptionPane.showConfirmDialog(new JFrame(), "The document has already been signed. Do you want to overwrite?", "Overwrite?", JOptionPane.OK_CANCEL_OPTION);
                if (showConfirmDialog == JOptionPane.CANCEL_OPTION) {
                    return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }
    }
