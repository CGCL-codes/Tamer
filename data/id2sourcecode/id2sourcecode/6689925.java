    private boolean loadAnnotationHelper(String AnnotationFile, Ontology ontology, Hashtable TermToAccessions_HT, Hashtable AccessionToTerms_HT) {
        BufferedReader in;
        try {
            if (isURL(AnnotationFile)) {
                URL url = new URL(AnnotationFile);
                URLConnection uc = url.openConnection();
                in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            } else {
                in = new BufferedReader(new FileReader(AnnotationFile));
            }
        } catch (Exception e) {
            if (this.verbose) System.out.println("Error: the file at " + AnnotationFile + " could not be opened for reading.");
            return false;
        }
        int lineNum = 1;
        try {
            while (true) {
                String line;
                try {
                    line = in.readLine().trim();
                } catch (Exception e) {
                    break;
                }
                lineNum++;
                String[] components = line.split("\\s");
                String accession = components[0].trim();
                int startI = 1;
                if (line.charAt(0) == '\"') {
                    int terminatingIndex = line.indexOf("\"", 1);
                    accession = line.substring(1, terminatingIndex);
                    components = line.substring(terminatingIndex + 1).trim().split("\\s");
                    startI = 0;
                }
                HashSet terms = new HashSet(components.length - 1);
                for (int i = startI; i < components.length; i++) {
                    OBO_Object obj = (OBO_Object) ontology.terms.get(Integer.valueOf(components[i]));
                    if (obj == null) {
                        if (this.verbose) System.out.println("Line " + Integer.toString(lineNum) + ": Warning: Unrecognized ontology ID " + components[i] + " found in annotation");
                        continue;
                    }
                    while (obj.is_obsolete) {
                        if (obj.use_term != null && obj.use_term.length > 0) {
                            obj = (OBO_Object) ontology.terms.get(obj.use_term[0]);
                        } else {
                            break;
                        }
                    }
                    terms.add(obj);
                    HashSet AccessionsForTerm = (HashSet) TermToAccessions_HT.get(obj.integer_id);
                    if (AccessionsForTerm == null) {
                        AccessionsForTerm = new HashSet();
                        TermToAccessions_HT.put(obj.integer_id, AccessionsForTerm);
                    }
                    AccessionsForTerm.add(accession);
                }
                HashSet existingTerms = (HashSet) AccessionToTerms_HT.get(accession);
                if (existingTerms != null) {
                    existingTerms.addAll(terms);
                } else {
                    AccessionToTerms_HT.put(accession, terms);
                }
            }
            in.close();
        } catch (IOException e) {
            if (this.verbose) System.out.println("Error reading file");
            return false;
        }
        return true;
    }
