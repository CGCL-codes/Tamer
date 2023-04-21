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
