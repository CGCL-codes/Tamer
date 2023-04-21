    public void encode(final SWFEncoder coder, final Context context) throws IOException {
        coder.writeByte(type);
        transform.encode(coder, context);
        coder.writeByte(count | spread | interpolation);
        for (final Gradient gradient : gradients) {
            gradient.encode(coder, context);
        }
    }
