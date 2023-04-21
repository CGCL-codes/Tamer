    public void setValues(final Object values, final IListener<SplitBlobEvent> clientListener) {
        if (values.getClass().isArray() == false) {
            throw new IllegalArgumentException("Only arrays are supported");
        }
        int rank = SplitBlobUtils.getRank(values.getClass());
        if (rank == 0) {
            throw new IllegalArgumentException();
        }
        Class<?> componentType = SplitBlobUtils.getComponentType(values.getClass());
        this.ptype = SplitBlobUtils.extractType(componentType);
        int[] lengths = SplitBlobUtils.getShape(values, rank);
        int[] fromPos = new int[rank];
        this.elmtCount = SplitBlobUtils.numberOfElements(lengths);
        this.shape = encodeShape(lengths);
        preinitParts();
        IAccessor reader = new MultiArrayJava(values);
        IBlobPartAccessor writer = getAccessor();
        writer.setPartListener(clientListener);
        switch(ptype) {
            case PDOUBLE:
                rwValuesDouble(fromPos, lengths, reader, writer, null);
                break;
            case PFLOAT:
                rwValuesFloat(fromPos, lengths, reader, writer, null);
                break;
            case PLONG:
                rwValuesLong(fromPos, lengths, reader, writer, null);
                break;
            case PINTEGER:
                rwValuesInteger(fromPos, lengths, reader, writer, null);
                break;
            case PSHORT:
                rwValuesShort(fromPos, lengths, reader, writer, null);
                break;
            case PBYTE:
                rwValuesByte(fromPos, lengths, reader, writer, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
        writer.close();
    }
