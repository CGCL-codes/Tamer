        @Override
        public void actionPerformed(ActionEvent e) {
            this.tf_height.setBackground(Color.WHITE);
            this.tf_width.setBackground(Color.WHITE);
            this.tf_bgmPath.setBackground(Color.WHITE);
            this.tf_imagePath.setBackground(Color.WHITE);
            this.tf_savePath.setBackground(Color.WHITE);
            this.tf_mapName.setBackground(Color.WHITE);
            this.tf_tw.setBackground(Color.WHITE);
            this.tf_th.setBackground(Color.WHITE);
            this.btn_generate.setBackground(this.standardBtnBaground);
            this.btn_update.setBackground(this.standardBtnBaground);
            if (e.getSource() == this.btn_findImage) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File e) {
                        if (e.isDirectory()) return true; else {
                            String name = e.getPath().toLowerCase();
                            return (name.matches(".*\\.png") || name.matches(".*\\.jpg") || name.matches(".*\\.jpeg") || name.matches(".*\\.gif"));
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "Image files ( .png / .jpg / .jpeg / .gif )";
                    }
                });
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) this.tf_imagePath.setText(fc.getSelectedFile().getPath());
            } else if (e.getSource() == this.btn_findSave) {
                if (mapW.mapName == "") {
                    this.tf_mapName.setBackground(DISABLED_COLOR);
                    this.btn_update.setBackground(DISABLED_COLOR);
                    return;
                }
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File e) {
                        if (e.isDirectory()) return true; else {
                            return e.getPath().toLowerCase().matches(".*\\.shmap ");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "Shinigami Map Files ( .shmap )";
                    }
                });
                int result = fc.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    if (!f.isDirectory()) {
                        int option = JOptionPane.showConfirmDialog(null, "File already exists. Overwrite?");
                        if (option != JOptionPane.OK_OPTION) return;
                    }
                    String s = f.getPath();
                    if (!s.matches(".*\\.shmap")) s = s + ".shmap";
                    this.tf_savePath.setText(fc.getSelectedFile().getPath() + File.separator + mapW.mapName + ".shmap");
                }
            } else if (e.getSource() == this.btn_update) {
                boolean allRight = true;
                if (tf_mapName.getText().contains(" ") || tf_mapName.getText().equals("")) {
                    tf_mapName.setBackground(DISABLED_COLOR);
                    allRight = false;
                }
                int width = 0, height = 0;
                try {
                    width = Integer.parseInt(tf_width.getText());
                } catch (NumberFormatException exception) {
                    this.tf_width.setBackground(DISABLED_COLOR);
                    allRight = false;
                }
                try {
                    height = Integer.parseInt(tf_height.getText());
                } catch (NumberFormatException exception) {
                    this.tf_height.setBackground(DISABLED_COLOR);
                    allRight = false;
                }
                int tw = 0, th = 0;
                try {
                    tw = Integer.parseInt(tf_tw.getText());
                } catch (NumberFormatException exception) {
                    this.tf_tw.setBackground(DISABLED_COLOR);
                    allRight = false;
                }
                try {
                    th = Integer.parseInt(tf_th.getText());
                } catch (NumberFormatException exception) {
                    this.tf_th.setBackground(DISABLED_COLOR);
                    allRight = false;
                }
                try {
                    ImageIO.read(new File(tf_imagePath.getText()));
                } catch (IOException exception) {
                    this.tf_imagePath.setBackground(DISABLED_COLOR);
                    allRight = false;
                }
                if (!tf_bgmPath.getText().equals("")) {
                    try {
                        Sound.create(tf_bgmPath.getText());
                    } catch (Exception exception) {
                        this.tf_bgmPath.setBackground(DISABLED_COLOR);
                        allRight = false;
                    }
                }
                if (allRight) {
                    File sound = null;
                    if (!tf_bgmPath.getText().equals("")) sound = new File(tf_bgmPath.getText());
                    mapW.updateState(tf_mapName.getText(), new File(tf_imagePath.getText()), sound, width, height, tw, th);
                }
            } else if (e.getSource() == this.btn_generate) {
                tf_mapName.setEnabled(false);
                tf_width.setEnabled(false);
                tf_height.setEnabled(false);
                tf_savePath.setEnabled(false);
                tf_imagePath.setEnabled(false);
                tf_tw.setEnabled(false);
                tf_th.setEnabled(false);
                btn_update.setEnabled(false);
                btn_findImage.setEnabled(false);
                btn_findSave.setEnabled(false);
                btn_generate.setEnabled(false);
                if (mapW.createFile(new File(this.tf_savePath.getText()), new File(this.tf_imagePath.getText()))) {
                    JOptionPane.showMessageDialog(null, "File Created");
                } else this.tf_savePath.setBackground(DISABLED_COLOR);
                tf_mapName.setEnabled(true);
                tf_width.setEnabled(true);
                tf_height.setEnabled(true);
                tf_savePath.setEnabled(true);
                tf_imagePath.setEnabled(true);
                tf_tw.setEnabled(true);
                tf_th.setEnabled(true);
                btn_update.setEnabled(true);
                btn_findImage.setEnabled(true);
                btn_findSave.setEnabled(true);
                btn_generate.setEnabled(true);
            } else if (e.getSource() == mi_open) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File e) {
                        if (e.isDirectory()) return true; else return e.getPath().toLowerCase().matches(".*\\.shmap");
                    }

                    @Override
                    public String getDescription() {
                        return "Shinigami Map Files ( .shmap )";
                    }
                });
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    MapScope s = MapScope.createScope(new MapFile(fc.getSelectedFile().getPath()));
                    this.tf_mapName.setText(s.getMapName());
                    this.tf_width.setText("" + s.getWidth());
                    this.tf_height.setText("" + s.getHeight());
                    this.tf_tw.setText("" + s.getTileWidth());
                    this.tf_th.setText("" + s.getTileHeight());
                    this.tf_imagePath.setText("" + s.getBackgroundFile().getPath());
                    this.tf_savePath.setText(fc.getSelectedFile().getPath());
                    mapW.updateState(s.getMapName(), s.getBackgroundFile(), s.getBGM(), s.getWidth(), s.getHeight(), s.getTileWidth(), s.getTileHeight());
                    for (int i = 0; i < mapW.nodes.length; i++) for (int k = 0; k < mapW.nodes[i].length; k++) for (int m = 0; m < mapW.nodes[i][k].length; m++) mapW.nodes[i][k][m].accessible = s.getPassage(i, k, m);
                    for (PortalScope ps : s.getPortals()) mapW.portals.add(new GenPortal(ps.TX, ps.TY, ps.TARGET, ps.TTX, ps.TTY));
                    mapW.imX = s.getBackgroundAdjustX();
                    mapW.imY = s.getBackgroundAdjustY();
                }
            } else if (e.getSource() == mi_about) {
                CREDITS.setVisible(true);
            } else if (e.getSource() == this.btn_findBGM) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File e) {
                        if (e.isDirectory()) return true; else {
                            String name = e.getPath().toLowerCase();
                            return (name.matches(".*\\.mid") || name.matches(".*\\.mp3"));
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "Sound files ( .mp3 / .mid )";
                    }
                });
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    this.tf_bgmPath.setText(fc.getSelectedFile().getPath());
                }
            } else if (e.getSource() == this.btn_play) {
                if (playingSound == null) {
                    playingSound = Sound.create(this.tf_bgmPath.getText());
                    playingSound.play();
                    this.btn_play.setText("Stop");
                } else {
                    playingSound.stop();
                    playingSound = null;
                    this.btn_play.setText("Play");
                }
            }
        }
