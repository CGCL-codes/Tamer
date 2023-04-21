    public void savePlotAs() {
        if (ViewManager.getSelectedPlot() != null) {
            Plot plot = ViewManager.getSelectedPlot();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new PlotFileFilter());
            int returnValue = fileChooser.showSaveDialog(OpenPlotTool.getInstance().getMainFrame());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                if (FileUtils.getExtension(saveFile).equals("")) {
                    saveFile = new File(saveFile.getAbsoluteFile() + ".opf");
                }
                if (saveFile.exists()) {
                    int n = JOptionPane.showConfirmDialog(OpenPlotTool.getInstance().getMainFrame(), "File already exists.\n Are you sure you wish to overwrite it?", "Overwrite", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                try {
                    ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(saveFile));
                    objectOutput.writeObject(plot);
                    objectOutput.close();
                    plot.setPlotFile(saveFile);
                    ViewManager.updateTabText();
                    plot.setEdited(false);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(OpenPlotTool.getInstance().getMainFrame(), "Failed to save plot:\n" + e.getMessage(), "File Not Found Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(OpenPlotTool.getInstance().getMainFrame(), "Failed to save plot:\n" + e.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
