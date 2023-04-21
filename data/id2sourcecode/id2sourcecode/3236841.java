    private void saveFromMetaDataActionPerformed(ActionEvent evt) throws FileNotFoundException, IOException {
        int selectedOption = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to replicate the MetaData into two sample set?\nTip: This would reduce the accuracy of verification by half.", "Are you sure?", JOptionPane.OK_CANCEL_OPTION);
        if (selectedOption == JOptionPane.CANCEL_OPTION) {
            return;
        }
        String datapath = preferences.get("datapath", "C:\\Signit\\Data");
        String folderName = datapath + "\\" + efirstNameTextField.getText() + "_" + elastNameTextField.getText() + "_" + eidTextField.getText();
        File file = new File(folderName);
        if (file.exists()) {
            throw new RuntimeException("[exception] User already exists. Cannot overwrite.");
        }
        file.mkdir();
        for (int i = 1; i <= 2; i++) {
            FileOutputStream fOS = new FileOutputStream(folderName + "\\" + i);
            PrintWriter pw = new PrintWriter(fOS);
            pw.println(Formatter.listToString(signatureReadFromMetaData.getX(), SignItConstants.DELIMITER));
            pw.println(Formatter.listToString(signatureReadFromMetaData.getY(), SignItConstants.DELIMITER));
            pw.println(Formatter.byteArrayToString(signatureReadFromMetaData.getDigest()));
            pw.close();
            fOS.close();
        }
    }
