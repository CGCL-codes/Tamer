    private void jButton_signPDF() {
        String certName = (String) jList_certList.getSelectedValue();
        if (certName == null) {
            showMessageBox("selectCert");
            return;
        }
        if (jTextField_inputPath.getText().equalsIgnoreCase("") || jTextField_outputPath.getText().equalsIgnoreCase("")) {
            showMessageBox("selectDocumentFile");
            return;
        }
        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(jTextField_inputPath.getText()));
        } catch (FileNotFoundException e) {
            showMessageError(jTextField_inputPath.getText(), "documentFileNotFound");
            e.printStackTrace();
            return;
        }
        OutputStream outStream = null;
        OutputStream pdfOutStream = null;
        try {
            outStream = new FileOutputStream(jTextField_outputPath.getText());
        } catch (FileNotFoundException e) {
            showMessageError("documentCannotCreate");
            e.printStackTrace();
            return;
        }
        try {
            pdfOutStream = signer.signPDF(inStream, certName, jPasswordField_certPasswd.getText(), contentType, jTextField_URL.getText(), jComboBox_position.getSelectedIndex() + 1);
        } catch (FileNotFoundException fe) {
            showMessageError("documentFileNotFound");
        } catch (IOException io) {
            showMessageError("networkError");
        } catch (Exception se) {
            se.printStackTrace();
            Throwable root = se;
            while (root.getCause() != null && root.getCause() != root) root = root.getCause();
            JOptionPane.showMessageDialog(null, signaturaProperties.getProperty("signatureError") + se.toString(), "Govern Balear: Signatura Digital", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outStream.write(((ByteArrayOutputStream) pdfOutStream).toByteArray());
        } catch (IOException e1) {
            e1.printStackTrace();
            showMessageError("documentCannotCreate");
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
