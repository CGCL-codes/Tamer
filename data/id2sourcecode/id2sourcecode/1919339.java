    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("save")) {
            openFileChooser();
            if (outfile != null) {
                if (outfile.exists()) {
                    n = JOptionPane.showConfirmDialog(null, "The file " + outfile.toString() + " already exists.\nDo you want to overwrite this file?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                } else {
                    outfile = new File(outfile.toString());
                }
                if (n == JOptionPane.YES_OPTION) {
                    outfile = new File(outfile.toString());
                    JSimLogic.getInstance().writeParameterFile(this, outfile);
                    fireParamEditorFinished(new ParamEditorFinishedEvent(this));
                }
            }
            if (sshOutfile != null) {
                SSHFileChooser sshFc = (SSHFileChooser) fileChooser;
                SSHDataHandler handler = sshFc.getHandler();
                String localFile = handler.createTempPath(sshOutfile);
                localFile += sshOutfile.substring(sshOutfile.lastIndexOf("/") + 1, sshOutfile.length());
                outfile = new File(localFile);
                JSimLogic.getInstance().writeParameterFile(this, outfile);
                try {
                    handler.copyToServer(outfile);
                    fireParamEditorFinished(new ParamEditorFinishedEvent(this));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }
