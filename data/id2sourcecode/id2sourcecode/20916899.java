    private void startMole() {
        setNumTasksCompleted(0);
        File rootDir = new File(currentDirectory.getText());
        if (rootDir == null || !rootDir.exists() || !rootDir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Image directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            currentDirectory.selectAll();
            return;
        }
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        infoExtractor = new ExtractorThread(this, rootDir, overwriteButton.isSelected());
        infoExtractor.start();
    }
