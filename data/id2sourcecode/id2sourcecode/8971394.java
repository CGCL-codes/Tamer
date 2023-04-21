    private void saveSummary() {
        if (!personality.issetME()) {
            JOptionPane.showMessageDialog(this, "Please fill in the obserer (me)", "No observer specified", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setFileFilter(Extensions.XSUM_FILE_FILTER);
                try {
                    StringBuilder titname = new StringBuilder();
                    titname.append(personality.getRN());
                    titname.append("_");
                    titname.append(personality.getSEQ());
                    titname.append("_");
                    titname.append(Dates.SAVE_DATE_FORMAT.format(personality.getDATE()));
                    titname.append(".xsum");
                    chooser.setSelectedFile(new File(titname.toString()));
                } catch (AttributeNotSetException exception) {
                }
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File selectedfile = chooser.getSelectedFile();
                    if (!selectedfile.exists() || JOptionPane.showConfirmDialog(this, selectedfile.getName() + " already exists. Overwriter file?", "File already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        SuperSummarizer summarizer = new SuperSummarizer(Configurations.getSummaryIntervals(), Configurations.getDefaultFlyTime());
                        observation.produceObservation(summarizer);
                        SummaryWriter writer = new SummaryXMLWriter(selectedfile);
                        Summary[] summaries = summarizer.getSummaries();
                        for (int i = 0; i < summaries.length; i++) {
                            writer.writeSummary(summaries[i], personality);
                        }
                        writer.closeWriter();
                    }
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "An error occurred while summarizing the file: " + exception.toString());
                exception.printStackTrace();
            }
        }
    }
