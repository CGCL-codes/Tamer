    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == close) {
            dispose();
            return;
        }
        final String newPassphrase = new String(keygen.getNewPassphrase()).trim();
        final String oldPassphrase = new String(keygen.getOldPassphrase()).trim();
        if ((keygen.getAction() == KeygenPanel.GENERATE_KEY_PAIR) || (keygen.getAction() == KeygenPanel.CHANGE_PASSPHRASE)) {
            if (newPassphrase.length() == 0) {
                if (JOptionPane.showConfirmDialog(this, "Passphrase is empty. Are you sure?", "Empty Passphrase", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
            }
        }
        final File inputFile = new File(keygen.getInputFilename());
        final File outputFile = new File(keygen.getOutputFilename());
        final File publicFile = new File(keygen.getOutputFilename() + ".pub");
        if ((keygen.getAction() == KeygenPanel.CONVERT_IETF_SECSH_TO_OPENSSH) || (keygen.getAction() == KeygenPanel.CONVERT_OPENSSH_TO_IETF_SECSH) || (keygen.getAction() == KeygenPanel.GENERATE_KEY_PAIR)) {
            if (keygen.getOutputFilename().length() == 0) {
                JOptionPane.showMessageDialog(this, "No Output file supplied.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (outputFile.exists()) {
                if (JOptionPane.showConfirmDialog(this, "Output file " + outputFile.getName() + " exists. Are you sure?", "File exists", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            if (outputFile.exists() && !outputFile.canWrite()) {
                JOptionPane.showMessageDialog(this, "Output file " + outputFile.getName() + " can not be written.", "Unwriteable file", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if ((keygen.getAction() == KeygenPanel.CONVERT_IETF_SECSH_TO_OPENSSH) || (keygen.getAction() == KeygenPanel.CONVERT_OPENSSH_TO_IETF_SECSH)) {
            if (keygen.getInputFilename().length() == 0) {
                JOptionPane.showMessageDialog(this, "No Input file supplied.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (keygen.getAction() == KeygenPanel.GENERATE_KEY_PAIR) {
            if (publicFile.exists() && !publicFile.canWrite()) {
                JOptionPane.showMessageDialog(this, "Public key file " + publicFile.getName() + " can not be written.", "Unwriteable file", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        final ProgressMonitor monitor = new ProgressMonitor(this, "Generating keys", "Generating", 0, 100);
        monitor.setMillisToDecideToPopup(0);
        monitor.setMillisToPopup(0);
        Runnable r = new Runnable() {

            public void run() {
                try {
                    if (keygen.getAction() == KeygenPanel.CHANGE_PASSPHRASE) {
                        monitor.setNote("Changing passphrase");
                        SshKeyGenerator.changePassphrase(inputFile, oldPassphrase, newPassphrase);
                        monitor.setNote("Complete");
                        JOptionPane.showMessageDialog(Main.this, "Passphrase changed", "Passphrase changed", JOptionPane.INFORMATION_MESSAGE);
                    } else if (keygen.getAction() == KeygenPanel.CONVERT_IETF_SECSH_TO_OPENSSH) {
                        monitor.setNote("Converting key file");
                        writeString(outputFile, SshKeyGenerator.convertPublicKeyFile(inputFile, new OpenSSHPublicKeyFormat()));
                        monitor.setNote("Complete");
                        JOptionPane.showMessageDialog(Main.this, "Key converted", "Key converted", JOptionPane.INFORMATION_MESSAGE);
                    } else if (keygen.getAction() == KeygenPanel.CONVERT_OPENSSH_TO_IETF_SECSH) {
                        monitor.setNote("Converting key file");
                        writeString(outputFile, SshKeyGenerator.convertPublicKeyFile(inputFile, new SECSHPublicKeyFormat()));
                        monitor.setNote("Complete");
                        JOptionPane.showMessageDialog(Main.this, "Key converted", "Key converted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        monitor.setNote("Creating generator");
                        SshKeyGenerator generator = new SshKeyGenerator();
                        monitor.setNote("Generating");
                        String username = System.getProperty("user.name");
                        generator.generateKeyPair(keygen.getType(), keygen.getBits(), outputFile.getAbsolutePath(), username, newPassphrase);
                        monitor.setNote("Complete");
                        JOptionPane.showMessageDialog(Main.this, "Key generated to " + outputFile.getName(), "Complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Main.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    monitor.close();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
