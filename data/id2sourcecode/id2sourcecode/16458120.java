    private void saveSummaryAs() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(Extensions.XSUM_FILE_FILTER);
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedfile = chooser.getSelectedFile();
                if (!selectedfile.exists() || JOptionPane.showConfirmDialog(this, selectedfile.getName() + " already exists. Overwriter file?", "File already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    SummaryWriter writer = new SummaryXMLWriter(file = selectedfile);
                    Iterator<CompleteSummary> iterator = summaries.getAllSummaries().iterator();
                    while (iterator.hasNext()) {
                        writer.writeSummary(iterator.next());
                    }
                    writer.closeWriter();
                    dirty = false;
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "An error occurred while summarizing the file: " + exception.toString());
            exception.printStackTrace();
        }
    }
