        public void actionPerformed(ActionEvent e) {
            JPanel options = new JPanel(new SpringLayout());
            JCheckBox asZipFile = new JCheckBox("Save all images to ZIP file");
            options.add(asZipFile);
            options.add(new JLabel());
            ButtonGroup group = new ButtonGroup();
            JRadioButton jpeg = new JRadioButton("Save as JPEG");
            group.add(jpeg);
            jpeg.setActionCommand("JPEG");
            JRadioButton png = new JRadioButton("Save as PNG");
            png.setActionCommand("PNG");
            group.add(png);
            options.add(jpeg);
            options.add(png);
            jpeg.setSelected(true);
            SpringUtilities.makeCompactGrid(options, options.getComponentCount() / 2, 2, 0, 0, 2, 2);
            JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            chooser.setAccessory(options);
            int returnVal = chooser.showSaveDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    OutputStream out = null;
                    if (asZipFile.isSelected()) {
                        out = new ZipOutputStream(new FileOutputStream(chooser.getSelectedFile().getAbsolutePath() + File.separator + "Clusters_" + treatment + ".zip"));
                    }
                    Iterator<String> it = images.keySet().iterator();
                    while (it.hasNext()) {
                        String name = it.next();
                        File f = new File(chooser.getSelectedFile().getAbsolutePath() + File.separator + name + ".jpg");
                        if (asZipFile.isSelected()) {
                            ((ZipOutputStream) out).putNextEntry(new ZipEntry(f.getName()));
                        } else {
                            out = new BufferedOutputStream(new FileOutputStream(f));
                        }
                        if (group.getSelection().getActionCommand().equals("JPEG")) {
                            ChartUtilities.writeBufferedImageAsJPEG(out, images.get(name));
                        } else if (group.getSelection().getActionCommand().equals("PNG")) {
                            ChartUtilities.writeBufferedImageAsPNG(out, images.get(name));
                        }
                        if (asZipFile.isSelected()) {
                            ((ZipOutputStream) out).closeEntry();
                        } else {
                            out.close();
                        }
                    }
                    if (asZipFile.isSelected()) {
                        out.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
