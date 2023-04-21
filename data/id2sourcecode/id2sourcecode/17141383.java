    public static void saveAsPageFile() {
        PlotPage page = (PlotPage) OpenPlotTool.getMainFrame().getPlotPanel().getSelectedComponent();
        int index = OpenPlotTool.getMainFrame().getPlotPanel().getSelectedIndex();
        if (page != null) {
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(OpenPlotTool.getMainFrame());
            File saveFile = fc.getSelectedFile();
            if (saveFile != null) {
                String fileExtension = PreferenceHandler.getSettings().useCompressedFiles() ? "opc" : "opp";
                if (fileExtension.equals(getFileExtension(saveFile))) {
                } else if (getFileExtension(saveFile).equals("") && PreferenceHandler.getSettings().isAddFileExtensions()) {
                    saveFile = new File(saveFile.getAbsoluteFile() + "." + fileExtension);
                } else if (PreferenceHandler.getSettings().isCheckFileExtensions()) {
                    int chosen = JOptionPane.showConfirmDialog(OpenPlotTool.getMainFrame(), "The file extension you have used is not the recommened one.\n" + "Do you want to save with that extension anyway?", "Wrong File Extension", JOptionPane.YES_NO_OPTION);
                    if (chosen == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                if (saveFile.exists() && !(page.getPageFile().equals(saveFile))) {
                    int chosen = JOptionPane.showConfirmDialog(OpenPlotTool.getMainFrame(), "A file with the name you have given alreadt exists.\n" + "Do you wish to overwrite this file?", "Overwrite File", JOptionPane.YES_NO_OPTION);
                    if (chosen == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                performSave(page, saveFile);
                page.setPageFile(saveFile);
                OpenPlotTool.getMainFrame().getPlotPanel().setTitleAt(index, saveFile.getName());
                OpenPlotTool.getMainFrame().getPlotPanel().setToolTipTextAt(index, saveFile.getAbsolutePath());
                PageHandler.updatePageChange();
            }
        }
    }
