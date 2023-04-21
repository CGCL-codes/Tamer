    public SNMPTableModel(String[] headers, Functor[] readFunctors, Functor[] writeFunctors, Class<Object>[] editorClasses) {
        this.headers.addAll(Arrays.asList(headers));
        this.classes.addAll(Arrays.asList(editorClasses));
        this.readFunctors = new ArrayList<Functor>(Arrays.asList(readFunctors));
        this.writeFunctors = new ArrayList<Functor>(Arrays.asList(writeFunctors));
        int numHeaders = headers.length;
        int numClasses = classes.size();
        if (numClasses != numHeaders) {
            log.warn("Header count=" + numHeaders + " but classes count=" + numClasses);
        }
        int numWrite = writeFunctors.length;
        if (numWrite > 0 && numWrite != numHeaders) {
            log.warn("Header count=" + numHeaders + " but writeFunctor count=" + numWrite);
        }
        int numRead = readFunctors.length;
        if (numRead > 0 && numRead != numHeaders) {
            log.warn("Header count=" + numHeaders + " but readFunctor count=" + numRead);
        }
    }
