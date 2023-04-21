    @SuppressWarnings(value = "unchecked")
    public static <T> ReaderSource<T> getTypeReader(final PipeOutput pipeOutput) throws IOException {
        ReaderSource<T> reader;
        if (pipeOutput == null) {
            return null;
        }
        Order order = createOrder(pipeOutput.getPipe());
        String[] fileNames = pipeOutput.getFileNames();
        if (fileNames.length > 100) {
            List<String> names = Arrays.asList(fileNames);
            ArrayList<String> reduced = new ArrayList<String>();
            for (int i = 0; i < names.size(); i += 20) {
                int start = i;
                int end = Math.min(names.size(), i + 20);
                List<String> toCombine = names.subList(start, end);
                reader = OrderedCombiner.combineFromFiles(toCombine, order);
                File temporary = Utility.createTemporary();
                FileOrderedWriter<T> writer = new FileOrderedWriter<T>(temporary, order);
                try {
                    reader.setProcessor(writer);
                } catch (IncompatibleProcessorException e) {
                    throw (IOException) new IOException("Incompatible processor for reader tuples").initCause(e);
                }
                reader.run();
                reduced.add(temporary.toString());
                temporary.deleteOnExit();
            }
            reader = OrderedCombiner.combineFromFiles(reduced, order);
        } else if (fileNames.length > 1) {
            reader = OrderedCombiner.combineFromFiles(Arrays.asList(fileNames), order);
        } else {
            reader = new FileOrderedReader(fileNames[0], order);
        }
        return reader;
    }
