    protected UIntValuator(AttributeTable table, long upper) throws BadFormException, VisADException, RemoteException {
        super(table, 0, upper);
        floatFold = (upper + 1) / 2;
        doubleFold = (upper + 1) / 2;
    }
