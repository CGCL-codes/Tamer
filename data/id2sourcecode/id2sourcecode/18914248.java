    private void convertJButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String[] splittedName = titleJTextField.getText().split(" ");
        String fileName = "";
        for (String part : splittedName) {
            fileName += part;
        }
        File prideFile = new File(outputFolderJTextField.getText(), fileName + ".xml");
        if (prideFile.exists()) {
            int selection = JOptionPane.showConfirmDialog(this, "The file \'" + prideFile.getAbsolutePath() + "\' already exists." + "\nOverwrite file?", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (selection != JOptionPane.YES_OPTION) {
                return;
            }
        }
        final String prideFileName = fileName;
        final PrideExportDialog prideExportDialog = this;
        progressDialog = new ProgressDialogX(this, this, true);
        progressDialog.setIndeterminate(true);
        new Thread(new Runnable() {

            public void run() {
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Exporting PRIDE XML. Please Wait...");
                try {
                    progressDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }, "ProgressDialog").start();
        new Thread("ConvertThread") {

            @Override
            public void run() {
                peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")));
                ArrayList<Reference> references = new ArrayList<Reference>();
                for (int row = 0; row < ((DefaultTableModel) referencesJTable.getModel()).getRowCount(); row++) {
                    references.add(new Reference((String) referencesJTable.getValueAt(row, 1), (String) referencesJTable.getValueAt(row, 2), (String) referencesJTable.getValueAt(row, 3)));
                }
                String selectedContact = (String) contactsJComboBox.getSelectedItem();
                Contact contact = prideObjectsFactory.getContacts().get(selectedContact);
                String selectedSample = (String) sampleJComboBox.getSelectedItem();
                Sample sample = prideObjectsFactory.getSamples().get(selectedSample);
                String selectedProtocol = (String) protocolJComboBox.getSelectedItem();
                Protocol protocol = prideObjectsFactory.getProtocols().get(selectedProtocol);
                String selectedInstrument = (String) instrumentJComboBox.getSelectedItem();
                Instrument instrument = prideObjectsFactory.getInstruments().get(selectedInstrument);
                boolean conversionCompleted = false;
                try {
                    PRIDEExport prideExport = new PRIDEExport(peptideShakerGUI, prideExportDialog, titleJTextField.getText(), labelJTextField.getText(), descriptionJTextArea.getText(), projectJTextField.getText(), references, contact, sample, protocol, instrument, new File(outputFolderJTextField.getText()), prideFileName);
                    prideExport.createPrideXmlFile(progressDialog);
                    conversionCompleted = true;
                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                }
                progressDialog.dispose();
                peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")));
                if (conversionCompleted && !cancelProgress) {
                    JLabel label = new JLabel();
                    JEditorPane ep = new JEditorPane("text/html", "<html><body bgcolor=\"#" + Util.color2Hex(label.getBackground()) + "\">" + "PRIDE XML file \'" + new File(outputFolderJTextField.getText(), titleJTextField.getText() + ".xml").getAbsolutePath() + "\' created.<br><br>" + "Please see <a href=\"http://www.ebi.ac.uk/pride\">www.ebi.ac.uk/pride</a> for how to submit data to PRIDE." + "</body></html>");
                    ep.addHyperlinkListener(new HyperlinkListener() {

                        @Override
                        public void hyperlinkUpdate(HyperlinkEvent e) {
                            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                                BareBonesBrowserLaunch.openURL(e.getURL().toString());
                            }
                        }
                    });
                    ep.setBorder(null);
                    ep.setEditable(false);
                    JOptionPane.showMessageDialog(prideExportDialog, ep, "PRIDE XML File Created", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                if (cancelProgress) {
                    JOptionPane.showMessageDialog(peptideShakerGUI, "PRIDE XML conversion cancelled by the user.", "PRIDE XML Conversion Cancelled", JOptionPane.WARNING_MESSAGE);
                }
                cancelProgress = false;
            }
        }.start();
    }
