    public static float[] getMinMax(File file) {
        float[] ret = { Float.MAX_VALUE, Float.MAX_VALUE * -1f };
        try {
            long s = file.length();
            FileChannel in = new FileInputStream(file).getChannel();
            ByteBuffer buffa = BufferUtil.newByteBuffer((int) s);
            in.read(buffa);
            buffa.rewind();
            FloatBuffer data = buffa.asFloatBuffer();
            for (int i = 0; i < data.capacity(); ++i) {
                float f = data.get(i);
                if (ret[0] > f) {
                    ret[0] = f;
                }
                if (ret[1] < f) {
                    ret[1] = f;
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return ret;
    }
