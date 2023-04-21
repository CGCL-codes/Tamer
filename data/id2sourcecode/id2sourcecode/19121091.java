    private void doOperation() {
        Parameters params = new Parameters();
        params.setMerge(operationsPanel.isMerge());
        params.setDiff(operationsPanel.isDiff());
        params.setStdout(false);
        params.setXslt(false);
        File tempFile;
        tempFile = fileSelectorPanel1.getSelectedFile();
        if (tempFile.canRead() && tempFile.isFile()) {
            params.setOriginalPath(fileSelectorPanel1.getSelectedStringFile());
        } else {
            JOptionPane.showMessageDialog((this), "File: \"" + fileSelectorPanel1.getSelectedStringFile() + "\" not exists or is not readable", "JNDiff Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tempFile = fileSelectorPanel2.getSelectedFile();
        if (tempFile.canRead() && tempFile.isFile()) {
            if (operationsPanel.isDiff()) {
                params.setModifiedPath(fileSelectorPanel2.getSelectedStringFile());
            } else {
                params.setDeltaPath(fileSelectorPanel2.getSelectedStringFile());
            }
        } else {
            JOptionPane.showMessageDialog((this), "File: \"" + fileSelectorPanel2.getSelectedStringFile() + "\" not exists or is not readable", "JNDiff Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tempFile = fileSelectorPanel3.getSelectedFile();
        if (tempFile.getParentFile().isDirectory() && tempFile.getParentFile().canWrite()) {
            if (tempFile.isFile() && tempFile.canWrite()) {
                int overWriteDecision = JOptionPane.showConfirmDialog(this, "File \"" + tempFile.getAbsolutePath() + "\"\n" + "Already exists.\n" + "Do you overwrite?", "JNDiff warning:", JOptionPane.YES_NO_OPTION);
                switch(overWriteDecision) {
                    case 0:
                        break;
                    case 1:
                    default:
                        return;
                }
            }
            if (operationsPanel.isMerge()) {
                params.setMarkupPath(fileSelectorPanel3.getSelectedStringFile());
            } else {
                params.setDeltaPath(fileSelectorPanel3.getSelectedStringFile());
            }
        }
        try {
            OperationsHandler.doOperation(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
