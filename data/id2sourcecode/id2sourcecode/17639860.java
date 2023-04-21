    public void load() {
        try {
            rootsForIndentationLevels = new Hashtable();
            String rootIdentifier = TermIdentifierUtility.createIdentifier(url, "root");
            overAllRootTerm = new TreeOntologyTerm(rootIdentifier, "root");
            URLConnection urlConnection = url.openConnection();
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(isr);
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                numberOfIndents = countIndentationLevels(currentLine);
                String phrase = currentLine.trim();
                String identifier = TermIdentifierUtility.createIdentifier(url, phrase);
                TreeOntologyTerm currentTerm = new TreeOntologyTerm(identifier, phrase);
                if (numberOfIndents == 0) {
                    overAllRootTerm.addRelatedTerm(currentTerm);
                } else {
                    TreeOntologyTerm levelTerm = (TreeOntologyTerm) rootsForIndentationLevels.get(new Integer(numberOfIndents - 1));
                    levelTerm.addRelatedTerm(currentTerm);
                    currentTerm.setParentTerm(levelTerm);
                }
                rootsForIndentationLevels.remove(new Integer(numberOfIndents));
                rootsForIndentationLevels.put(new Integer(numberOfIndents), currentTerm);
                currentLine = bufferedReader.readLine();
            }
            ArrayList children = overAllRootTerm.getRelatedTerms();
            int numberOfChildren = children.size();
            if (numberOfChildren == 1) {
                TreeOntologyTerm firstChild = (TreeOntologyTerm) children.get(0);
                rootTerm = firstChild;
            } else {
                rootTerm = overAllRootTerm;
            }
            isSourceWorking = true;
            String statusOKMessage = PedroResources.getMessage("ontology.statusOK", url.getFile());
            status = new StringBuffer();
            status.append(statusOKMessage);
        } catch (Exception err) {
            status = new StringBuffer();
            String statusErrorMessage = PedroResources.getMessage("ontology.statusError", err.toString());
            status.append(statusErrorMessage);
            isSourceWorking = false;
            err.printStackTrace(System.out);
        }
    }
