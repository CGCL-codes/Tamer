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
            try {
                Core.UI.Desktop.showDialog(comp, Core.Resources.Icons.get("JFrame/MediaManager/Export"), "Exporting ...");
                progress_overall_max = books.size();
                for (Book book : books) {
                    File zip = new File(dest, book + Core.Properties.get("org.dyndns.doujindb.dat.file_extension").asString());
                    DataFile ds = Core.Repository.child(book.getID());
                    progress_file_max = count(ds);
                    progress_file_current = 0;
                    progressbar_overall.setString(book.toString());
                    try {
                        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zip));
                        zout.setLevel(9);
                        ;
                        ZipEntry entry = new ZipEntry(PACKAGE_INDEX);
                        zout.putNextEntry(entry);
                        writeXML(book, zout);
                        zout.closeEntry();
                        ;
                        entry = new ZipEntry(PACKAGE_MEDIA);
                        entry.setMethod(ZipEntry.DEFLATED);
                        try {
                            zout.putNextEntry(entry);
                            zout.closeEntry();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            Core.Logger.log(ioe.getMessage(), Level.WARNING);
                        }
                        ;
                        zip(PACKAGE_MEDIA, ds.children(), zout);
                        zout.close();
                    } catch (IOException ioe) {
                        zip.delete();
                        Core.Logger.log(ioe.getMessage(), Level.WARNING);
                    }
                    progress_overall_current++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Core.Logger.log(e.getMessage(), Level.WARNING);
            }
            clock.stop();
            DouzDialog window = (DouzDialog) comp.getRootPane().getParent();
            window.dispose();
        }
