    protected void store(final SerializerOutputStream out) throws ViewCompilerException {
        try {
            out.writeByte((byte) 1);
            out.writeBoolean(readOnly);
            out.writeBoolean(saveOnAccept);
            out.writeBoolean(defaultButtons);
            final int count = items.size();
            out.writeInt(count);
            for (int i = 0; i < count; i++) {
                final Item item = (Item) items.get(i);
                item.store(out);
            }
        } catch (SerializationException e) {
            throw new ViewCompilerException(e);
        }
    }
