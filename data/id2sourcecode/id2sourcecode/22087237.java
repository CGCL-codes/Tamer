    public boolean saveMap(File f) {
        if (f == null) return false;
        try {
            writeBytes(MapFormatReader.read(mapFormat.currentFormat, this), f.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "IOException saving map " + f.getName() + "!\n" + "Details: " + e);
            return false;
        }
        mapPane.saved = true;
        setTitle("Map Editor: " + (mapPane.mapFileName = f.getAbsolutePath()));
        return true;
    }
