    public void install() {
        location = new File(tfLoc.getText());
        if (!location.exists()) {
            location.mkdirs();
        }
        if (!location.isDirectory()) location = new File(location.getParentFile(), "jsynoptic");
        tfLoc.setText(location.getAbsolutePath());
        boolean overwriteAll = false;
        byte[] data = new byte[4096];
        installer.previous.setEnabled(false);
        installer.next.setEnabled(false);
        tfLoc.setEnabled(false);
        btnLoc.setEnabled(false);
        try {
            for (int i = 1; i <= numPackages; ++i) {
                JCheckBox cb = (JCheckBox) boxes.get(i - 1);
                if (!cb.isSelected()) continue;
                cb.setBackground(Color.red);
                String zipName;
                try {
                    if (installer.jre == null) {
                        try {
                            zipName = Installer.resources.getString("packageZip" + i + os + "NoJRE");
                        } catch (MissingResourceException e) {
                            zipName = Installer.resources.getString("packageZip" + i + os);
                        }
                    } else {
                        zipName = Installer.resources.getString("packageZip" + i + os);
                    }
                } catch (MissingResourceException e1) {
                    zipName = Installer.resources.getString("packageZip" + i);
                }
                InputStream is = InstallerResources.class.getResourceAsStream(zipName);
                if (is == null) throw new IOException();
                ZipInputStream zip = new ZipInputStream(is);
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    File f = new File(location, entry.getName());
                    if (entry.isDirectory()) {
                        f.mkdirs();
                        continue;
                    }
                    if (f.exists() && (!overwriteAll)) {
                        Object[] options = new Object[] { Installer.resources.getString("yes"), Installer.resources.getString("yesAll"), Installer.resources.getString("no"), Installer.resources.getString("cancel") };
                        int res = JOptionPane.showOptionDialog(installer, Installer.resources.getString("overwriteFile") + f.getAbsolutePath(), Installer.resources.getString("overwriteTitle"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (res == 2) continue;
                        if ((res == JOptionPane.CLOSED_OPTION) || (res == 3)) break;
                        if (res == 1) overwriteAll = true;
                    }
                    long size = entry.getSize();
                    if (size != -1) {
                        progress.setIndeterminate(false);
                        progress.setMinimum(0);
                        progress.setMaximum((int) size);
                        progress.setValue(0);
                    } else progress.setIndeterminate(true);
                    progress.setString(f.getAbsolutePath());
                    progress.setStringPainted(true);
                    FileOutputStream fos = new FileOutputStream(f);
                    int nread;
                    while ((nread = zip.read(data)) != -1) {
                        fos.write(data, 0, nread);
                        try {
                            SwingUtilities.invokeAndWait(new BarUpdater(nread));
                        } catch (InterruptedException e1) {
                        } catch (InvocationTargetException e1) {
                        }
                    }
                    fos.flush();
                    fos.close();
                }
                cb.setBackground(Color.green);
            }
            if (installer.zip != null) {
                ZipFile zip = new ZipFile(installer.zip);
                ZipEntry entry = zip.getEntry("jre");
                if (entry == null) throw new IOException("Internal Error!");
                Enumeration enumEntries = zip.entries();
                while (enumEntries.hasMoreElements()) {
                    entry = (ZipEntry) enumEntries.nextElement();
                    if (!entry.getName().startsWith("jre")) continue;
                    File f = new File(location, entry.getName());
                    if (entry.isDirectory()) {
                        f.mkdirs();
                        continue;
                    }
                    if (f.exists() && (!overwriteAll)) {
                        Object[] options = new Object[] { Installer.resources.getString("yes"), Installer.resources.getString("yesAll"), Installer.resources.getString("no"), Installer.resources.getString("cancel") };
                        int res = JOptionPane.showOptionDialog(installer, Installer.resources.getString("overwriteFile") + f.getAbsolutePath(), Installer.resources.getString("overwriteTitle"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (res == 2) continue;
                        if ((res == JOptionPane.CLOSED_OPTION) || (res == 3)) break;
                        if (res == 1) overwriteAll = true;
                    }
                    long size = entry.getSize();
                    if (size != -1) {
                        progress.setIndeterminate(false);
                        progress.setMinimum(0);
                        progress.setMaximum((int) size);
                        progress.setValue(0);
                    } else progress.setIndeterminate(true);
                    progress.setString(Installer.resources.getString("installingJRE") + " : " + f.getAbsolutePath());
                    progress.setStringPainted(true);
                    FileOutputStream fos = new FileOutputStream(f);
                    InputStream zipStream = zip.getInputStream(entry);
                    int nread;
                    while ((nread = zipStream.read(data)) != -1) {
                        fos.write(data, 0, nread);
                        try {
                            SwingUtilities.invokeAndWait(new BarUpdater(nread));
                        } catch (InterruptedException e1) {
                        } catch (InvocationTargetException e1) {
                        }
                    }
                    fos.flush();
                    fos.close();
                }
                zip.close();
            } else if (installer.jre != null) {
                progress.setMinimum(0);
                progress.setMaximum(1);
                progress.setValue(0);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            progress.setIndeterminate(true);
                            progress.setString(Installer.resources.getString("installingJRE"));
                        }
                    });
                } catch (InterruptedException e1) {
                } catch (InvocationTargetException e1) {
                }
                copy(installer.jre, location);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            progress.setIndeterminate(false);
                            progress.setValue(1);
                        }
                    });
                } catch (InterruptedException e1) {
                } catch (InvocationTargetException e1) {
                }
            }
            if (os.equals("Unix")) {
                File chmod = new File("/usr/bin/chmod");
                if (!chmod.exists()) chmod = new File("/bin/chmod");
                if (!chmod.exists()) chmod = new File("/usr/local/bin/chmod");
                if (!chmod.exists()) chmod = new File("/sbin/chmod");
                if (!chmod.exists()) chmod = new File("/usr/sbin/chmod");
                if (!chmod.exists()) chmod = new File("/usr/local/sbin/chmod");
                if (chmod.exists()) {
                    String cmd = chmod.getAbsolutePath() + " +x ";
                    File jsynoptic = new File(location, "jsynoptic.sh");
                    if (jsynoptic.exists()) Runtime.getRuntime().exec(cmd + jsynoptic.getAbsolutePath());
                    File jreBinDir = new File(new File(location, "jre"), "bin");
                    if (jreBinDir.exists()) {
                        File[] binaries = jreBinDir.listFiles();
                        for (int i = 0; i < binaries.length; ++i) {
                            Runtime.getRuntime().exec(cmd + binaries[i].getAbsolutePath());
                        }
                    }
                    Runtime.getRuntime().exec(cmd + "jsynoptic.sh");
                }
            }
        } catch (IOException e) {
            installer.previous.setEnabled(true);
            installer.next.setEnabled(true);
            tfLoc.setEnabled(true);
            btnLoc.setEnabled(true);
            JOptionPane.showMessageDialog(installer, Installer.resources.getString("installErrorIs") + e.getLocalizedMessage(), Installer.resources.getString("errorInInstall"), JOptionPane.ERROR_MESSAGE);
            synchronized (this) {
                installFinished = false;
                installThread = null;
            }
            return;
        }
        synchronized (this) {
            installFinished = true;
            progress.setString(Installer.resources.getString("installComplete"));
            installer.next.setText(Installer.resources.getString("run"));
            installer.cancel.setText(Installer.resources.getString("dontRun"));
            ActionListener[] listeners = installer.cancel.getActionListeners();
            for (int i = 0; i < listeners.length; ++i) installer.cancel.removeActionListener(listeners[i]);
            installer.cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    installer.writeProperties();
                    System.exit(0);
                }
            });
            installer.next.setEnabled(true);
        }
    }
