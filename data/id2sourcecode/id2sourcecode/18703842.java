    private void output() throws IOException, SAXException, ParserConfigurationException, TransformerConfigurationException, TransformerException, JDOMException, JexmaraldaException, FSMException {
        OutputFileDialog dialog = new OutputFileDialog(table.homeDirectory);
        ActionUtilities.setFileFilter("last-output-filter", table.getTopLevelAncestor(), dialog);
        int retValue = dialog.showSaveDialog(table.parent);
        if (retValue != javax.swing.JFileChooser.APPROVE_OPTION) return;
        ParameterFileFilter selectedFileFilter = (ParameterFileFilter) (dialog.getFileFilter());
        File selectedFile = dialog.getSelectedFile();
        String filename = selectedFile.getAbsolutePath();
        if (!(selectedFile.getName().indexOf(".") >= 0)) {
            filename += "." + selectedFileFilter.getSuffix();
        }
        File exportFile = new File(filename);
        if (exportFile.exists()) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(table, exportFile.getAbsolutePath() + "\nalready exists. Overwrite?");
            if (confirm == javax.swing.JOptionPane.CANCEL_OPTION) return;
            if (confirm == javax.swing.JOptionPane.NO_OPTION) output();
        }
        BasicTranscription trans = null;
        switch(dialog.getSelectionChoice()) {
            case SelectionAccessory.ALL:
                trans = table.getModel().getTranscription().makeCopy();
                break;
            case SelectionAccessory.VISIBLE:
                trans = table.getVisibleTiersAsNewTranscription();
                break;
            case SelectionAccessory.SELECTION:
                trans = table.getCurrentSelectionAsNewTranscription();
                break;
        }
        if ((selectedFileFilter == dialog.HTMLPartiturFileFilter) || (selectedFileFilter == dialog.HTMLPartiturWithHTML5AudioFileFilter) || (selectedFileFilter == dialog.HTMLPartiturWithFlashFileFilter) || (selectedFileFilter == dialog.RTFPartiturFileFilter) || (selectedFileFilter == dialog.SVGPartiturFileFilter) || (selectedFileFilter == dialog.XMLPartiturFileFilter)) {
            InterlinearText it = null;
            if (dialog.getSelectionChoice() != SelectionAccessory.SELECTION) {
                it = ItConverter.BasicTranscriptionToInterlinearText(trans, table.getModel().getTierFormatTable());
            } else {
                int timelineStart = table.selectionStartCol;
                it = ItConverter.BasicTranscriptionToInterlinearText(trans, table.getModel().getTierFormatTable(), timelineStart);
            }
            System.out.println("Transcript converted to interlinear text.");
            if (table.getFrameEndPosition() >= 0) {
                ((ItBundle) it.getItElementAt(0)).frameEndPosition = table.getFrameEndPosition();
            }
            if (selectedFileFilter == dialog.HTMLPartiturFileFilter) {
                exportHTMLPartitur(it, filename, dialog.framesAccessory.useFrames());
            } else if (selectedFileFilter == dialog.RTFPartiturFileFilter) {
                exportRTFPartitur(it, filename);
            } else if (selectedFileFilter == dialog.SVGPartiturFileFilter) {
                exportSVGPartitur(it, filename, dialog.svgAccessory.getSubdirectory(), dialog.svgAccessory.getBasename());
            } else if (selectedFileFilter == dialog.XMLPartiturFileFilter) {
                exportXMLPartitur(it, filename, dialog.chooseSettingsForXMLExportPanel.getSelection());
            } else if (selectedFileFilter == dialog.HTMLPartiturWithFlashFileFilter) {
                exportHTMLPartiturWithFlash(it, filename);
            } else if (selectedFileFilter == dialog.HTMLPartiturWithHTML5AudioFileFilter) {
                exportHTMLPartiturWithHTML5Audio(it, filename);
            }
        } else if (selectedFileFilter == dialog.FreeStylesheetFileFilter) {
            exportFreeStylesheet(filename, dialog.encodings[dialog.encodingComboBox.getSelectedIndex()]);
        } else if (selectedFileFilter == dialog.HTMLSegmentChainFileFilter) {
            exportHTMLSegmentChainList(trans, filename, table.getModel().getTierFormatTable());
        } else if (selectedFileFilter == dialog.HTMLSegmentChainWithFlashFileFilter) {
            exportHTMLSegmentChainListWithFlash(trans, filename);
        } else if (selectedFileFilter == dialog.HTMLSegmentChainWithHTML5AudioFileFilter) {
            exportHTMLSegmentChainListWithHTML5Audio(trans, filename);
        } else if (selectedFileFilter == dialog.GATTranscriptFileFilter) {
            exportGATTranscript(trans, filename, dialog.encodings[dialog.encodingComboBox.getSelectedIndex()]);
        } else if (selectedFileFilter == dialog.SimpleTextTranscriptFileFilter) {
            exportSimpleTextTranscript(trans, filename);
        }
        ActionUtilities.memorizeFileFilter("last-output-filter", table.getTopLevelAncestor(), dialog);
        if (filename.endsWith("html") || filename.endsWith("htm")) {
            BrowserLauncher.openURL(new File(filename).toURI().toURL().toString());
        }
        table.status("Transcription output as " + filename);
    }
