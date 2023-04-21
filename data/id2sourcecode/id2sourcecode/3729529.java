    private <O extends Observation & CentroidFactory<O>> void learn(int nbStates, RelatedObjs<O> relatedObjs, Reader reader, Writer writer) throws IOException, FileFormatException {
        OpdfFactory<? extends Opdf<O>> opdfFactory = relatedObjs.opdfFactory();
        List<List<O>> seqs = relatedObjs.readSequences(reader);
        OpdfWriter<? extends Opdf<O>> opdfWriter = relatedObjs.opdfWriter();
        KMeansLearner<O> kl = new KMeansLearner<O>(nbStates, opdfFactory, seqs);
        Hmm<O> hmm = kl.learn();
        HmmWriter.write(writer, opdfWriter, hmm);
    }
