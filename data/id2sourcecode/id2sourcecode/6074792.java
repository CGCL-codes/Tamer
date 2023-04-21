    private static void saveJCDModel(Tuple3f[] ve, Tuple3f[] no, TexCoord3f[] ta, TexCoord3f[] bi, TexCoord3f[] te, int[] indices, int validVertexCount, String name) {
        ByteBuffer modelInfo = ByteBuffer.allocate(ve.length * 60 + indices.length * 4 + 8);
        modelInfo.putInt(indices.length).putInt(validVertexCount);
        for (int i = 0; i < validVertexCount; i++) {
            modelInfo.putFloat(ve[i].getX()).putFloat(ve[i].getY()).putFloat(ve[i].getZ());
            modelInfo.putFloat(no[i].getX()).putFloat(no[i].getY()).putFloat(no[i].getZ());
            modelInfo.putFloat(ta[i].getS()).putFloat(ta[i].getT()).putFloat(ta[i].getP());
            modelInfo.putFloat(bi[i].getS()).putFloat(bi[i].getT()).putFloat(bi[i].getP());
            modelInfo.putFloat(te[i].getS()).putFloat(te[i].getT()).putFloat(te[i].getP());
        }
        int size = indices.length / 3;
        for (int i = 0; i < size; i++) modelInfo.putInt(indices[i * 3 + 0]).putInt(indices[i * 3 + 1]).putInt(indices[i * 3 + 2]);
        modelInfo.flip();
        try {
            new FileOutputStream(name + "JCD.jcd").getChannel().write(modelInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
