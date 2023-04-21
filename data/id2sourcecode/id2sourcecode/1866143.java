    public void exportData(List<String> channels) {
        for (int i = channels.size() - 1; i >= 0; i--) {
            Channel channel = RBNBController.getInstance().getChannel(channels.get(i));
            String mime = channel.getMetadata("mime");
            if (!mime.equals("application/octet-stream")) {
                channels.remove(i);
            }
        }
        if (channels == null || channels.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no data channels selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> fileFormats = Arrays.asList(new String[] { "ASCII", "MATLAB" });
        ExportDialog dialog = ExportDialog.showDialog(channels, fileFormats);
        if (dialog == null) {
            return;
        }
        DataFileWriter writer;
        if (dialog.getFileFormat().equals("MATLAB")) {
            writer = new MATFileWriter();
        } else {
            writer = new ASCIIDataFileWriter();
        }
        exportData(dialog.getSelectedChannels(), dialog.getStartTime(), dialog.getEndTime(), dialog.getFile(), writer);
    }
