    public void encode(final SWFEncoder coder, final Context context) throws IOException {
        coder.writeByte(FillStyleTypes.FOCAL_GRADIENT);
        startTransform.encode(coder, context);
        endTransform.encode(coder, context);
        coder.writeByte(count | spread | interpolation);
        for (final MorphGradient gradient : gradients) {
            gradient.encode(coder, context);
        }
        coder.writeShort(startFocalPoint);
        coder.writeShort(endFocalPoint);
    }
