    private void saveObservationAs() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(Extensions.XTIT_FILE_FILTER);
            try {
                StringBuilder titname = new StringBuilder();
                titname.append(personality.getRN());
                titname.append("_");
                titname.append(personality.getSEQ());
                titname.append("_");
                titname.append(Dates.SAVE_DATE_FORMAT.format(personality.getDATE()));
                titname.append(".xtit");
                chooser.setSelectedFile(new File(titname.toString()));
            } catch (AttributeNotSetException exception) {
            }
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedfile = chooser.getSelectedFile();
                if (!selectedfile.exists() || JOptionPane.showConfirmDialog(this, selectedfile.getName() + " already exists. Overwriter file?", "File already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = builder.newDocument();
                    ObservationDOMWriter domwriter = new ObservationDOMWriter(document, personality);
                    observation.produceObservation(domwriter);
                    Writer output = new FileWriter(file = selectedfile);
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    Source source = new DOMSource(document);
                    Result result = new StreamResult(output);
                    transformer.transform(source, result);
                    dirty = false;
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the file: " + exception.toString());
        }
    }
