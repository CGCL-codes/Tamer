    @Primitive
    public static Value unix_dup(final CodeRunner ctxt, final Value fd) throws Fail.Exception {
        final Context c = ctxt.getContext();
        final Channel ch = c.getChannel(fd.asLong());
        if (ch != null) {
            return Value.createFromLong(c.addChannel(ch));
        } else {
            Unix.fail(ctxt, "dup", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
    }
