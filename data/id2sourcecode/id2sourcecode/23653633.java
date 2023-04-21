    private void saveGraph() {
        JFileChooser fileChooser = getFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File packageFile = fileChooser.getSelectedFile();
            String basePath = packageFile.getParent();
            String baseName = FileUtils.removeExtension(packageFile);
            try {
                File graphFile = new File(baseName + ".graph.xml");
                if (backgroundFile != null) {
                    graphView.getGraph().setWidth(graphView.getBackgroundImage().getWidth(this));
                    graphView.getGraph().setHeight(graphView.getBackgroundImage().getHeight(this));
                }
                graphView.getGraph().save(graphFile.getAbsolutePath());
                String imageFileName = null;
                if (backgroundFile != null) {
                    File imageFile = new File(backgroundFile);
                    imageFileName = imageFile.getName();
                    if (!imageFile.getParent().equals(basePath)) {
                        File destFile = new File(basePath + "/" + imageFile.getName());
                        FileUtils.copyFile(imageFile, destFile);
                    }
                }
                GraphPackage graphPackage = new GraphPackage(imageFileName, graphFile.getName());
                graphPackage.save(packageFile.getAbsolutePath());
            } catch (Exception e) {
                UIUtils.showErrorMessage(this, e);
            }
        }
    }
