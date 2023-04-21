    @Override
    protected synchronized void doInitialise() throws Exception {
        final EntryRetrievalService entryRetrievalService = UniProtJAPI.factory.getEntryRetrievalService();
        final UniProtEntry entry = entryRetrievalService.getUniProtEntry(id);
        if (entry != null) {
            uniProtId = entry.getUniProtId().getValue();
            type = entry.getType();
            final ProteinDescription proteinDescription = entry.getProteinDescription();
            if (proteinDescription.getRecommendedName().getFields().size() > 0) {
                setName(CollectionUtils.getFirst(proteinDescription.getRecommendedName().getFields()).getValue());
            }
            for (Iterator<Name> iterator = proteinDescription.getAlternativeNames().iterator(); iterator.hasNext(); ) {
                for (Iterator<Field> iterator2 = iterator.next().getFields().iterator(); iterator2.hasNext(); ) {
                    addSynonym(iterator2.next().getValue());
                }
            }
            for (DatabaseCrossReference crossReference : entry.getDatabaseCrossReferences()) {
                if (crossReference instanceof GeneIdImpl) {
                    geneId = ((GeneIdImpl) crossReference).getDbAccession();
                }
            }
            organisms = new TreeSet<String>();
            organisms.add(entry.getOrganism().getCommonName().getValue());
            ncbiTaxonomyIds = new TreeSet<String>();
            for (Iterator<NcbiTaxonomyId> iterator = entry.getNcbiTaxonomyIds().iterator(); iterator.hasNext(); ) {
                final NcbiTaxonomyId ncbiTaxonomyId = iterator.next();
                ncbiTaxonomyIds.add(ncbiTaxonomyId.getValue());
            }
            compartmentGoTerms = new LinkedHashSet<OntologyTerm>();
            for (final Comment comment : entry.getComments()) {
                if (comment instanceof SubcellularLocationComment) {
                    for (final SubcellularLocation subcellularLocation : ((SubcellularLocationComment) comment).getSubcellularLocations()) {
                        final SubcellularLocationValue locationValue = subcellularLocation.getLocation();
                        final String location = locationValue.getValue();
                        final OntologyTerm compartmentGoTerm = OntologyUtils.getInstance().getOntologyTerm(Ontology.GO, location);
                        if (compartmentGoTerm != null) {
                            compartmentGoTerms.add(compartmentGoTerm);
                        }
                    }
                }
            }
            for (final DatabaseCrossReference databaseCrossReference : entry.getDatabaseCrossReferences(DatabaseType.GO)) {
                final Go goCrossReference = (Go) databaseCrossReference;
                if (goCrossReference.getOntologyType().equals(CELLULAR_COMPARTMENT)) {
                    final OntologyTerm goTerm = OntologyUtils.getInstance().getOntologyTerm(Ontology.GO, goCrossReference.getGoId().getValue());
                    compartmentGoTerms.add(goTerm);
                }
            }
            sequence = entry.getSequence().getValue();
            addSynonym(id);
            addSynonym(name);
            addSynonym(geneId);
            addSynonym(uniProtId);
            final URL url = new URL("http://www.cathdb.info/cgi-bin/search.pl?search_text=" + id + "#tab-results");
            final Collection<String> cathNodes = RegularExpressionUtils.getMatches(url.openStream(), "(?<=http://www\\.cathdb\\.info/cathnode/)\\d+\\.\\d+\\.\\d+\\.\\d+(?=.*)");
            for (String cathNode : cathNodes) {
                final OntologyTerm cathTerm = OntologyUtils.getInstance().getOntologyTerm(Ontology.CATH, cathNode);
                if (cathTerm != null) {
                    addXref(cathTerm, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_HAS_PROPERTY);
                }
            }
        }
    }
