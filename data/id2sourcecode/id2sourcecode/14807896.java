    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        int selectedRow = tilesetTable.getSelectedRow();
        List<TileSet> tilesets = map.getTilesets();
        TileSet set = null;
        try {
            set = tilesets.get(selectedRow);
        } catch (IndexOutOfBoundsException e) {
        }
        if (command.equals("Close")) {
            dispose();
        } else if (command.equals("Edit...")) {
            if (map != null && selectedRow >= 0) {
                TileDialog tileDialog = new TileDialog(this, set);
                tileDialog.setVisible(true);
            }
        } else if (command.equals("Remove")) {
            try {
                if (checkSetUsage(set) > 0) {
                    int ret = JOptionPane.showConfirmDialog(this, "This tileset is currently in use. " + "Are you sure you wish to remove it?", "Sure?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (ret == JOptionPane.YES_OPTION) {
                        map.removeTileset(set);
                        updateTilesetTable();
                    }
                } else {
                    map.removeTileset(set);
                    updateTilesetTable();
                }
            } catch (ArrayIndexOutOfBoundsException a) {
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else if (command.equals("Save as...")) {
            JFileChooser ch = new JFileChooser(map.getFilename());
            MapWriter writers[] = PluginClassLoader.getInstance().getWriters();
            for (int i = 0; i < writers.length; i++) {
                try {
                    ch.addChoosableFileFilter(new TiledFileFilter(writers[i].getFilter(), writers[i].getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ch.addChoosableFileFilter(new TiledFileFilter(TiledFileFilter.FILTER_TSX));
            int ret = ch.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                String filename = ch.getSelectedFile().getAbsolutePath();
                File exist = new File(filename);
                if ((exist.exists() && JOptionPane.showConfirmDialog(this, "The file already exists. Do you wish to overwrite it?") == JOptionPane.OK_OPTION) || !exist.exists()) {
                    try {
                        MapHelper.saveTileset(set, filename);
                        set.setSource(filename);
                        embedButton.setEnabled(true);
                        saveButton.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (command.equals("Save")) {
            try {
                MapHelper.saveTileset(set, set.getSource());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (command.equals("Embed")) {
            set.setSource(null);
            embedButton.setEnabled(false);
            saveButton.setEnabled(false);
        } else {
            System.out.println("Unimplemented command: " + command);
        }
    }
