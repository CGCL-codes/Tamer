            public void actionPerformed(ActionEvent e) {
                classPane.closeMenus();
                createFileChooser();
                fileChooser.setFileFilter(new FileFilter() {

                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith(".udc");
                    }

                    public String getDescription() {
                        return "*.udc";
                    }
                });
                if (fileChooser.showOpenDialog(WorkspaceToolBarMenu.this) == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    try {
                        InputStream in = new BufferedInputStream(new InflaterInputStream(new FileInputStream(f)));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        for (int count; (count = in.read(bytes)) >= 0; ) {
                            out.write(bytes, 0, count);
                        }
                        Workspace.setXMLDescription(classPane, new String(out.toByteArray(), "UTF-8"));
                        in.close();
                    } catch (Exception ex) {
                        try {
                            InputStream in = new BufferedInputStream(new FileInputStream(f));
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] bytes = new byte[1024];
                            for (int count; (count = in.read(bytes)) >= 0; ) {
                                out.write(bytes, 0, count);
                            }
                            Workspace.setXMLDescription(classPane, new String(out.toByteArray(), "UTF-8"));
                            in.close();
                        } catch (Exception ex2) {
                            ex2.printStackTrace();
                        }
                    }
                }
            }
