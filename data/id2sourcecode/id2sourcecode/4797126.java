    private boolean hasSaveCurrentContextAs() {
        if (contextPanes.size() == 0) return false;
        ContextTableScrollPane selectedPane = contextPanes.elementAt(currentContextIdx);
        Context currentContext = ((ContextTableModel) selectedPane.getContextTable().getModel()).getContext();
        JFileChooser fileChooser = new JFileChooser(LMPreferences.getLastDirectory());
        fileChooser.setApproveButtonText(GUIMessages.getString("GUI.save"));
        fileChooser.setDialogTitle(GUIMessages.getString("GUI.saveCurrentContext"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (currentContext instanceof ValuedContext) {
            ExampleFileFilter filterValued = new ExampleFileFilter("lmv", GUIMessages.getString("GUI.LatticeMinerValuedFormat"));
            fileChooser.addChoosableFileFilter(filterValued);
            fileChooser.setSelectedFile(new File(currentContext.getName() + GUIMessages.getString("GUI.LatticeMinerValuedFormatDefaultName")));
        } else if (currentContext instanceof NestedContext) {
            ExampleFileFilter filterNested = new ExampleFileFilter("lmn", GUIMessages.getString("GUI.LatticeMinerNestedFormat"));
            fileChooser.addChoosableFileFilter(filterNested);
            fileChooser.setSelectedFile(new File(((NestedContext) currentContext).getNestedContextName() + GUIMessages.getString("GUI.LatticeMinerNestedFormatDefaultName")));
        } else {
            ExampleFileFilter filterGaliciaBinSLF = new ExampleFileFilter("slf", GUIMessages.getString("GUI.galiciaSLFBinaryFormat"));
            fileChooser.addChoosableFileFilter(filterGaliciaBinSLF);
            ExampleFileFilter filterGaliciaBin = new ExampleFileFilter("bin.xml", GUIMessages.getString("GUI.galiciaXMLBinaryFormat"));
            fileChooser.addChoosableFileFilter(filterGaliciaBin);
            ExampleFileFilter filterCex = new ExampleFileFilter("cex", GUIMessages.getString("GUI.conceptExplorerBinaryFormat"));
            fileChooser.addChoosableFileFilter(filterCex);
            ExampleFileFilter filterBinary = new ExampleFileFilter("lmb", GUIMessages.getString("GUI.LatticeMinerBinaryFormat"));
            fileChooser.addChoosableFileFilter(filterBinary);
            fileChooser.setSelectedFile(new File(currentContext.getName() + GUIMessages.getString("GUI.LatticeMinerBinaryFormatDefaultName")));
        }
        int returnVal = fileChooser.showSaveDialog(panel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            boolean hasBeenClosed = false;
            if (currentContext.getContextFile() != null) {
                hasBeenClosed = hasClosedCurrentContext(false);
            }
            File fileName = fileChooser.getSelectedFile();
            ExampleFileFilter currentFilter = (ExampleFileFilter) fileChooser.getFileFilter();
            ArrayList<String> extensions = currentFilter.getExtensionsList();
            String oldFileType = ExampleFileFilter.getExtension(fileName);
            String newFileType = oldFileType;
            if (extensions != null && !extensions.contains(oldFileType)) {
                newFileType = extensions.get(0);
                String oldFileName = fileName.getAbsolutePath();
                int posOldExt = oldFileName.lastIndexOf(".");
                String newFileName = oldFileName + "." + newFileType;
                if (posOldExt != -1) newFileName = newFileName.substring(0, posOldExt) + "." + newFileType;
                fileName = new File(newFileName);
            }
            if (fileName.exists()) {
                int overwrite = DialogBox.showDialogWarning(panel, GUIMessages.getString("GUI.doYouWantToOverwriteFile"), GUIMessages.getString("GUI.selectedFileAlreadyExist"));
                if (overwrite == DialogBox.NO) {
                    DialogBox.showMessageInformation(panel, GUIMessages.getString("GUI.contextHasNotBeenSaved"), GUIMessages.getString("GUI.notSaved"));
                    return false;
                }
            }
            saveDependingOnFileType(fileName, currentContext);
            LMPreferences.setLastDirectory(fileChooser.getCurrentDirectory().getAbsolutePath());
            if (hasBeenClosed) {
                openContextFile(fileName);
            }
            return true;
        } else {
            return false;
        }
    }
