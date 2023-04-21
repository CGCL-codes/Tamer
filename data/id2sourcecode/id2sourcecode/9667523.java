    @Primitive
    public static Value caml_ml_open_descriptor_out(final CodeRunner ctxt, final Value fd) {
        final Block c = Block.createCustom(Custom.CHANNEL_SIZE, Custom.CHANNEL_OPS);
        c.setCustom(ctxt.getContext().getChannel(fd.asLong()));
        return Value.createFromBlock(c);
    }
