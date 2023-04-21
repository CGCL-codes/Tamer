    private final void applyDestinationTextureCoordinateTransform(float[] texCoord0, float[] texCoord1, float[] texCoord2, float[] texCoord3) {
        final float umin = Math.min(texCoord0[0], Math.min(texCoord1[0], Math.min(texCoord2[0], texCoord3[0])));
        final float umax = Math.max(texCoord0[0], Math.max(texCoord1[0], Math.max(texCoord2[0], texCoord3[0])));
        final float vmin = Math.min(texCoord0[1], Math.min(texCoord1[1], Math.min(texCoord2[1], texCoord3[1])));
        final float vmax = Math.max(texCoord0[1], Math.max(texCoord1[1], Math.max(texCoord2[1], texCoord3[1])));
        final float ucenter = (umin + umax) / 2;
        final float vcenter = (vmin + vmax) / 2;
        final int uindex = (int) Math.floor(ucenter * horizontalSubdivisionCount);
        final int vindex = (int) Math.floor(vcenter * verticalSubdivisionCount);
        final float uoffset = uindex / (float) horizontalSubdivisionCount;
        final float voffset = vindex / (float) verticalSubdivisionCount;
        texCoord0[0] = Math.min(1.0f, Math.max(0.0f, (texCoord0[0] - uoffset) * horizontalSubdivisionCount));
        texCoord0[1] = Math.min(1.0f, Math.max(0.0f, (texCoord0[1] - voffset) * verticalSubdivisionCount));
        texCoord1[0] = Math.min(1.0f, Math.max(0.0f, (texCoord1[0] - uoffset) * horizontalSubdivisionCount));
        texCoord1[1] = Math.min(1.0f, Math.max(0.0f, (texCoord1[1] - voffset) * verticalSubdivisionCount));
        texCoord2[0] = Math.min(1.0f, Math.max(0.0f, (texCoord2[0] - uoffset) * horizontalSubdivisionCount));
        texCoord2[1] = Math.min(1.0f, Math.max(0.0f, (texCoord2[1] - voffset) * verticalSubdivisionCount));
        texCoord3[0] = Math.min(1.0f, Math.max(0.0f, (texCoord3[0] - uoffset) * horizontalSubdivisionCount));
        texCoord3[1] = Math.min(1.0f, Math.max(0.0f, (texCoord3[1] - voffset) * verticalSubdivisionCount));
    }
