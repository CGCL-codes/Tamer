    private void createAndSaveSamplePackage(SampleContext sc, Integer[] indexes, String name, String notes) throws CommandFailedException, PackageGenerationException {
        try {
            if (!sc.getDeviceContext().isSmdiCoupled()) throw new CommandFailedException("Cannot retrieve samples for the package- the device is not SMDI coupled.");
        } catch (DeviceException e) {
            throw new CommandFailedException(e.getMessage());
        }
        File extFile = null;
        synchronized (this.getClass()) {
            assertChooser();
            fc.setSelectedFile(new File(name));
            int retval = fc.showSaveDialog(ZoeosFrame.getInstance());
            if (retval == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                extFile = ZUtilities.replaceExtension(f, SamplePackage.SAMPLE_PKG_EXT);
                if (extFile.exists() && JOptionPane.showConfirmDialog(ZoeosFrame.getInstance(), "Overwrite " + extFile.getName() + " ?", "File Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 1) return;
                ZPREF_lastDir.putValue(extFile.getAbsolutePath());
            }
        }
        SamplePackage pkg = null;
        try {
            pkg = PackageFactory.createSamplePackage(sc, indexes, name, notes, null, ZAudioSystem.getDefaultAudioType());
        } catch (PackageGenerationException e) {
            throw new CommandFailedException("Error saving sample package: " + e.getMessage());
        }
        if (extFile != null) {
            final SamplePackage f_pkg = pkg;
            final File f_extFile = extFile;
            ProgressCallbackTree prog = new ProgressCallbackTree("Writing sample package", false);
            try {
                PackageFactory.saveSamplePackage(f_pkg, f_extFile, prog);
            } finally {
                prog.updateProgress(1);
            }
        }
    }
