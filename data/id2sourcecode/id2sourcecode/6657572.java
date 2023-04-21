    private void addButton() {
        if ("".equals(pc.getFileName())) {
            ShowMessageDelegate.showMessageDialog("You must save the current character first", Constants.s_APPNAME, MessageType.ERROR);
            return;
        }
        TreePath avaCPath = availableTable.getTree().getSelectionPath();
        TreePath selCPath = selectedTable.getTree().getSelectionPath();
        String target;
        if (selCPath == null) {
            ShowMessageDelegate.showMessageDialog("First select destination", Constants.s_APPNAME, MessageType.ERROR);
            return;
        }
        target = selCPath.getPathComponent(1).toString();
        Object endComp = avaCPath.getLastPathComponent();
        PObjectNode fNode = (PObjectNode) endComp;
        if ((fNode.getItem() instanceof Race)) {
            Race aRace = (Race) fNode.getItem();
            if (aRace == null) {
                return;
            }
            String nName;
            String aType;
            Logging.errorPrint("addButton:race: " + aRace.getName() + " -> " + target);
            Object nValue = JOptionPane.showInputDialog(null, "Please enter a name for new " + target + ":", Constants.s_APPNAME, JOptionPane.QUESTION_MESSAGE);
            if (nValue != null) {
                nName = ((String) nValue).trim();
            } else {
                return;
            }
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save new " + target + " named: " + nName);
            fc.setSelectedFile(new File(SettingsHandler.getPcgPath(), nName + Constants.s_PCGEN_CHARACTER_EXTENSION));
            fc.setCurrentDirectory(SettingsHandler.getPcgPath());
            if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = fc.getSelectedFile();
            if (!PCGFile.isPCGenCharacterFile(file)) {
                file = new File(file.getParent(), file.getName() + Constants.s_PCGEN_CHARACTER_EXTENSION);
            }
            if (file.exists()) {
                int iConfirm = JOptionPane.showConfirmDialog(null, "The file " + file.getName() + " already exists. Are you sure you want to overwrite it?", "Confirm OverWrite", JOptionPane.YES_NO_OPTION);
                if (iConfirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            PlayerCharacter newPC = new PlayerCharacter();
            newPC.setName(nName);
            newPC.setFileName(file.getAbsolutePath());
            for (Iterator i = newPC.getStatList().getStats().iterator(); i.hasNext(); ) {
                final PCStat aStat = (PCStat) i.next();
                aStat.setBaseScore(10);
            }
            newPC.setAlignment(pc.getAlignment(), true, true);
            newPC.setRace(aRace);
            if (newPC.getRace().hitDice(pc) != 0) {
                newPC.getRace().rollHP(pc);
            }
            newPC.setDirty(true);
            aType = target;
            Follower newMaster = new Follower(pc.getFileName(), pc.getName(), aType);
            newPC.setMaster(newMaster);
            Follower newFollower = new Follower(file.getAbsolutePath(), nName, aType);
            newFollower.setRace(newPC.getRace().getName());
            pc.addFollower(newFollower);
            pc.setDirty(true);
            pc.setCalcFollowerBonus(pc);
            pc.setAggregateFeatsStable(false);
            pc.setVirtualFeatsStable(false);
            ShowMessageDelegate.showMessageDialog("Saving " + nName + " and switching tabs", Constants.s_APPNAME, MessageType.INFORMATION);
            try {
                (new PCGIOHandler()).write(newPC, file.getAbsolutePath());
            } catch (Exception ex) {
                ShowMessageDelegate.showMessageDialog("Could not save " + newPC.getDisplayName(), Constants.s_APPNAME, MessageType.ERROR);
                Logging.errorPrint("Could not save " + newPC.getDisplayName(), ex);
                return;
            }
            setNeedsUpdate(true);
            pc.calcActiveBonuses();
            PlayerCharacter loadedChar = PCGen_Frame1.getInst().loadPCFromFile(file);
            loadedChar.calcActiveBonuses();
            CharacterInfo pane = PCGen_Frame1.getCharacterPane();
            pane.setPaneForUpdate(pane.infoSummary());
            pane.refresh();
        } else if ((fNode.getItem() instanceof Equipment)) {
            Equipment eqI = (Equipment) fNode.getItem();
            if (eqI == null) {
                return;
            }
            Logging.errorPrint("addButton:item: " + eqI.getName() + " -> " + target);
            pc.setDirty(true);
            updateSelectedModel();
        }
    }
