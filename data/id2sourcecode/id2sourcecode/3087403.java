    private void createAndSavePresetPackage() throws PackageGenerationException {
        PresetPackage pkg = null;
        File extFile = null;
        synchronized (this.getClass()) {
            assertChooser();
            fc.setSelectedFile(new File(packageNameField.getText()));
            int retval = fc.showSaveDialog(ZoeosFrame.getInstance());
            if (retval == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                extFile = ZUtilities.replaceExtension(f, PresetPackage.PRESET_PKG_EXT);
                if (extFile.exists() && JOptionPane.showConfirmDialog(ZoeosFrame.getInstance(), "Overwrite " + extFile.getName() + " ?", "File Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 1) return;
                ZPREF_lastDir.putValue(extFile.getAbsolutePath());
            }
        }
        if (extFile != null) {
            ProgressSession sess = ZoeosFrame.getInstance().getProgressSession("Creating preset package", 100, false);
            sess.setIndeterminate(true);
            try {
                pkg = PackageFactory.createPresetPackage(pc, PresetContextMacros.extractPresetIndexes(incPresets), packageNameField.getText(), packageNotesField.getText(), ZPREF_incMaster.getValue(), ZPREF_incMultimode.getValue(), incSamplesCheck.isEnabled() && ZPREF_incSamples.getValue(), ZAudioSystem.getDefaultAudioType(), null);
            } finally {
                sess.end();
            }
            PackageFactory.savePresetPackage(pkg, extFile, new ProgressCallbackTree("Saving preset package", false).splitTask(1, false)[0]);
        }
    }
