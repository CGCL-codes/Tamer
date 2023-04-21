    protected void SaveMosaic() {
        final JFileChooser chooser = new JFileChooser(InputImageText.getText());
        chooser.setFileFilter(new WosaicFilter());
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        final File theFile = chooser.getSelectedFile();
        if (theFile.exists()) if (JOptionPane.showConfirmDialog(this, theFile.getName() + " already exists.  Overwrite?", "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        final Dimension d = checkDimensions(GeneratedMosaic.getParams().originalWidth, GeneratedMosaic.getParams().originalHeight);
        if (d == null) return;
        GeneratedMosaic.setOutputSize(d.width, d.height);
        StatusUI.setIndeterminate(true);
        StatusUI.setStatus("Saving...");
        final SaveThread st = new SaveThread(GeneratedMosaic, StatusUI, theFile);
        final Thread saveThread = new Thread(st);
        saveThread.start();
        System.gc();
    }
