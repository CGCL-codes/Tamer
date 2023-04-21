    public static void main(String[] args) throws Exception {
        try {
            if (args.length == 0) {
                printUsage();
                if (!TestUtil.isTesting()) {
                    System.exit(2);
                } else {
                    return;
                }
            }
            ConfigureTranche.loadDefaults(ConfigureTranche.DEFAULT_CONFIG_FILE_LOCATION);
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-d") || args[i].equals("--debug")) {
                    DebugUtil.setDebug(MakeRepositoryCertsTool.class, true);
                    DebugUtil.setDebug(MakeUserZipFileTool.class, true);
                    DebugUtil.setDebug(UserZipFile.class, true);
                } else if (args[0].equals("-n") || args[0].equals("--buildnumber") || args[0].equals("-V") || args[0].equals("--version")) {
                    System.out.println("Tranche, build #@buildNumber");
                    if (!TestUtil.isTesting()) {
                        System.exit(0);
                    } else {
                        return;
                    }
                } else if (args[0].equals("-h") || args[0].equals("--help")) {
                    printUsage();
                    if (!TestUtil.isTesting()) {
                        System.exit(0);
                    } else {
                        return;
                    }
                } else if (args[i].equals("-v") || args[i].equals("--verbose")) {
                    verbose = true;
                }
            }
            File directory = null;
            try {
                directory = new File(args[args.length - 1]);
            } catch (Exception e) {
                System.err.println("ERROR: Invalid value for output directory.");
                DebugUtil.debugErr(MakeRepositoryCertsTool.class, e);
                if (!TestUtil.isTesting()) {
                    System.exit(2);
                } else {
                    return;
                }
            }
            printVerbose("Directory to save certificates: " + directory.getAbsolutePath());
            if (!directory.exists()) {
                System.err.println("ERROR: Directory does not exist: " + directory.getAbsolutePath());
                if (!TestUtil.isTesting()) {
                    System.exit(3);
                } else {
                    return;
                }
            }
            MakeUserZipFileTool tool = new MakeUserZipFileTool();
            tool.setValidDays(Long.valueOf("365000"));
            FileOutputStream fos = null;
            try {
                {
                    printVerbose("Creating Administrator Certificate");
                    tool.setName("Admin");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("Administator Certificate Passphrase: " + passphrase);
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "admin.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: Administrator zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    File adminCert = new File(directory, "admin.public.certificate");
                    printVerbose("Creating administrator certificate file: " + adminCert.getAbsolutePath());
                    if (adminCert.exists()) {
                        System.err.println("ERROR: Administrator certificate file already exists: " + adminCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!adminCert.createNewFile()) {
                        System.err.println("ERROR: Could not create administator certificate file: " + adminCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the administrator certificate to " + adminCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(adminCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    File passphrasesFile = new File(directory, "passphrases.txt");
                    printVerbose("Creating passphrases file: " + passphrasesFile.getAbsolutePath());
                    if (!passphrasesFile.createNewFile()) {
                        System.err.println("ERROR: Could not create passphrases file: " + passphrasesFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    fos = new FileOutputStream(passphrasesFile);
                    fos.write(("Administator: " + passphrase + "\n").getBytes());
                }
                {
                    printVerbose("Creating Write-Only Certificate");
                    tool.setName("Write-Only");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("Write-Only Certificate Passphrase: " + passphrase);
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "write.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: Write-only zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    File writeCert = new File(directory, "write.public.certificate");
                    printVerbose("Creating write-only certificate file: " + writeCert.getAbsolutePath());
                    if (writeCert.exists()) {
                        System.err.println("ERROR: Write-only certificate file already exists: " + writeCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!writeCert.createNewFile()) {
                        System.err.println("ERROR: Could not create write-only certificate file: " + writeCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the write-only certificate to " + writeCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(writeCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    fos.write(("Write-Only: " + passphrase + "\n").getBytes());
                }
                UserZipFile readUser = null;
                {
                    printVerbose("Creating Read-Only Certificate");
                    tool.setName("Read-Only");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("Read-Only Certificate Passphrase: " + passphrase);
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "read.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: Read-only zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    readUser = user;
                    File readCert = new File(directory, "read.public.certificate");
                    printVerbose("Creating read-only certificate file: " + readCert.getAbsolutePath());
                    if (readCert.exists()) {
                        System.err.println("ERROR: Read-only certificate file already exists: " + readCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!readCert.createNewFile()) {
                        System.err.println("ERROR: Could not create read-only certificate file: " + readCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the read-only certificate to " + readCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(readCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    fos.write(("Read-Only: " + passphrase + "\n").getBytes());
                }
                {
                    printVerbose("Creating User Certificate");
                    tool.setName("User");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("User Certificate Passphrase: " + passphrase);
                    fos.write(("User: " + passphrase + "\n").getBytes());
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "user.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: User zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    File userCert = new File(directory, "user.public.certificate");
                    printVerbose("Creating user certificate file: " + userCert.getAbsolutePath());
                    if (userCert.exists()) {
                        System.err.println("ERROR: User certificate file already exists: " + userCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!userCert.createNewFile()) {
                        System.err.println("ERROR: Could not create user certificate file: " + userCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the user certificate to " + userCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(userCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                }
                {
                    printVerbose("Creating AutoCert Certificate");
                    tool.setName("AutoCert");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("AutoCert Certificate Passphrase: " + passphrase);
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "autocert.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: User zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    File autocertCert = new File(directory, "autocert.public.certificate");
                    printVerbose("Creating auto certificate file: " + autocertCert.getAbsolutePath());
                    if (autocertCert.exists()) {
                        System.err.println("ERROR: Auto certificate file already exists: " + autocertCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!autocertCert.createNewFile()) {
                        System.err.println("ERROR: Could not create auto certificate file: " + autocertCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the auto certificate to " + autocertCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(autocertCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    fos.write(("AutoCert: " + passphrase + "\n").getBytes());
                }
                {
                    printVerbose("Creating Anonymous Certificate");
                    tool.setName("Anonymous");
                    tool.setSignerCertificate(readUser.getCertificate());
                    tool.setSignerPrivateKey(readUser.getPrivateKey());
                    printVerbose("Anonymous certificate signed by read-only certificate.");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("Anonymous Certificate Passphrase: " + passphrase);
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "anonymous.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: Anonymous zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    File anonCert = new File(directory, "anonymous.public.certificate");
                    printVerbose("Creating anonymous certificate file: " + anonCert.getAbsolutePath());
                    if (anonCert.exists()) {
                        System.err.println("ERROR: Anonymous certificate file already exists: " + anonCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!anonCert.createNewFile()) {
                        System.err.println("ERROR: Could not create anonymous certificate file: " + anonCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the anonymous certificate to " + anonCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(anonCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    File anonKey = new File(directory, "anonymous.private.key");
                    printVerbose("Creating anonymous private key file: " + anonKey.getAbsolutePath());
                    if (anonKey.exists()) {
                        System.err.println("ERROR: Anonymous private key file already exists: " + anonKey.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!anonKey.createNewFile()) {
                        System.err.println("ERROR: Could not create anonymous private key file: " + anonKey.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the anonymous private key to " + anonCert.getAbsolutePath());
                    try {
                        os = new FileOutputStream(anonKey);
                        os.write(user.getPrivateKey().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    fos.write(("Anonymous: " + passphrase + "\n").getBytes());
                }
                {
                    printVerbose("Creating Email Certificate");
                    tool.setName("Email");
                    String passphrase = SecurityUtil.generateBase64Password(15);
                    printVerbose("Email Certificate Passphrase: " + passphrase);
                    tool.setPassphrase(passphrase);
                    File saveFile = new File(directory, "email.zip.encrypted");
                    if (saveFile.exists()) {
                        System.err.println("ERROR: Email zip file file already exists: " + saveFile.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    tool.setSaveFile(saveFile);
                    UserZipFile user = tool.makeCertificate();
                    File emailCert = new File(directory, "email.public.certificate");
                    printVerbose("Creating email certificate file: " + emailCert.getAbsolutePath());
                    if (emailCert.exists()) {
                        System.err.println("ERROR: Email certificate file already exists: " + emailCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!emailCert.createNewFile()) {
                        System.err.println("ERROR: Could not create email certificate file: " + emailCert.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the email certificate to " + emailCert.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(emailCert);
                        os.write(user.getCertificate().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    File emailKey = new File(directory, "email.private.key");
                    printVerbose("Creating email private key file: " + emailKey.getAbsolutePath());
                    if (emailKey.exists()) {
                        System.err.println("ERROR: Email private key file already exists: " + emailKey.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    if (!emailKey.createNewFile()) {
                        System.err.println("ERROR: Could not create email private key file: " + emailKey.getAbsolutePath());
                        if (!TestUtil.isTesting()) {
                            System.exit(3);
                        } else {
                            return;
                        }
                    }
                    printVerbose("Writing out the email private key to " + emailKey.getAbsolutePath());
                    try {
                        os = new FileOutputStream(emailKey);
                        os.write(user.getPrivateKey().getEncoded());
                    } finally {
                        IOUtil.safeClose(os);
                    }
                    fos.write(("Email: " + passphrase + "\n").getBytes());
                }
            } finally {
                IOUtil.safeClose(fos);
            }
            printVerbose("Certificates created in: " + directory.getAbsolutePath());
            if (!TestUtil.isTesting()) {
                System.exit(0);
            } else {
                return;
            }
        } catch (Exception e) {
            DebugUtil.debugErr(MakeRepositoryCertsTool.class, e);
            if (!TestUtil.isTesting()) {
                System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            } else {
                return;
            }
        }
    }
