            public void actionPerformed(ActionEvent e) {
                int ret = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(ImageButton.this));
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File imageDir = new File("img");
                    File selectedFile = fileChooser.getSelectedFile();
                    File newFile = new File(imageDir, selectedFile.getName());
                    try {
                        if (!selectedFile.getCanonicalPath().startsWith(imageDir.getCanonicalPath())) {
                            FileUtils.copyFile(selectedFile, newFile);
                        }
                    } catch (IOException ex) {
                        LOGGER.log(Level.WARNING, "", ex);
                    }
                    imageLocation = imageDir.toURI().relativize(newFile.toURI()).getPath();
                    imageLocationField.setText(imageLocation);
                    canvas.setTheme(new Theme(canvas.getTheme().getFont(), canvas.getTheme().getFontColor(), new Background(imageLocation, null)));
                }
            }
