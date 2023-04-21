    private void importAffyFile(BufferedReader in, FileInputStream fis, HashSet<ByteString> names, TermContainer terms, IAssociationParserProgress progress) throws IOException {
        String[] annot = { "Probe Set ID", "GeneChip Array", "Species Scientific Name", "Annotation Date", "Sequence Type", "Sequence Source", "Transcript ID(Array Design)", "Target Description", "Representative Public ID", "Archival UniGene Cluster", "UniGene ID", "Genome Version", "Alignments", "Gene Title", "Gene Symbol", "Chromosomal Location", "Unigene Cluster Type", "Ensembl", "Entrez Gene", "SwissProt", "EC", "OMIM", "RefSeq Protein ID", "RefSeq Transcript ID", "FlyBase", "AGI", "WormBase", "MGI Name", "RGD Name", "SGD accession number", "Gene Ontology Biological Process", "Gene Ontology Cellular Component", "Gene Ontology Molecular Function", "Pathway", "Protein Families", "Protein Domains", "InterPro", "Trans Membrane", "QTL", "Annotation Description", "Annotation Transcript Cluster", "Transcript Assignments", "Annotation Notes" };
        FileChannel fc = fis.getChannel();
        if (progress != null) progress.init((int) fc.size());
        int skipped = 0;
        long millis = 0;
        String line;
        do {
            line = in.readLine();
        } while (line.startsWith("#"));
        boolean headerFailure = false;
        String fields[];
        String delim = ",";
        fields = line.split(delim);
        for (int i = 0; i < 33; i++) {
            String item = fields[i];
            int x, y;
            x = item.indexOf('"') + 1;
            y = item.lastIndexOf('"');
            if (x == 0 && y == (item.length() - 1)) System.out.print("OK");
            item = item.substring(x, y);
            if (!item.equals(annot[i])) {
                logger.severe("Found column header \"" + item + "\" but expected \"" + annot[i] + "\"");
                headerFailure = true;
                break;
            }
        }
        if (!headerFailure) {
            SwissProtAffyAnnotaionSet annotationSet = new SwissProtAffyAnnotaionSet();
            while ((line = in.readLine()) != null) {
                if (progress != null) {
                    long newMillis = System.currentTimeMillis();
                    if (newMillis - millis > 250) {
                        progress.update((int) fc.position());
                        millis = newMillis;
                    }
                }
                ByteString probeid = null, swiss = null;
                LinkedList<TermID> termList = new LinkedList<TermID>();
                int len = line.length();
                int x, y;
                int idx;
                x = -1;
                idx = 0;
                for (int i = 0; i < len; ++i) {
                    if (line.charAt(i) == '\"') {
                        if (x == -1) x = i; else {
                            y = i;
                            if (y > x) {
                                if (idx == 0) {
                                    probeid = new ByteString(line.substring(x + 1, y));
                                } else {
                                    if (idx == 14) {
                                        String s = line.substring(x + 1, y);
                                        if (s.startsWith("---")) swiss = null; else {
                                            swiss = new ByteString(s);
                                            int sepIndex = swiss.indexOf(THREE_SLASHES);
                                            if (sepIndex != -1) swiss = swiss.trimmedSubstring(0, sepIndex);
                                        }
                                    } else if (idx == 30 || idx == 31 || idx == 32) {
                                        String[] ids = line.substring(x + 1, y).split("///");
                                        if (ids != null) {
                                            int j;
                                            for (j = 0; j < ids.length; j++) {
                                                String number;
                                                if (ids[j].contains("/")) {
                                                    number = ids[j].substring(0, ids[j].indexOf('/')).trim();
                                                } else number = ids[j].trim();
                                                try {
                                                    int goId = Integer.parseInt(number);
                                                    TermID id = new TermID(goId);
                                                    if (terms.get(id) != null) termList.add(id); else skipped++;
                                                } catch (NumberFormatException ex) {
                                                }
                                            }
                                        }
                                    }
                                }
                                idx++;
                                x = -1;
                            }
                        }
                    }
                }
                if (swiss != null && swiss.length() > 0) {
                    annotationSet.add(swiss, probeid, termList);
                } else {
                    if (termList.size() > 0) {
                        annotationSet.add(probeid, probeid, termList);
                    }
                }
            }
            for (SwissProtAffyAnnotation swissAnno : annotationSet) {
                ByteString swissID = swissAnno.getSwissProtID();
                for (TermID goID : swissAnno.getGOIDs()) {
                    Association assoc = new Association(swissID, goID);
                    associations.add(assoc);
                }
                for (ByteString affy : swissAnno.getAffyIDs()) {
                    synonym2gene.put(affy, swissID);
                }
            }
        }
        System.err.println("Skipped " + skipped + " annotations");
    }
