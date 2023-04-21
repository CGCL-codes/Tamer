    private void pubmedLookupButtonActionPerformed() {
        try {
            String ncbiUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id=" + pubmedIdField.getText();
            URL url = new URL(ncbiUrl);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("DocSum");
            if (nList.getLength() == 1) {
                PubMedReference ref = new PubMedReference();
                NodeList items = doc.getElementsByTagName("Item");
                for (int i = 0; i < items.getLength(); i++) {
                    Node node = items.item(i);
                    String nodeName = node.getAttributes().getNamedItem("Name").getFirstChild().getNodeValue();
                    if (nodeName.equals("PubDate")) {
                        ref.setDate(getValue(node));
                    }
                    if (nodeName.equals("Source")) {
                        ref.setSource(getValue(node));
                    }
                    if (nodeName.equals("Author")) {
                        ref.addAuthor(getValue(node));
                    }
                    if (nodeName.equals("Title")) {
                        ref.setTitle(getValue(node));
                    }
                    if (nodeName.equals("Volume")) {
                        ref.setVolume(getValue(node));
                    }
                    if (nodeName.equals("Issue")) {
                        ref.setIssue(getValue(node));
                    }
                    if (nodeName.equals("Pages")) {
                        ref.setPages(getValue(node));
                    }
                    if (nodeName.equals("doi")) {
                        ref.setDoi(getValue(node));
                    }
                    if (nodeName.equals("pubmed")) {
                        ref.setPmid(getValue(node));
                    }
                }
                referenceArea.setText(ref.toCitation());
                if (ref.getDOI() != null) {
                    doiIDField.setText(ref.getDOI());
                }
            } else {
                if (nList.getLength() == 0) {
                    JOptionPane.showMessageDialog(this, "Pubmed ID not found", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "More than one document returned for pubmed ID: " + pubmedIdField.getText(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
