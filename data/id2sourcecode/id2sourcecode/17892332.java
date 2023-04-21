    @Override
    public void approveSelection() {
        File f = super.getSelectedFile();
        if (f.exists()) {
            int ans = JOptionPane.showConfirmDialog(null, "" + f.getName() + " already exists. Overwrite?", "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ans == JOptionPane.OK_OPTION) {
                try {
                    if (getFileFilter().getDescription().equals("SPARUL")) {
                        OREManager.getInstance().saveChangesAsSPARUL(f);
                    } else {
                        OREManager.getInstance().saveOntology(f);
                    }
                    super.approveSelection();
                } catch (OWLOntologyStorageException e) {
                    JOptionPane.showMessageDialog(this, "Could not save file: " + e.getCause(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            try {
                if (getFileFilter().getDescription().equals("SPARUL")) {
                    OREManager.getInstance().saveChangesAsSPARUL(f);
                } else {
                    OREManager.getInstance().saveOntology(f);
                }
                super.approveSelection();
            } catch (OWLOntologyStorageException e) {
                JOptionPane.showMessageDialog(this, "Could not save file: " + e.getCause(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
