    private JButton buildDownloadButton() throws IOException {
        JButton downloadButton = buildActionItemButton("Download Current Song", "download.png");
        downloadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser downloadFileChooser = new JFileChooser(new File("/"));
                if (downloadFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File saveToFile = downloadFileChooser.getSelectedFile();
                    try {
                        FileUtils.copyFile(new File("tempSongFile.aac"), saveToFile);
                    } catch (IOException e) {
                        ErrorDialogBuilder.showErrorDialog("Failed To Download File!", e);
                    }
                }
            }
        });
        return downloadButton;
    }
