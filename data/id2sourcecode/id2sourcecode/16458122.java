    private void exportSummaryAsSAS() {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter a name for the observation", "Enter a name", JOptionPane.QUESTION_MESSAGE);
            if (name != null) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setFileFilter(Extensions.SAS_FILE_FILTER);
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File selectedfile = chooser.getSelectedFile();
                    if (!selectedfile.exists() || JOptionPane.showConfirmDialog(this, selectedfile.getName() + " already exists. Overwriter file?", "File already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        SummaryWriter writer = new SummarySASWriter(new PrintStream(selectedfile), name);
                        Iterator<CompleteSummary> iterator = summaries.getAllSummaries().iterator();
                        while (iterator.hasNext()) {
                            writer.writeSummary(iterator.next());
                        }
                        writer.closeWriter();
                    }
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "An error occurred while summarizing the file: " + exception.toString());
            exception.printStackTrace();
        }
    }
