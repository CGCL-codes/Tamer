    CreateExampleOntologies() throws Exception {
        String dbdir = getTempDir() + File.separator + System.currentTimeMillis();
        File dir = new File(dbdir);
        if (dir.exists()) DirUtils.deleteTree(dir);
        dir.mkdir();
        if (!dir.canRead() || !dir.canWrite()) {
            throw new IOException("Cant read or write directory " + dbdir);
        }
        benv = new BerkeleyEnv(s, dbdir, new ONDEXLogger());
        aog = benv.getONDEXGraph();
        md = aog.getONDEXGraphData(s);
        ccElement = md.createConceptClass(s, "Element");
        ccCompound = md.createConceptClass(s, "Compound");
        ccEnv = md.createConceptClass(s, "Environment");
        chemicalAccCv = md.createCV(s, "ChemicalAccession");
        RelationType rt = md.createRelationType(s, "is_componentof");
        rts = md.createRelationTypeSet(s, "is_componentof", rt);
        CV cv = md.createCV(s, "ChemicalOntologyOne");
        CV cv2 = md.createCV(s, "ChemicalOntologyTwo");
        CV cv3 = md.createCV(s, "ChemicalOntologyThree");
        createChemicalOntology(true, true, true, true, cv);
        createChemicalOntology(true, true, true, false, cv2);
        createChemicalOntology(false, false, false, false, cv3);
        lenv = new LuceneEnv(s, dir + File.separator + "index", true);
        lenv.addONDEXListener(new ONDEXLogger());
        lenv.setONDEXGraph(aog);
        AbstractONDEXIterator<AbstractConcept> cit = lenv.getONDEXGraph().getConcepts(s);
        System.out.println(cit.size() + " Concepts made");
        cit.close();
        cit = null;
        AbstractONDEXIterator<AbstractRelation> rit = lenv.getONDEXGraph().getRelations(s);
        System.out.println(rit.size() + " Relations made");
        rit.close();
        rit = null;
    }
