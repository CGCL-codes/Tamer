        @Override
        public void run() {
            super.setPriority(Thread.MIN_PRIORITY);
            JPanel comp = new JPanel();
            comp.setMinimumSize(new Dimension(250, 75));
            comp.setPreferredSize(new Dimension(250, 75));
            comp.setLayout(new GridLayout(4, 1));
            cancel = new JButton("Cancel");
            cancel.setFont(Core.Resources.Font);
            cancel.setMnemonic('C');
            cancel.setFocusable(false);
            cancel.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    clock.stop();
                    stopped = true;
                    DouzDialog window = (DouzDialog) cancel.getRootPane().getParent();
                    window.dispose();
                }
            });
            progressbar_bytes = new JProgressBar(1, 100);
            progressbar_bytes.setStringPainted(true);
            progressbar_bytes.setFont(Core.Resources.Font);
            progressbar_file = new JProgressBar(1, 100);
            progressbar_file.setStringPainted(true);
            progressbar_file.setFont(Core.Resources.Font);
            progressbar_overall = new JProgressBar(1, 100);
            progressbar_overall.setStringPainted(true);
            progressbar_overall.setFont(Core.Properties.get("org.dyndns.doujindb.ui.font").asFont());
            comp.add(progressbar_overall);
            comp.add(progressbar_file);
            comp.add(progressbar_bytes);
            comp.add(cancel);
            clock = new Timer(50, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    int bytes = (int) (progress_bytes_current * 100 / progress_bytes_max);
                    progressbar_bytes.setString(bytes + "%");
                    progressbar_bytes.setValue(bytes);
                    int file = (int) (progress_file_current * 100 / progress_file_max);
                    progressbar_file.setString(file + "%");
                    progressbar_file.setValue(file);
                    int overall = (int) (progress_overall_current * 100 / progress_overall_max);
                    progressbar_overall.setValue(overall);
                }
            });
            clock.start();
            Vector<String> errors = new Vector<String>();
            try {
                Core.UI.Desktop.showDialog(comp, Core.Resources.Icons.get("JFrame/MediaManager/Import"), "Importing ...");
                progress_overall_max = files.length;
                for (File file : files) {
                    try {
                        ZipFile zip = new ZipFile(file);
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        boolean valid = false;
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            if (entry.getName().equals(PACKAGE_INDEX)) {
                                valid = true;
                                DataFile ds = Core.Repository.child(parseXML(zip.getInputStream(entry)));
                                ds.mkdirs();
                                ;
                                entries = zip.entries();
                                progress_file_max = zip.size();
                                progress_file_current++;
                                while (entries.hasMoreElements()) {
                                    try {
                                        entry = entries.nextElement();
                                        if (entry.getName().equals(PACKAGE_INDEX)) {
                                            progress_file_current++;
                                            continue;
                                        }
                                        if (!entry.getName().startsWith(PACKAGE_MEDIA)) {
                                            progress_file_current++;
                                            continue;
                                        }
                                        DataFile ds0 = ds.child(entry.getName().substring(PACKAGE_MEDIA.length()));
                                        if (entry.isDirectory()) {
                                            ds0.mkdirs();
                                        } else {
                                            OutputStream out = ds0.getOutputStream();
                                            InputStream in = zip.getInputStream(entry);
                                            byte[] buff = new byte[0x800];
                                            int read;
                                            progress_bytes_current = 0;
                                            progress_bytes_max = entry.getSize();
                                            while ((read = in.read(buff)) != -1) {
                                                out.write(buff, 0, read);
                                                progress_bytes_current += read;
                                                if (stopped) throw new Exception("Thread stopped by user input.");
                                            }
                                            in.close();
                                            out.close();
                                        }
                                        progress_file_current++;
                                    } catch (Exception e) {
                                        progress_file_current++;
                                    }
                                }
                                valid = true;
                                break;
                            }
                        }
                        if (!valid) errors.add(file.getName());
                        progress_overall_current++;
                    } catch (IOException ioe) {
                        errors.add(file.getName());
                    } catch (RepositoryException dse) {
                        errors.add(file.getName());
                    } catch (DataBaseException dbe) {
                        errors.add(file.getName());
                    }
                }
            } catch (PropertyVetoException pve) {
                pve.printStackTrace();
                Core.Logger.log(pve.getMessage(), Level.WARNING);
            }
            clock.stop();
            DouzDialog window = (DouzDialog) comp.getRootPane().getParent();
            window.dispose();
            ;
            if (errors.size() == 0) return;
            {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                panel.setLayout(new BorderLayout(5, 5));
                JLabel lab = new JLabel("<html><body>" + "The following entries were not imported.<br>" + "Make sure the provided files are valid archives.<br>" + "</body></html>");
                panel.add(lab, BorderLayout.NORTH);
                JList<String> list = new JList<String>(errors);
                list.setFont(Core.Properties.get("org.dyndns.doujindb.ui.font").asFont());
                list.setSelectionBackground(list.getSelectionForeground());
                list.setSelectionForeground(Core.Properties.get("org.dyndns.doujindb.ui.theme.background").asColor());
                panel.add(new JScrollPane(list), BorderLayout.CENTER);
                JButton ok = new JButton("Ok");
                ok.setFont(Core.Resources.Font);
                ok.setMnemonic('O');
                ok.setFocusable(false);
                ok.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        DouzDialog window = (DouzDialog) ((JComponent) ae.getSource()).getRootPane().getParent();
                        window.dispose();
                    }
                });
                JPanel centered = new JPanel();
                centered.setLayout(new GridLayout(1, 3));
                centered.add(new JLabel());
                centered.add(ok);
                centered.add(new JLabel());
                panel.add(centered, BorderLayout.SOUTH);
                try {
                    Core.UI.Desktop.showDialog(panel, Core.Resources.Icons.get("JDesktop/Explorer/Book/Media/Unpack"), "Unpacking - Error");
                } catch (PropertyVetoException pve) {
                }
            }
        }
