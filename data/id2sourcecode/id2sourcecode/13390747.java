    @Override
    protected void processIteration(Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        InputStream input = (InputStream) iterationData[0];
        try {
            TupleInputStream tuples = new TupleInputStream(input);
            mOutput.write(ControlBlock.LIST_BEGIN);
            mOutput.write(new MetadataWrapper(tuples.readTupleMetadata()));
            try {
                while (true) {
                    mOutput.write(tuples.readTuple());
                }
            } catch (EOFException e) {
            }
            mOutput.write(ControlBlock.LIST_END);
        } catch (UnsupportedTupleTypeException e) {
            throw new ActivityUserException(e);
        } catch (IOException e) {
            throw new ActivityProcessingException(e);
        } catch (PipeClosedException e) {
            iterativeStageComplete();
        } catch (PipeIOException e) {
            throw new ActivityProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }
