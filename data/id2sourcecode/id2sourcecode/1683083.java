    private void saveFile() {
        if (checkOutput()) {
            String dir = System.getProperty("user.dir");
            JFileChooser chooser = new JFileChooser(dir);
            int r = chooser.showSaveDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                String t = chooser.getSelectedFile().getAbsolutePath();
                boolean overwrite = true;
                File output_file = new File(t);
                if (output_file.exists()) {
                    int ans = JOptionPane.showConfirmDialog(this, "The file already exists, overwrite?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                    overwrite = (ans == JOptionPane.YES_OPTION);
                }
                if (overwrite) {
                    try {
                        document = buildDocument();
                        TransformerFactory tFactory = TransformerFactory.newInstance();
                        Transformer transformer = tFactory.newTransformer();
                        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, DTD_publicID);
                        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD_document);
                        formatXML(document.getDocumentElement(), "    ");
                        document.getDocumentElement().normalize();
                        DOMSource source = new DOMSource(document);
                        StreamResult result = new StreamResult(output_file);
                        transformer.transform(source, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
