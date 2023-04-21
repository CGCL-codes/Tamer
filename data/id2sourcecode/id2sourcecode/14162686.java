            public void actionPerformed(ActionEvent event) {
                try {
                    String text = statsTextView.getText();
                    fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), "Untitled.txt"));
                    int status = fileChooser.showSaveDialog(MPSWindow.this);
                    switch(status) {
                        case JFileChooser.APPROVE_OPTION:
                            File selectedFile = fileChooser.getSelectedFile();
                            if (selectedFile.exists()) {
                                int confirm = displayConfirmDialog("Warning", "The selected file:  " + selectedFile + " already exists! \n Overwrite selection?");
                                if (confirm == NO_OPTION) return;
                            } else {
                                selectedFile.createNewFile();
                            }
                            FileWriter writer = new FileWriter(selectedFile);
                            writer.write(text, 0, text.length());
                            writer.flush();
                            break;
                        default:
                            break;
                    }
                } catch (Exception exception) {
                    displayError("Save Error", "Error saving file: ", exception);
                }
            }
