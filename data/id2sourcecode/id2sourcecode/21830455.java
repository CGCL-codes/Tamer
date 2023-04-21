    private void doDecrypt() {
        try {
            getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String passwd = getPassphrase();
            if (passwd == null) {
                return;
            }
            DecryptionResult decryptionResult;
            if (rdDecryptFile.isSelected()) {
                File tmpFile = File.createTempFile("dcx", ".dec");
                FileOutputStream fout = new FileOutputStream(tmpFile);
                FileInputStream fin = new FileInputStream(tbDecPath.getText());
                decryptionResult = PGPUtils.decryptFile(fin, passwd.toCharArray(), fout);
                if (confirmDecryption(decryptionResult)) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle(resMap.getString("Select.decrypted.file.path"));
                    chooser.setSelectedFile(new File(decryptionResult.getDecryptFileName()));
                    if (chooser.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    FileUtils.copyFile(tmpFile, chooser.getSelectedFile());
                }
                tmpFile.delete();
            } else {
                decryptionResult = PGPUtils.decryptText(tbDecText.getText(), passwd.toCharArray());
                String decText = decryptionResult.getDecryptedText();
                if (confirmDecryption(decryptionResult)) {
                    new TextShowDialog(getFrame(), decText).display();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(getFrame(), ex.getMessage(), Error, JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            getFrame().setCursor(Cursor.getDefaultCursor());
        }
    }
