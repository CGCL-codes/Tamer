            @Override
            public void actionPerformed(ActionEvent e) {
                getMe().hexEditor.getTransferHandler();
                fc.setSelectedFile(new File(txtFileName.getText()));
                int returnVal = fc.showSaveDialog(getMe());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (fc.getSelectedFile().exists()) {
                        int confirm = JOptionPane.showConfirmDialog(getMe(), "<html>The selected file already exists.<br>Do you want to overwrite?", "File already exits", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    strfileName = fc.getSelectedFile().getAbsolutePath();
                    txtFileName.setText(strfileName);
                    new SaveContent(getMe(), txtFileName.getText(), hexEditor.getByteContent());
                }
            }
