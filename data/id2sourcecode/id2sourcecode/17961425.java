    public Iterator<DigestedPeptide> digestProteins(File fastaFile, File decoyFile) throws ParseException, IOException, PeptideFragmentationException, ProcessException {
        FastaReader reader = FastaReader.newInstance();
        FastaHeaderFormatManager manager = params.getFormatMgr();
        reader.setHeaderManager(manager);
        FastaWriter writer = FastaWriter.newInstance();
        reader.parse(fastaFile);
        Iterator<FastaEntry> it = reader.iterator();
        uniquePeptides = new HashMap<String, String>();
        List<DigestedPeptide> digests = new ArrayList<DigestedPeptide>();
        writer.open(decoyFile);
        while (it.hasNext()) {
            FastaEntry orgEntry = it.next();
            if (params.doAppend()) {
                writer.add(orgEntry);
            }
            FastaEntry decoyEntry = null;
            String sequenceString = orgEntry.getSequence();
            String decoyProteinSeq = "";
            FastaHeader decoyHeader = FastaUtils.makeDecoyHeader(orgEntry.getHeader(), params.getDecoyFlag(), params.getDecoyFlagPosition());
            if (params.getMethod().equals("proteinReverse")) {
                decoyProteinSeq = FastaUtils.reverseSequence(sequenceString);
            } else if (params.getMethod().equals("proteinShuffle")) {
                decoyProteinSeq = FastaUtils.shuffleSequence(sequenceString);
            } else if (params.getMethod().equals("peptideShuffle")) {
                Peptide protein = new Peptide.Builder(orgEntry.getSequence()).nterm(NTerminus.PROT_N).cterm(CTerminus.PROT_C).ambiguityEnabled().build();
                Digester digester = params.getDigester();
                digester.digest(protein);
                for (DigestedPeptide digest : digester.getAllDigests()) {
                    String peptideSeq = digest.getSymbolSequence().toString();
                    if (uniquePeptides.containsKey(peptideSeq)) {
                        decoyProteinSeq += uniquePeptides.get(peptideSeq);
                    } else {
                        String shuffledSeq = peptideSeq;
                        if (peptideSeq.length() > params.getPeptLenFilter()) {
                            if (params.getKeepTermini().equalsIgnoreCase("cterm")) {
                                shuffledSeq = shuffleSequenceKeepCTerm(peptideSeq);
                            } else if (params.getKeepTermini().equalsIgnoreCase("nterm")) {
                                shuffledSeq = shuffleSequenceKeepNTerm(peptideSeq);
                            } else {
                                shuffledSeq = shuffleSequence(peptideSeq);
                            }
                        }
                        uniquePeptides.put(peptideSeq, shuffledSeq);
                        decoyProteinSeq += shuffledSeq;
                    }
                }
            }
            decoyEntry = new FastaEntry(decoyHeader, decoyProteinSeq);
            writer.add(decoyEntry);
        }
        writer.close();
        return digests.iterator();
    }
