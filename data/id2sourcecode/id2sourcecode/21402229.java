    @Override
    public synchronized File chooseFile(final int operation, final File initialDir, final javax.swing.filechooser.FileFilter... filters) {
        final UserExperience xp = UserExperience.getUserExperience();
        final FileFilter[] adaptedFilters;
        if (filters == null || filters.length == 0) {
            adaptedFilters = new FileFilter[1];
        } else {
            adaptedFilters = new FileFilter[filters.length];
            for (int i = 0; i < filters.length; i++) {
                if (filters[i] == null || filters[i] instanceof FileFilter) {
                    adaptedFilters[i] = (FileFilter) filters[i];
                } else {
                    adaptedFilters[i] = new FileFilterAdapter(filters[i]);
                }
            }
        }
        final PrintWriter output;
        final Console console = System.console();
        if (console != null) {
            output = console.writer();
        } else {
            output = null;
        }
        File selectedFile = null;
        File currentDir = initialDir;
        if (currentDir == null) {
            try {
                currentDir = new File(System.getProperty("user.home"));
            } catch (final SecurityException e) {
                currentDir = new File("");
            }
        } else {
            try {
                while (currentDir != null && !currentDir.isDirectory()) {
                    currentDir = currentDir.getParentFile();
                }
            } catch (final SecurityException e) {
                LOG.info(e.toString());
            }
        }
        final SortedSet<String> filenames = new TreeSet<String>();
        final SortedSet<String> dirnames = new TreeSet<String>();
        boolean gotDirname = true;
        while (selectedFile == null && gotDirname) {
            if (output != null) {
                if (currentDir == null) {
                    output.println("Root locations");
                } else {
                    try {
                        currentDir = currentDir.getCanonicalFile();
                    } catch (final IOException e) {
                        try {
                            currentDir = currentDir.getAbsoluteFile();
                        } catch (final SecurityException e2) {
                            LOG.info(e2.toString());
                        }
                    } catch (final SecurityException e) {
                        LOG.info(e.toString());
                    }
                    if (currentDir != null) {
                        output.println("Directory of " + currentDir.toString());
                    }
                }
                output.println();
            }
            if (currentDir != null) {
                try {
                    for (final FileFilter filter : adaptedFilters) {
                        final File[] files = currentDir.listFiles(filter);
                        if (files != null) {
                            for (final File file : files) {
                                try {
                                    if (!file.isHidden()) {
                                        if (file.isDirectory()) {
                                            dirnames.add(file.getName());
                                        } else if (file.exists()) {
                                            filenames.add(file.getName());
                                        }
                                    }
                                } catch (final SecurityException e) {
                                    dirnames.add(file.getName());
                                }
                            }
                        } else {
                            System.err.println("I/O error.");
                        }
                    }
                } catch (final SecurityException e) {
                    if (output != null) output.println("Access denied.");
                }
                if (currentDir.getParent() != null) {
                    dirnames.add("..");
                } else {
                    final File[] roots = File.listRoots();
                    if (roots != null && roots.length > 1) {
                        dirnames.add("..");
                    }
                }
            } else {
                final File[] roots = File.listRoots();
                if (roots != null) {
                    for (final File root : roots) {
                        try {
                            if (!root.isHidden()) {
                                dirnames.add(root.getPath());
                            }
                        } catch (final SecurityException e) {
                            dirnames.add(root.getPath());
                        }
                    }
                }
            }
            if (output != null) {
                int i = 0;
                final StringBuilder line = new StringBuilder(80);
                for (final String dirname : dirnames) {
                    if (i % 2 == 1 && dirname.length() > 37) {
                        output.println();
                        i++;
                    }
                    line.append("+");
                    line.append(String.format("%1$#-37s", dirname));
                    line.append("  ");
                    i++;
                    if (i % 2 == 0 || dirname.length() > 37) {
                        output.println(line);
                        line.delete(0, line.length());
                        if (i % 2 == 1) i++;
                    }
                }
                for (final String filename : filenames) {
                    if (i % 2 == 1 && filename.length() > 38) {
                        output.println();
                        i++;
                    }
                    line.append(" ");
                    line.append(String.format("%1$#-38s", filename));
                    line.append(" ");
                    i++;
                    if (i % 2 == 0 || filename.length() > 38) {
                        output.println(line);
                        line.delete(0, line.length());
                        if (i % 2 == 1) i++;
                    }
                }
                if (i % 2 == 1) {
                    output.println(line);
                }
                output.println();
                output.flush();
            }
            gotDirname = false;
            do {
                System.err.flush();
                final String filename = this.input("", "Filename:", JOptionPane.PLAIN_MESSAGE);
                if (filename.equals("")) {
                    System.out.println("Cancelled.");
                    return null;
                }
                if (currentDir != null && filename.equals("..")) {
                    currentDir = currentDir.getParentFile();
                    gotDirname = true;
                } else {
                    String adjustedFilename = xp.getFilename(filename, null);
                    File chosenFile = new File(adjustedFilename);
                    if (!chosenFile.isAbsolute()) {
                        chosenFile = new File(currentDir, adjustedFilename);
                    }
                    boolean isExistingDir;
                    try {
                        isExistingDir = chosenFile.isDirectory();
                    } catch (final SecurityException e) {
                        isExistingDir = adjustedFilename.endsWith(File.separator);
                    }
                    if (isExistingDir) {
                        currentDir = chosenFile;
                        gotDirname = true;
                    } else if (adjustedFilename.endsWith(File.separator)) {
                        if (!chosenFile.exists()) {
                            final Confirmation confirm = this.confirm("", chosenFile.getPath() + " does not exist.  Create directory", JOptionPane.WARNING_MESSAGE);
                            if (confirm == Confirmation.YES) {
                                try {
                                    final boolean success = chosenFile.mkdirs();
                                    if (!success) {
                                        System.err.println("Failed.");
                                        try {
                                            do {
                                                chosenFile = chosenFile.getParentFile();
                                            } while (chosenFile != null && !chosenFile.isDirectory());
                                        } catch (final SecurityException e) {
                                            LOG.info(e.toString());
                                        }
                                        gotDirname = true;
                                    } else {
                                        currentDir = chosenFile;
                                        gotDirname = true;
                                    }
                                } catch (final SecurityException e) {
                                    System.err.println("Access denied.");
                                }
                            }
                        } else {
                            System.err.println(chosenFile.getName() + " is a file and not a directory.");
                        }
                    } else {
                        if (filters != null && filters.length > 0) {
                            boolean foundFilter = false;
                            for (final javax.swing.filechooser.FileFilter filter : filters) {
                                if (filter.accept(chosenFile)) {
                                    adjustedFilename = xp.getFilename(filename, filter);
                                    foundFilter = true;
                                    break;
                                }
                            }
                            if (!foundFilter) {
                                adjustedFilename = xp.getFilename(filename, filters[0]);
                            }
                            chosenFile = new File(adjustedFilename);
                            if (!chosenFile.isAbsolute()) {
                                chosenFile = new File(currentDir, adjustedFilename);
                            }
                        }
                        boolean canAccess;
                        if (operation == JFileChooser.OPEN_DIALOG) {
                            try {
                                canAccess = chosenFile.canRead();
                            } catch (final SecurityException e) {
                                canAccess = false;
                            }
                            if (!canAccess) {
                                System.err.println(adjustedFilename + " does not exist or cannot be accessed.");
                            } else {
                                selectedFile = chosenFile;
                            }
                        } else {
                            boolean exists;
                            try {
                                exists = chosenFile.exists();
                            } catch (final SecurityException e) {
                                exists = false;
                            }
                            final File parent = chosenFile.getParentFile();
                            boolean missingParent = false;
                            try {
                                missingParent = parent != null && !parent.isDirectory();
                                canAccess = (!exists && !missingParent) || chosenFile.canWrite();
                            } catch (final SecurityException e) {
                                canAccess = false;
                            }
                            if (canAccess) {
                                if (exists) {
                                    final Confirmation overwrite = this.confirm("", adjustedFilename + " already exists.  Overwrite", JOptionPane.WARNING_MESSAGE);
                                    if (overwrite == Confirmation.YES) {
                                        selectedFile = chosenFile;
                                    }
                                } else {
                                    selectedFile = chosenFile;
                                }
                            } else if (missingParent && parent != null) {
                                System.err.println(parent.toString() + " is not a valid directory.");
                            } else {
                                System.err.println("Access denied.");
                            }
                        }
                    }
                }
            } while (selectedFile == null && !gotDirname && output != null);
            dirnames.clear();
            filenames.clear();
        }
        return selectedFile;
    }
