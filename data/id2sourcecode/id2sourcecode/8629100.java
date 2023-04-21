    protected boolean saveDocument(Document document, boolean overwrite) {
        while (true) {
            boolean write = (document.getURI() != null);
            if (!overwrite && document.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "Overwrite file?", "File Already Exists", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.CANCEL_OPTION) break;
                write = (result == JOptionPane.YES_OPTION);
            }
            if (write) {
                boolean success = document.save();
                if (success) updateGUI(); else JOptionPane.showMessageDialog(this, "Failed to save Document", "Error", JOptionPane.ERROR_MESSAGE);
                return success;
            } else {
                int result = DefaultFileKit.getFileChooser().showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) setDocumentFile(document, DefaultFileKit.getFileChooser().getSelectedFile());
                if (result == JFileChooser.CANCEL_OPTION) break;
            }
        }
        return false;
    }
