    private void readJAPEFileContents(URL url) {
        if (treePhases.getLastSelectedPathComponent() == null) return;
        updating = true;
        try {
            Reader japeReader = null;
            String encoding = (String) transducer.getParameterValue("encoding");
            if (encoding == null) {
                japeReader = new BomStrippingInputStreamReader(url.openStream());
            } else {
                japeReader = new BomStrippingInputStreamReader(url.openStream(), encoding);
            }
            BufferedReader br = new BufferedReader(japeReader);
            String content = br.readLine();
            StringBuilder japeFileContents = new StringBuilder();
            List<Integer> lineOffsets = new ArrayList<Integer>();
            while (content != null) {
                lineOffsets.add(japeFileContents.length());
                japeFileContents.append(content.replaceAll("\t", "   ")).append("\n");
                content = br.readLine();
            }
            textArea.setText(japeFileContents.toString());
            textArea.updateUI();
            br.close();
            ParseCpslTokenManager tokenManager = new ParseCpslTokenManager(new SimpleCharStream(new StringReader(japeFileContents.toString())));
            StyledDocument doc = textArea.getStyledDocument();
            doc.setCharacterAttributes(0, japeFileContents.length(), defaultStyle, true);
            ((DefaultMutableTreeNode) treePhases.getSelectionPath().getLastPathComponent()).removeAllChildren();
            Token t;
            while ((t = tokenManager.getNextToken()).kind != 0) {
                Token special = t.specialToken;
                while (special != null) {
                    Style style = colorMap.get(special.kind);
                    if (style != null) {
                        int start = lineOffsets.get(special.beginLine - 1) + special.beginColumn - 1;
                        int end = lineOffsets.get(special.endLine - 1) + special.endColumn - 1;
                        doc.setCharacterAttributes(start, end - start + 1, style, true);
                    }
                    special = special.specialToken;
                }
                Style style = colorMap.get(t.kind);
                if (style != null) {
                    int start = lineOffsets.get(t.beginLine - 1) + t.beginColumn - 1;
                    int end = lineOffsets.get(t.endLine - 1) + t.endColumn - 1;
                    doc.setCharacterAttributes(start, end - start + 1, style, true);
                }
                if (t.kind == ParseCpslConstants.path) {
                    ((DefaultMutableTreeNode) treePhases.getSelectionPath().getLastPathComponent()).add(new DefaultMutableTreeNode(t.toString()));
                }
            }
        } catch (IOException ioe) {
            throw new GateRuntimeException(ioe);
        } catch (ResourceInstantiationException rie) {
            throw new GateRuntimeException(rie);
        }
        if (treePhases.getSelectionRows() != null && treePhases.getSelectionRows().length > 0) treePhases.expandRow(treePhases.getSelectionRows()[0]);
        updating = false;
    }
