    public boolean setHardDriveParams() {
        ModuleATA ata = (ATA) modules.getModule(Module.Type.ATA);
        int numFixedDisks = moduleConfig.getAta().getHarddiskdrive().size();
        logger.log(Level.INFO, "Configuring " + numFixedDisks + " fixed disks");
        if (moduleConfig.getAta().getHarddiskdrive().isEmpty()) {
            return false;
        }
        for (int i = 0; i < numFixedDisks; i++) {
            Harddiskdrive hddConfig = moduleConfig.getAta().getHarddiskdrive().get(i);
            boolean enabled = hddConfig.isEnabled();
            int ideChannelIndex = hddConfig.getChannelindex().intValue();
            boolean isMaster = hddConfig.isMaster();
            boolean autoDetectCylinders = hddConfig.isAutodetectcylinders();
            int numCylinders = hddConfig.getCylinders().intValue();
            int numHeads = hddConfig.getHeads().intValue();
            int numSectorsPerTrack = hddConfig.getSectorspertrack().intValue();
            String imageFilePath = Utilities.resolvePathAsString(hddConfig.getImagefilepath());
            if (enabled && ideChannelIndex >= 0 && ideChannelIndex < 4) {
                if (autoDetectCylinders) {
                    numCylinders = 0;
                }
                ata.initConfig(ideChannelIndex, isMaster, true, false, numCylinders, numHeads, numSectorsPerTrack, ATATranslationType.AUTO, imageFilePath);
                if (ideChannelIndex == 0 && i == 0) {
                    getGui().updateGUI(GUI.EMU_HD1_INSERT);
                }
                if (ideChannelIndex == 0 && i == 1) {
                    getGui().updateGUI(GUI.EMU_HD2_INSERT);
                }
            }
        }
        return true;
    }
