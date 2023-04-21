    public boolean checkLibraries(Frame owner) throws NoSuchAlgorithmException, IOException {
        String algorithm = "SHA-1";
        Os os = detectOs();
        Arch arch = detectArch();
        System.out.println("Platform identified as " + os + " " + arch);
        String dir = getNativeLibsDir(os, arch);
        String[] libs = getLibraryNames(os);
        String javaLibraryPath = System.getProperty("java.library.path");
        String[] folders = javaLibraryPath.split(System.getProperty("path.separator"));
        byte[][] digests = new byte[libs.length][];
        for (int i = 0; i < libs.length; i++) {
            System.out.println(dir + ":" + libs[i]);
            digests[i] = digest(ClassLoader.getSystemResourceAsStream(dir + libs[i]), algorithm);
            System.out.println(libs[i] + " " + digestToString(digests[i]));
        }
        int libraryFolder = -1;
        scanFolders: for (int i = 0; i < folders.length; i++) {
            String folder = folders[i];
            for (int j = 0; j < libs.length; j++) {
                String lib = libs[j];
                File libFile = new File(folder, lib);
                if (libFile.exists()) {
                    System.out.println(lib + " exists in " + folder);
                    libraryFolder = i;
                    break scanFolders;
                }
            }
        }
        if (libraryFolder == -1) {
            String message = "<b>The required JOGL native libraries have not been found. Please specify the folder " + "of the library path where JPatch should try to install the required libraries.</b>" + "<p>If the libraies have already been installed or you wish to install them to a different " + "folder, please add this folder to the library path and restart JPatch with the " + "<code>-Djava.library.path=<i>&lt;path&gt;</i></code> commandline switch.";
            JComboBox folderCombo = new JComboBox(folders);
            int selection = JPatchDialog.showDialog(owner, "JOGL native libraries installation", JPatchDialog.WARNING, message, folderCombo, new String[] { "Install", null, "Quit" }, 1, "400");
            if (selection != 0) {
                System.exit(0);
            }
            try {
                for (int i = 0; i < libs.length; i++) {
                    String lib = libs[i];
                    InputStream source = ClassLoader.getSystemResourceAsStream(dir + lib);
                    File destination = new File(folders[folderCombo.getSelectedIndex()], lib);
                    install(source, destination);
                }
            } catch (IOException e) {
                message = "<b>An error occured during the installation of the JOGL native libraries:</b><p>" + "<font color='red'>" + e.getMessage() + "</font>" + "<p>You possibly need administrator (root) privileges to install files in the specified folder.";
                JPatchDialog.showDialog(owner, "JPatch error", JPatchDialog.ERROR, message, null, new String[] { "OK" }, 0, "300");
            }
            return false;
        } else {
            List<Integer> reinstall = new ArrayList<Integer>();
            List<Integer> install = new ArrayList<Integer>();
            String folder = folders[libraryFolder];
            for (int i = 0; i < libs.length; i++) {
                String lib = libs[i];
                File libFile = new File(folder, lib);
                if (libFile.exists()) {
                    if (!Arrays.equals(digests[i], digest(new FileInputStream(libFile), algorithm))) {
                        reinstall.add(i);
                    }
                } else {
                    install.add(i);
                }
            }
            if (reinstall.size() == 0 && install.size() == 0) {
                return true;
            } else {
                String message, buttonText;
                if (install.size() == 0) {
                    message = "<b>The required JOGL native libraries have been found in " + folder + ", but they appear to be corrupt or of an unsupported version " + "and need to be re-installed.</b>" + "<p>Overwriting these files with the libraries from JOGL JSR-231 version 1.0.0 " + "may break other applications that depend on the installed version. If you do not " + "want to overwrite these files, select QUIT and restart JPatch with another " + "library-path by setting the " + "<code>-Djava.library.path=<i>&lt;path&gt;</i></code> commandline switch." + "<p>Choosing RE-INSTALL will overwrite the following files in " + folder + ":<ul>";
                    for (int i = 0; i < libs.length; i++) {
                        if (reinstall.contains(i)) {
                            message += "<li>" + libs[i] + "</li>";
                        }
                    }
                    message += "</ul>";
                    buttonText = "Re-Install";
                } else if (reinstall.size() == 0) {
                    message = "<b>The required JOGL native libraries have been found in " + folder + ", but some files appear to be missing and need to be installed.</b>" + "<p>If you do not " + "want to install the missing files in this folder, select QUIT and restart JPatch with another " + "library-path by setting the " + "<code>-Djava.library.path=<i>&lt;path&gt;</i></code> commandline switch.";
                    buttonText = "Install";
                } else {
                    message = "<b>Some of the required JOGL native libraries have been found in " + folder + ", but they appear to be corrupt or of an unsupported version " + "and need to be re-installed. Some files are missing and need to be installed.</b>" + "<p>Overwriting the already installed files with the libraries from JOGL JSR-231 version 1.0.0 " + "may break other applications that depend on the installed files. If you do not " + "want to overwrite these files, select QUIT and restart JPatch with another " + "library-path by setting the " + "<code>-Djava.library.path=<i>&lt;path&gt;</i></code> commandline switch." + "<p>Choosing RE-INSTALL will overwrite the following files in " + folder + ":";
                    for (int i = 0; i < libs.length; i++) {
                        if (reinstall.contains(i)) {
                            message += "<li>" + libs[i] + "</li>";
                        }
                    }
                    message += "</ul>";
                    buttonText = "Re-Install";
                }
                int selection = JPatchDialog.showDialog(owner, "JOGL native libraries installation", JPatchDialog.WARNING, message, null, new String[] { buttonText, null, "Quit" }, 1, "400");
                if (selection != 0) {
                    System.exit(0);
                }
                try {
                    for (int i = 0; i < libs.length; i++) {
                        String lib = libs[i];
                        InputStream source = ClassLoader.getSystemResourceAsStream(dir + lib);
                        File destination = new File(folder, lib);
                        install(source, destination);
                    }
                } catch (IOException e) {
                    message = "<b>An error occured during the installation of the JOGL native libraries:</b><p>" + "<font color='red'>" + e.getMessage() + "</font>" + "<p>You possibly need administrator (root) privileges to install files in the specified folder.";
                    JPatchDialog.showDialog(owner, "JPatch error", JPatchDialog.ERROR, message, null, new String[] { "OK" }, 0, "300");
                }
                return false;
            }
        }
    }
