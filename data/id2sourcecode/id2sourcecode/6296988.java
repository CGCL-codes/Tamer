    private void export() throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException, JexmaraldaException, FSMException, JDOMException, XSLTransformException, Exception {
        ExportFileDialog dialog = new ExportFileDialog(table.homeDirectory);
        ActionUtilities.setFileFilter("last-export-filter", table.getTopLevelAncestor(), dialog);
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
            if (confirm == javax.swing.JOptionPane.NO_OPTION) export();
        }
        BasicTranscription trans = table.getModel().getTranscription().makeCopy();
        if (selectedFileFilter == dialog.TASXFileFilter) {
            TASXConverter tc = new TASXConverter();
            tc.writeTASXToFile(trans, filename);
        } else if (selectedFileFilter == dialog.PraatFileFilter) {
            PraatConverter pc = new PraatConverter();
            pc.writePraatToFile(trans, filename);
        } else if (selectedFileFilter == dialog.AGFileFilter) {
            AIFConverter.writeAIFToFile(trans, filename);
        } else if (selectedFileFilter == dialog.EAFFileFilter) {
            ELANConverter ec = new ELANConverter();
            ec.writeELANToFile(trans, filename);
        } else if (selectedFileFilter == dialog.AudacityLabelFileFilter) {
            switch(dialog.audacityExportAccessoryPanel.getMethod()) {
                case AudacityConverter.ALL_TIERS:
                    AudacityConverter.writeAudacityToFile(trans, filename);
                    break;
                case AudacityConverter.SELECTED_TIERS:
                    AudacityConverter.writeAudacityToFile(trans, filename, table.selectionStartRow, table.selectionEndRow);
                    break;
                case AudacityConverter.TIMELINE:
                    AudacityConverter.writeTimelineToFile(trans, filename);
                    break;
            }
        } else if (selectedFileFilter == dialog.TEIFileFilter) {
            TEIConverter ec;
            switch(dialog.teiExportAccessoryPanel.getMethod()) {
                case TEIConverter.GENERIC_METHOD:
                    ec = new TEIConverter();
                    ec.writeGenericTEIToFile(trans, filename);
                    break;
                case TEIConverter.AZM_METHOD:
                    ec = new TEIConverter();
                    ec.writeTEIToFile(trans, filename);
                    break;
                case TEIConverter.MODENA_METHOD:
                    ec = new TEIConverter("/org/exmaralda/partitureditor/jexmaralda/xsl/EXMARaLDA2TEI_Modena.xsl");
                    ec.writeModenaTEIToFile(trans, filename);
                    break;
                case TEIConverter.HIAT_METHOD:
                    ec = new TEIConverter();
                    ec.writeHIATTEIToFile(trans, filename);
                    break;
                case TEIConverter.HIAT_NEW_METHOD:
                    ec = new TEIConverter();
                    ec.writeNewHIATTEIToFile(trans, filename);
                    break;
                case TEIConverter.CGAT_METHOD:
                    ec = new TEIConverter();
                    ec.writeFOLKERTEIToFile(trans, filename);
                    break;
            }
        } else if (selectedFileFilter == dialog.TEIModenaFileFilter) {
            TEIConverter ec = new TEIConverter("/org/exmaralda/partitureditor/jexmaralda/xsl/EXMARaLDA2TEI_Modena.xsl");
            ec.writeModenaTEIToFile(trans, filename);
        } else if (selectedFileFilter == dialog.CHATTranscriptFileFilter) {
            switch(dialog.chatExportAccessoryPanel.getMethod()) {
                case CHATConverter.CHAT_SEGMENTATION_METHOD:
                    exportCHATTranscript(trans, filename, "UTF-8");
                    break;
                case CHATConverter.HIAT_SEGMENTATION_METHOD:
                    CHATConverter.writeHIATSegmentedCHATFile(trans, exportFile);
                    break;
                case CHATConverter.EVENT_METHOD:
                    CHATConverter.writeEventSegmentedCHATFile(trans, exportFile);
                    break;
            }
        } else if (selectedFileFilter == dialog.ExmaraldaSegmentedTranscriptionFileFilter) {
            SegmentedTranscription st = trans.toSegmentedTranscription();
            st.setEXBSource(table.filename);
            st.writeXMLToFile(filename, "none");
        } else if (selectedFileFilter == dialog.FOLKERTranscriptionFileFilter) {
            EventListTranscription elt = org.exmaralda.folker.io.EventListTranscriptionConverter.importExmaraldaBasicTranscription(trans);
            org.exmaralda.folker.io.EventListTranscriptionXMLReaderWriter.writeXML(elt, exportFile, new org.exmaralda.folker.data.GATParser(), 0);
        } else if (selectedFileFilter == dialog.TreeTaggerFilter) {
            new TreeTaggerConverter().writeText(trans, exportFile);
        }
        ActionUtilities.memorizeFileFilter("last-export-filter", table.getTopLevelAncestor(), dialog);
        table.status("Transcription exported as " + filename);
    }
