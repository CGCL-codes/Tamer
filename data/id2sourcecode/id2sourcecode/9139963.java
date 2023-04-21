    public static void main(String[] args) {
        try {
            processCommandLine(args);
            if (useGUI) {
                Class c = Class.forName("com.sshtools.j2ssh.keygen.Main");
                Method m = c.getMethod("main", new Class[] { args.getClass() });
                m.invoke(null, new Object[] { new String[] {} });
            } else {
                File f = new File(filename);
                if (filename == null) {
                    System.err.print("You must supply a valid file to convert!");
                    System.exit(1);
                }
                if (toOpenSSH || toSECSH) {
                    if (!f.exists()) {
                        System.err.print("The file " + f.getAbsolutePath() + " does not exist!");
                        System.exit(1);
                    }
                    try {
                        if (toOpenSSH) {
                            System.out.print(convertPublicKeyFile(f, new OpenSSHPublicKeyFormat()));
                        } else {
                            System.out.print(convertPublicKeyFile(f, new SECSHPublicKeyFormat()));
                        }
                    } catch (InvalidSshKeyException e) {
                        System.err.println("The key format is invalid!");
                    } catch (IOException ioe) {
                        System.err.println("An error occurs whilst reading the file " + f.getAbsolutePath());
                    }
                    System.exit(0);
                }
                if (changePass) {
                    if (!f.exists()) {
                        System.err.print("The file " + f.getAbsolutePath() + " does not exist!");
                        System.exit(1);
                    }
                    changePassphrase(f);
                } else {
                    SshKeyGenerator generator = new SshKeyGenerator();
                    String username = System.getProperty("user.name");
                    generator.generateKeyPair(type, bits, filename, username, null);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
