    public void store(final SerializerOutputStream out) throws ViewCompilerException {
        super.store(out);
        try {
            out.writeByte((byte) 1);
            out.writeNullableString(ViewCompiler.escape(label));
            out.writeNullableString(ViewCompiler.escape(labelExtra));
            out.writeBoolean(readOnly);
            out.writeNullableString(bind);
            out.writeInt(size);
            out.writeInt(constraints);
            out.writeInt((Integer) ViewCompiler.APPEARANCES.get(appearance));
            out.writeNullableString(ViewCompiler.escape(text));
        } catch (SerializationException e) {
            throw new ViewCompilerException(e);
        }
    }
