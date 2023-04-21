    @SuppressWarnings("unchecked")
    public <T> T getValues(final Class<T> clazz, final int[] fromPos, final int[] lengths, final IListener<SplitBlobEvent> clientListener) {
        if (fromPos.length != lengths.length) {
            throw new IllegalArgumentException();
        }
        Class<?> componentType = SplitBlobUtils.getComponentType(clazz);
        T values = (T) Array.newInstance(componentType, lengths);
        IAccessor writer = new MultiArrayJava(values);
        IBlobPartAccessor reader = getAccessor();
        reader.setPartListener(clientListener);
        final int rank = fromPos.length;
        final int[] toPosExcl = new int[rank];
        for (int i = 0; i < rank; i++) {
            toPosExcl[i] = fromPos[i] + lengths[i];
        }
        ITransformationIndex transf = new ITransformationIndex() {

            final int[] buff = new int[rank];

            @Override
            public int[] transform(int[] index) {
                for (int i = 0; i < buff.length; i++) {
                    buff[i] = index[i] - fromPos[i];
                }
                return buff;
            }
        };
        switch(ptype) {
            case PDOUBLE:
                rwValuesDouble(fromPos, toPosExcl, reader, writer, transf);
                break;
            case PFLOAT:
                rwValuesFloat(fromPos, toPosExcl, reader, writer, transf);
                break;
            case PLONG:
                rwValuesLong(fromPos, toPosExcl, reader, writer, transf);
                break;
            case PINTEGER:
                rwValuesInteger(fromPos, toPosExcl, reader, writer, transf);
                break;
            case PSHORT:
                rwValuesShort(fromPos, toPosExcl, reader, writer, transf);
                break;
            case PBYTE:
                rwValuesByte(fromPos, toPosExcl, reader, writer, transf);
                break;
            default:
                throw new IllegalArgumentException();
        }
        reader.close();
        return values;
    }
