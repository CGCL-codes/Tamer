    @SuppressWarnings("unused")
    private void importAssociationFile(BufferedInputStream is, FileInputStream fis, final HashSet<ByteString> names, final TermContainer terms, Collection<String> evidences, final IAssociationParserProgress progress) throws IOException {
        final HashSet<ByteString> myEvidences;
        if (evidences != null) {
            myEvidences = new HashSet<ByteString>();
            for (String e : evidences) myEvidences.add(new ByteString(e));
        } else myEvidences = null;
        final HashSet<TermID> usedGoTerms = new HashSet<TermID>();
        final HashMap<ByteString, ArrayList<Association>> gene2Associations = new HashMap<ByteString, ArrayList<Association>>();
        final HashMap<ByteString, ByteString> dbObject2ObjectSymbol = new HashMap<ByteString, ByteString>();
        final HashMap<ByteString, ByteString> objectSymbol2dbObject = new HashMap<ByteString, ByteString>();
        final FileChannel fc = fis.getChannel();
        if (progress != null) progress.init((int) fc.size());
        class GAFByteLineScanner extends AbstractByteLineScanner {

            int lineno = 0;

            long millis = 0;

            int good = 0;

            int bad = 0;

            int skipped = 0;

            int nots = 0;

            int evidenceMismatch = 0;

            int kept = 0;

            int obsolete = 0;

            HashMap<TermID, Term> altTermID2Term = null;

            public GAFByteLineScanner(InputStream is) {
                super(is);
            }

            @Override
            public boolean newLine(byte[] buf, int start, int len) {
                if (progress != null) {
                    long newMillis = System.currentTimeMillis();
                    if (newMillis - millis > 250) {
                        try {
                            progress.update((int) fc.position());
                        } catch (IOException e) {
                        }
                        millis = newMillis;
                    }
                }
                lineno++;
                if (len < 1 || buf[start] == '!') return true;
                Association assoc = Association.createFromGAFLine(buf, start, len, prefixPool);
                try {
                    TermID currentTermID = assoc.getTermID();
                    Term currentTerm;
                    good++;
                    if (assoc.hasNotQualifier()) {
                        skipped++;
                        nots++;
                        return true;
                    }
                    if (myEvidences != null) {
                        if (!myEvidences.contains(assoc.getEvidence())) {
                            skipped++;
                            evidenceMismatch++;
                            return true;
                        }
                    }
                    currentTerm = terms.get(currentTermID);
                    if (currentTerm == null) {
                        if (altTermID2Term == null) {
                            altTermID2Term = new HashMap<TermID, Term>();
                            for (Term t : terms) for (TermID altID : t.getAlternatives()) altTermID2Term.put(altID, t);
                        }
                        currentTerm = altTermID2Term.get(currentTermID);
                        if (currentTerm == null) {
                            System.err.println("Skipping association of item \"" + assoc.getObjectSymbol() + "\" to " + currentTermID + " because the term was not found!");
                            System.err.println("(Are the obo file and the association " + "file both up-to-date?)");
                            skipped++;
                            return true;
                        } else {
                            currentTermID = currentTerm.getID();
                            assoc.setTermID(currentTermID);
                        }
                    } else {
                        currentTermID = currentTerm.getID();
                        assoc.setTermID(currentTermID);
                    }
                    usedGoTerms.add(currentTermID);
                    if (currentTerm.isObsolete()) {
                        System.err.println("Skipping association of item \"" + assoc.getObjectSymbol() + "\" to " + currentTermID + " because term is obsolete!");
                        System.err.println("(Are the obo file and the association file in sync?)");
                        skipped++;
                        obsolete++;
                        return true;
                    }
                    ByteString[] synonyms;
                    if (assoc.getSynonym() != null && assoc.getSynonym().length() > 2) {
                        synonyms = assoc.getSynonym().splitBySingleChar('|');
                    } else synonyms = null;
                    if (names != null) {
                        boolean keep = false;
                        if (synonyms != null) {
                            for (int i = 0; i < synonyms.length; i++) {
                                if (names.contains(synonyms[i])) {
                                    keep = true;
                                    break;
                                }
                            }
                        }
                        if (keep || names.contains(assoc.getObjectSymbol()) || names.contains(assoc.getDB_Object())) {
                            kept++;
                        } else {
                            skipped++;
                            return true;
                        }
                    } else {
                        kept++;
                    }
                    if (synonyms != null) {
                        for (int i = 0; i < synonyms.length; i++) synonym2gene.put(synonyms[i], assoc.getObjectSymbol());
                    }
                    {
                        ByteString dbObject = objectSymbol2dbObject.get(assoc.getObjectSymbol());
                        if (dbObject == null) objectSymbol2dbObject.put(assoc.getObjectSymbol(), assoc.getDB_Object()); else {
                            if (!dbObject.equals(assoc.getDB_Object())) {
                                symbolWarnings++;
                                if (symbolWarnings < 1000) {
                                    logger.warning("Line " + lineno + ": Expected that symbol \"" + assoc.getObjectSymbol() + "\" maps to \"" + dbObject + "\" but it maps to \"" + assoc.getDB_Object() + "\"");
                                }
                            }
                        }
                        ByteString objectSymbol = dbObject2ObjectSymbol.get(assoc.getDB_Object());
                        if (objectSymbol == null) dbObject2ObjectSymbol.put(assoc.getDB_Object(), assoc.getObjectSymbol()); else {
                            if (!objectSymbol.equals(assoc.getObjectSymbol())) {
                                dbObjectWarnings++;
                                if (dbObjectWarnings < 1000) {
                                    logger.warning("Line " + lineno + ": Expected that dbObject \"" + assoc.getDB_Object() + "\" maps to symbol \"" + objectSymbol + "\" but it maps to \"" + assoc.getObjectSymbol() + "\"");
                                }
                            }
                        }
                    }
                    associations.add(assoc);
                    ArrayList<Association> gassociations = gene2Associations.get(assoc.getObjectSymbol());
                    if (gassociations == null) {
                        gassociations = new ArrayList<Association>();
                        gene2Associations.put(assoc.getObjectSymbol(), gassociations);
                    }
                    gassociations.add(assoc);
                    dbObjectID2gene.put(assoc.getDB_Object(), assoc.getObjectSymbol());
                } catch (Exception ex) {
                    bad++;
                    System.err.println("Nonfatal error: " + "malformed line in association file \n" + "\nCould not parse line " + lineno + "\n" + ex.getMessage() + "\n\"" + buf + "\"\n");
                }
                return true;
            }
        }
        ;
        GAFByteLineScanner ls = new GAFByteLineScanner(is);
        ls.scan();
        if (progress != null) progress.update((int) fc.size());
        is.close();
        logger.info(ls.good + " associations parsed, " + ls.kept + " of which were kept while " + ls.bad + " malformed lines had to be ignored.");
        logger.info("A further " + ls.skipped + " associations were skipped due to various reasons whereas " + ls.nots + " of those where explicitly qualified with NOT, " + +ls.obsolete + " referred to obsolete terms and " + ls.evidenceMismatch + " didn't" + " match the requested evidence codes");
        logger.info("A total of " + usedGoTerms.size() + " terms are directly associated to " + dbObjectID2gene.size() + " items.");
        if (symbolWarnings >= 1000) logger.warning("The symbols of a total of " + symbolWarnings + " entries mapped ambiguously");
        if (dbObjectWarnings >= 1000) logger.warning("The objects of a  total of " + dbObjectWarnings + " entries mapped ambiguously");
        if (false && names != null) {
            for (ByteString name : names) {
                ByteString objectSymbol = synonym2gene.get(name);
                if (objectSymbol != null && !objectSymbol.equals(name) && !names.contains(objectSymbol)) {
                    if (gene2Associations.containsKey(name)) {
                        System.out.println("nn " + name);
                        ArrayList<Association> gassociations = gene2Associations.get(name);
                        for (Association a : gassociations) associations.remove(a);
                    }
                }
            }
        }
    }
