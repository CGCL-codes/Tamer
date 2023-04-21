    public void process() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(TRUE_OUTPUT);
        validateOutput(FALSE_OUTPUT);
        validateInput(INPUT);
        BlockReader input = getInput(INPUT);
        BlockWriter trueOutput = getOutput(TRUE_OUTPUT);
        BlockWriter falseOutput = getOutput(FALSE_OUTPUT);
        try {
            Object block;
            boolean listHasData = false;
            int listDepth = 0;
            List<Object> readData = new LinkedList<Object>();
            while ((block = input.read()) != ControlBlock.NO_MORE_DATA) {
                if (!listHasData) {
                    readData.add(block);
                } else {
                    falseOutput.write(block);
                }
                if (block == ControlBlock.LIST_BEGIN) {
                    listDepth++;
                    if (listDepth > 1 && !listHasData) {
                        listHasData = true;
                        writeDataBlocks(readData, falseOutput);
                        readData.clear();
                    }
                } else if (block == ControlBlock.LIST_END) {
                    listDepth--;
                    if (listDepth == 0) {
                        if (listHasData) {
                            listHasData = false;
                            readData.clear();
                        } else {
                            writeDataBlocks(readData, trueOutput);
                            readData.clear();
                        }
                    }
                } else if (!(block instanceof MetadataWrapper) && !listHasData) {
                    listHasData = true;
                    writeDataBlocks(readData, falseOutput);
                    readData.clear();
                }
            }
        } catch (PipeClosedException e) {
        } catch (PipeIOException e) {
            throw new ActivityProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }
