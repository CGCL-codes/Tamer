    public void extract(String jarFile) {
        File currentArchive = new File(jarFile);
        File outputDir = getOutputDirectory(currentArchive);
        if (outputDir != null) {
            byte[] buf = new byte[1024];
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mma", Locale.getDefault());
            ProgressMonitor pm = null;
            boolean overwrite = false;
            ZipFile zf = null;
            FileOutputStream out = null;
            InputStream in = null;
            try {
                zf = new ZipFile(currentArchive);
                int size = zf.size();
                int extracted = 0;
                pm = new ProgressMonitor(getParent(), "Extracting files...", "starting", 0, size - 4);
                pm.setMillisToDecideToPopup(0);
                pm.setMillisToPopup(0);
                Enumeration<? extends ZipEntry> entries = zf.entries();
                for (int i = 0; i < size; i++) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String pathname = entry.getName();
                    if (_myClassName.equals(pathname) || MANIFEST.equals(pathname.toUpperCase())) {
                        continue;
                    }
                    extracted++;
                    pm.setProgress(i);
                    pm.setNote(pathname);
                    if (pm.isCanceled()) {
                        return;
                    }
                    in = zf.getInputStream(entry);
                    File outFile = new File(outputDir, pathname);
                    Date archiveTime = new Date(entry.getTime());
                    if (overwrite == false) {
                        if (outFile.exists()) {
                            Object[] options = { "Yes", "Yes To All", "No" };
                            Date existTime = new Date(outFile.lastModified());
                            Long archiveLen = new Long(entry.getSize());
                            String msg = "File name conflict: " + "There is already a file with " + "that name on the disk!\n" + "\nFile name: " + outFile.getName() + "\nExisting file: " + formatter.format(existTime) + ",  " + outFile.length() + "Bytes" + "\nFile in archive:" + formatter.format(archiveTime) + ",  " + archiveLen + "Bytes" + "\n\nWould you like to overwrite the file?";
                            int result = JOptionPane.showOptionDialog(JarSelfExtractor.this, msg, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                            if (result == 2) {
                                continue;
                            } else if (result == 1) {
                                overwrite = true;
                            }
                        }
                    }
                    File parent = new File(outFile.getParent());
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    out = new FileOutputStream(outFile);
                    while (true) {
                        int nRead = in.read(buf, 0, buf.length);
                        if (nRead <= 0) {
                            break;
                        }
                        out.write(buf, 0, nRead);
                    }
                    out.close();
                    outFile.setLastModified(archiveTime.getTime());
                }
                pm.close();
                zf.close();
                getToolkit().beep();
                JOptionPane.showMessageDialog(JarSelfExtractor.this, "Extracted " + extracted + " file" + ((extracted > 1) ? "s" : "") + " from the\n" + jarFile + "\narchive into the\n" + outputDir.getPath() + "\ndirectory.", "Zip Self Extractor", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (zf != null) {
                        zf.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }
