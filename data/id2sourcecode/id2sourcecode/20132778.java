    @Primitive
    public static Value unix_isatty(final CodeRunner ctxt, final Value fd) throws Fail.Exception {
        final Channel ch = ctxt.getContext().getChannel(fd.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "iastty", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final OutputStream os = ch.asOutputStream();
        return (os == System.out) || (os == System.err) ? Value.TRUE : Value.FALSE;
    }
