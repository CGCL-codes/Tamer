        public void run() {
            if (main != null) {
                Iterator iter = main.filesets.iterator();
                java.util.zip.ZipOutputStream zout = new java.util.zip.ZipOutputStream(pout);
                try {
                    ZipEntry m = new ZipEntry(META_INF_MANIFEST);
                    zout.putNextEntry(m);
                    if (main.manifest != null) {
                        copy(new FileInputStream(main.manifest), zout, true);
                    } else if (mainManifest != null) {
                        copy(new FileInputStream(mainManifest), zout, true);
                    }
                    zout.closeEntry();
                    while (iter.hasNext()) {
                        FileSet fileset = (FileSet) iter.next();
                        FileScanner scanner = fileset.getDirectoryScanner(getProject());
                        String[] files = scanner.getIncludedFiles();
                        File basedir = scanner.getBasedir();
                        for (int i = 0; i < files.length; i++) {
                            String file = files[i].replace('\\', '/');
                            if (entries.contains(file)) {
                                log("Duplicate entry " + target + " (ignored): " + file, Project.MSG_WARN);
                                continue;
                            }
                            entries.add(file);
                            String p = new File(file).getParent();
                            if (p != null) {
                                String dirs = p.replace('\\', '/');
                                if (!entries.contains(dirs)) {
                                    String toks[] = dirs.split("/");
                                    String dir = "";
                                    for (int d = 0; d < toks.length; d++) {
                                        dir += toks[d] + "/";
                                        if (!entries.contains(dir)) {
                                            ZipEntry ze = new ZipEntry(dir);
                                            zout.putNextEntry(ze);
                                            zout.flush();
                                            zout.closeEntry();
                                            entries.add(dir);
                                        }
                                    }
                                    entries.add(dir);
                                }
                            }
                            ZipEntry ze = new ZipEntry(file);
                            zout.putNextEntry(ze);
                            log("processing " + file, Project.MSG_DEBUG);
                            FileInputStream fis = new FileInputStream(new File(basedir, file));
                            copy(fis, zout, true);
                            zout.closeEntry();
                        }
                    }
                    zout.close();
                    synchronized (this) {
                        done = true;
                        notify();
                    }
                } catch (IOException iox) {
                    throw new BuildException(iox);
                }
            }
        }
