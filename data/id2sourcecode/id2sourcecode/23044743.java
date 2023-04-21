    protected void updateModule(final ModulePanel mp, final LModule m) {
        mp.install.setVisible(false);
        mp.update.setVisible(false);
        mp.cancel.setVisible(true);
        mp.progress.setVisible(true);
        mp.statistics.setVisible(true);
        SwingWorker<Void, Void> pretest = new SwingWorker<Void, Void>() {

            final List<FileEntry> localFiles = new ArrayList<FileEntry>();

            @Override
            protected Void doInBackground() throws Exception {
                long t = System.currentTimeMillis();
                int index = 0;
                for (LFile f : m.files) {
                    int idx = f.url.lastIndexOf("/");
                    final String lf = f.url.substring(idx + 1);
                    File lff = new File(lf);
                    try {
                        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                        if (lff.canRead()) {
                            byte[] buffer = new byte[64 * 1024];
                            long bytesReceived = 0;
                            long bytesTotal = lff.length();
                            try {
                                FileInputStream fin = new FileInputStream(lff);
                                do {
                                    int read = fin.read(buffer);
                                    if (read > 0) {
                                        bytesReceived += read;
                                        final int findex = index;
                                        final int fsize = m.files.size();
                                        final long fbytesReceived = bytesReceived;
                                        final long fbytesTotal = bytesTotal;
                                        SwingUtilities.invokeLater(new Runnable() {

                                            @Override
                                            public void run() {
                                                mp.statistics.setText("[" + (findex + 1) + " / " + fsize + "] " + lf + " (" + String.format("%.2f", fbytesReceived / 1024.0 / 1024.0) + " MB)");
                                                mp.progress.setValue((int) (100 * fbytesReceived / fbytesTotal));
                                            }

                                            ;
                                        });
                                        sha1.update(buffer, 0, read);
                                    } else if (read < 0) {
                                        break;
                                    }
                                } while (true);
                                byte[] sha1h = sha1.digest();
                                byte[] sha1hupdate = LFile.toByteArray(f.sha1);
                                if (!Arrays.equals(sha1h, sha1hupdate)) {
                                    localFiles.add(new FileEntry(lf + "." + t, f));
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            localFiles.add(new FileEntry(lf + "." + t, f));
                        }
                        index++;
                    } catch (NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                if (localFiles.size() == 0) {
                    installedVersions.put(m.id, m.version);
                    if (m.id.equals("Launcher")) {
                        listPanel.remove(mp);
                        listPanel.revalidate();
                        listPanel.repaint();
                    } else {
                        setVisibleModuleButtons(m, mp);
                    }
                    return;
                }
                final Downloader[] currentDownloader = new Downloader[1];
                downloadLoop(localFiles, mp, m, 0, currentDownloader);
                mp.cancel.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentDownloader[0].cancel(true);
                        mp.cancel.setVisible(false);
                        setVisibleModuleButtons(m, mp);
                    }
                });
            }
        };
        pretest.execute();
    }
