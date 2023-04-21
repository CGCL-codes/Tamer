    protected String callOndexProvider(String keyword, List<QTL> qtls, List<String> list) {
        long timestamp = System.currentTimeMillis();
        String fileGViewer = keyword + "_" + timestamp + ".xml";
        String fileTxt = keyword + "_" + timestamp + ".txt";
        ONDEXGraph graph = null;
        String request = "";
        try {
            graph = ondexProvider.searchGenome(keyword);
            AttributeName attTAXID = graph.getMetaData().getAttributeName("TAXID");
            if (graph.getConcepts().size() == 0) {
                System.out.println("NoFile: no genes found");
                request = "NoFile:noGenesFound";
            } else {
                ConceptClass ccGene = graph.getMetaData().getConceptClass("Gene");
                AttributeName attSize = graph.getMetaData().getAttributeName("size");
                Set<ONDEXConcept> candidates = graph.getConceptsOfConceptClass(ccGene);
                Map<ONDEXConcept, Double> gene2count = new HashMap<ONDEXConcept, Double>();
                for (ONDEXConcept gene : candidates) {
                    if (gene.getAttribute(attTAXID) == null || !gene.getAttribute(attTAXID).getValue().toString().equals(ondexProvider.getTaxId())) {
                        continue;
                    }
                    if (gene.getAttribute(attSize) != null) {
                        Double score = ((Integer) gene.getAttribute(attSize).getValue()).doubleValue();
                        gene2count.put(gene, score);
                    } else {
                    }
                }
                SortedSet<Entry<ONDEXConcept, Double>> sortedCandidates = ondexProvider.entriesSortedByValues(gene2count);
                ArrayList<ONDEXConcept> genes = new ArrayList<ONDEXConcept>();
                int count = 0;
                for (Entry<ONDEXConcept, Double> rankedGene : sortedCandidates) {
                    if (++count > 100) break;
                    genes.add(rankedGene.getKey());
                }
                System.out.println("Genes(s) displayed in GViewer: " + genes.size());
                Set<ONDEXConcept> userGenes = null;
                if (list != null && list.size() > 0) {
                    userGenes = ondexProvider.searchGenes(list);
                }
                boolean xmlIsCreated = ondexProvider.writeAnnotationXML(genes, userGenes, qtls, MultiThreadServer.props.getProperty("AnnotationPath") + fileGViewer, keyword, 100);
                boolean txtIsCreated = ondexProvider.writeTableOut(genes, userGenes, qtls, MultiThreadServer.props.getProperty("AnnotationPath") + fileTxt, graph);
                if (xmlIsCreated && txtIsCreated) {
                    System.out.println("FileCreated:" + fileGViewer + ":" + fileTxt + ":" + genes.size());
                    request = "FileCreated:" + fileGViewer + ":" + fileTxt + ":" + genes.size();
                } else {
                    System.out.println("NoFile: File (Keyword) is not created");
                    System.out.println("NoFile: no genes found");
                    request = "NoFile: no genes found";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }
