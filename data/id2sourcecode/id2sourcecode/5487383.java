    void write(TransmissionChannel transmissionChannel, Tuple tuple) throws IOException, MigrationUnsupported {
        Marshaler marshaler = transmissionChannel.marshaler;
        marshaler.writeStringLine(TUPLE_S);
        marshaler.writeStringLine(PARAMS_S);
        marshaler.writeStringLine(ID_S);
        marshaler.writeStringLine(tuple.getTupleId());
        marshaler.writeStringLine(HRETRIEVED_S);
        if (tuple.isHandleRetrieved()) {
            marshaler.writeStringLine("TRUE");
            Iterator<String> iterator = tuple.getAlreadyRetrieved();
            StringBuffer alreadyRetrieved = new StringBuffer();
            int n = 0;
            while (iterator.hasNext()) {
                alreadyRetrieved.append(iterator.next() + "\n");
                ++n;
            }
            marshaler.writeStringLine("" + n);
            marshaler.writeBytes(alreadyRetrieved.toString());
        } else {
            marshaler.writeStringLine("FALSE");
        }
        if (tuple.getMatched() != null) {
            marshaler.writeStringLine(MATCHED_S);
            write(transmissionChannel, tuple.getMatched());
        }
        marshaler.writeStringLine(END_S + PARAMS_S);
        marshaler.writeStringLine(ITEMS_S);
        Enumeration<Object> tupleitems = tuple.tupleElements();
        while (tupleitems.hasMoreElements()) {
            Object item = tupleitems.nextElement();
            if (item instanceof TupleItem) {
                TupleItem tupleItem = (TupleItem) item;
                if (tupleItem.isFormal()) {
                    marshaler.writeStringLine(FORMAL_S);
                } else {
                    marshaler.writeStringLine(ACTUAL_S);
                    marshaler.writeStringLine(ITEM_S);
                }
                if (writeSpecial(transmissionChannel, item)) {
                    if (!tupleItem.isFormal()) marshaler.writeStringLine(END_S + ITEM_S);
                    continue;
                }
                marshaler.writeStringLine(tupleItem.getClass().getName());
                if (!tupleItem.isFormal()) {
                    marshaler.writeStringLine(tupleItem.toString());
                    marshaler.writeStringLine(END_S + ITEM_S);
                }
            } else {
                boolean isFormal = (item instanceof Class);
                if (isFormal) {
                    marshaler.writeStringLine(FORMAL_S);
                } else {
                    marshaler.writeStringLine(ACTUAL_S);
                    marshaler.writeStringLine(ITEM_S);
                }
                if (writeSpecial(transmissionChannel, item)) {
                    if (!isFormal) marshaler.writeStringLine(END_S + ITEM_S);
                    continue;
                }
                if (isFormal) {
                    marshaler.writeStringLine(item.toString());
                } else {
                    marshaler.writeStringLine(item.getClass().getName());
                }
                if (!isFormal) {
                    marshaler.writeStringLine(item.toString());
                    marshaler.writeStringLine(END_S + ITEM_S);
                }
            }
        }
        marshaler.writeStringLine(END_S + ITEMS_S);
        marshaler.writeStringLine(END_S + TUPLE_S);
    }
