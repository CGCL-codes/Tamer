    protected JComponent makeTripSummaryView() {
        Box statsView = new Box(VERTICAL);
        statsView.add(new JLabel("Daily Trip Summary:"));
        final JTextArea statsTextView = new JTextArea();
        statsTextView.setEditable(false);
        statsView.add(new JScrollPane(statsTextView, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED));
        Box buttonRow = new Box(HORIZONTAL);
        statsView.add(buttonRow);
        buttonRow.add(Box.createHorizontalGlue());
        final JButton dumpButton = new JButton("dump");
        buttonRow.add(dumpButton);
        dumpButton.setEnabled(false);
        dumpButton.addActionListener(new ActionListener() {

            JFileChooser fileChooser = new JFileChooser();

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
        });
        _model.addDocumentModelListener(new DocumentModelListener() {

            public void handlerSelected(DocumentModel model, RequestHandler handler) {
                updateLog();
            }

            public void mpsTypeSelected(DocumentModel model, int index) {
                updateLog();
            }

            public void mpsChannelsUpdated(RequestHandler handler, int mpsTypeIndex, java.util.List channelRefs) {
            }

            public void inputChannelsUpdated(RequestHandler handler, int mpsTypeIndex, java.util.List channelRefs) {
            }

            public void mpsEventsUpdated(RequestHandler handler, int mpsTypeIndex) {
                updateLog();
            }

            public void lastCheck(RequestHandler handler, Date timestamp) {
            }

            protected void updateLog() {
                final int mpsType = _model.getSelectedMPSTypeIndex();
                final RequestHandler handler = _model.getSelectedHandler();
                String text = "";
                if (mpsType >= 0 && handler != null) {
                    text = handler.getTripSummary(mpsType);
                }
                statsTextView.setText(text);
                statsTextView.setSelectionStart(0);
                statsTextView.moveCaretPosition(0);
                dumpButton.setEnabled(text != "" && text != null);
            }
        });
        return statsView;
    }
