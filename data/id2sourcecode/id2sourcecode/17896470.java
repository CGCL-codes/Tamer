    private void extractButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if ((!inputField.getText().equals(NO_FILE_SELECTED)) && (!outputField.getText().equals(NO_FILE_SELECTED))) {
            progressLabel.setBackground(INFO);
            progressLabel.setText(EXTRACTING);
            progressLabel.validate();
            try {
                writeFile = new File(outputField.getText());
                Extractor extractor = new Extractor(readFile, writeFile);
                if (extractImagesChckbx.isSelected()) extractor.extractImages(readFile, writeFile);
                progressLabel.setBackground(SUCCEED);
                progressLabel.setText(EXTARCT_DONE);
            } catch (IOException iox) {
                System.out.println(ERROR_EXTRACT);
                iox.printStackTrace();
                progressLabel.setBackground(WARNING);
                progressLabel.setText(ERROR_EXTRACT + iox.getMessage());
            }
        } else {
            progressLabel.setBackground(WARNING);
            progressLabel.setText(MUST_SELECT_FILE);
        }
    }
