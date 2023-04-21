    public boolean save(boolean saveAs) {
        File file = saveAs ? null : environment.getFile();
        Codec codec = (Codec) environment.getEncoder();
        Serializable object = environment.getObject();
        boolean badname = false;
        if (file != null && (codec == null || !codec.canEncode(object))) {
            JOptionPane.showMessageDialog(this, "We cannot write this structure in the same format\n" + "it was read as!  Use Save As to select a new format.", "IO Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        FileFilter[] filters = Universe.CHOOSER.getChoosableFileFilters();
        for (int i = 0; i < filters.length; i++) Universe.CHOOSER.removeChoosableFileFilter(filters[i]);
        List encoders = Universe.CODEC_REGISTRY.getEncoders(object);
        Iterator it = encoders.iterator();
        while (it.hasNext()) Universe.CHOOSER.addChoosableFileFilter((FileFilter) it.next());
        if (codec != null && codec.canEncode(object)) {
            Universe.CHOOSER.setFileFilter(codec);
        } else {
            Universe.CHOOSER.setFileFilter((FileFilter) encoders.get(0));
        }
        if (file != null && codec != null) {
            String filename = file.getName();
            String newname = codec.proposeFilename(filename, object);
            if (!filename.equals(newname)) {
                int result = JOptionPane.showConfirmDialog(this, "To save as a " + codec.getDescription() + ",\n" + "JFLAP wants to save " + filename + " to a new file\n" + "named " + newname + ".  Is that OK?");
                switch(result) {
                    case JOptionPane.CANCEL_OPTION:
                        return false;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.YES_OPTION:
                        file = new File(file.getParent(), newname);
                        badname = true;
                }
            }
        }
        while (badname || file == null) {
            if (!badname) {
                int result = Universe.CHOOSER.showSaveDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) return false;
                file = Universe.CHOOSER.getSelectedFile();
                String filename = file.getName();
                codec = (Codec) Universe.CHOOSER.getFileFilter();
                file = new File(file.getParent(), codec.proposeFilename(filename, object));
            }
            badname = false;
            if (file.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "Overwrite " + file.getName() + "?");
                switch(result) {
                    case JOptionPane.CANCEL_OPTION:
                        return false;
                    case JOptionPane.NO_OPTION:
                        file = null;
                        continue;
                    default:
                }
            }
        }
        System.out.println("CODEC: " + codec.getDescription());
        Universe.CHOOSER.resetChoosableFileFilters();
        try {
            codec.encode(object, file, null);
            environment.setFile(file);
            environment.setEncoder(codec);
            environment.clearDirty();
            return true;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Write Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
