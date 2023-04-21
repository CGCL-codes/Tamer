    private Serializable readArray(InputStream is, int offset, Class clz) {
        if (getLogger().isDebugEnabled() && Trace.isLow()) {
            getLogger().debug("readArray( " + is + ", " + offset + ", " + clz + " )");
        }
        int len = is.read_long();
        if (getLogger().isDebugEnabled() && Trace.isMedium()) {
            getLogger().debug("writeArray: reading array of length = " + len);
        }
        Class cmpt = clz.getComponentType();
        if (getLogger().isDebugEnabled() && Trace.isMedium()) {
            getLogger().debug("writeArray: reading array of type '" + cmpt.getName() + "'");
        }
        if (cmpt.isPrimitive()) {
            if (cmpt.equals(boolean.class)) {
                boolean[] value = new boolean[len];
                is.read_boolean_array(value, 0, len);
                return value;
            } else if (cmpt.equals(byte.class)) {
                byte[] value = new byte[len];
                is.read_octet_array(value, 0, len);
                return value;
            } else if (cmpt.equals(short.class)) {
                short[] value = new short[len];
                is.read_short_array(value, 0, len);
                return value;
            } else if (cmpt.equals(int.class)) {
                int[] value = new int[len];
                is.read_long_array(value, 0, len);
                return value;
            } else if (cmpt.equals(long.class)) {
                long[] value = new long[len];
                is.read_longlong_array(value, 0, len);
                return value;
            } else if (cmpt.equals(float.class)) {
                float[] value = new float[len];
                is.read_float_array(value, 0, len);
                return value;
            } else if (cmpt.equals(double.class)) {
                double[] value = new double[len];
                is.read_double_array(value, 0, len);
                return value;
            } else if (cmpt.equals(char.class)) {
                char[] value = new char[len];
                is.read_wchar_array(value, 0, len);
                return value;
            } else {
                throw new Error("Unknown primitive type");
            }
        } else {
            Object[] value = (Object[]) Array.newInstance(cmpt, len);
            if (getLogger().isDebugEnabled() && Trace.isMedium()) {
                getLogger().debug("writeArray: array of type '" + cmpt.getName() + "' to be read");
            }
            boolean doPop = addReadIndirect(is, offset, value);
            for (int i = 0; i < len; ++i) {
                Object obj = readStreamValue(is, cmpt);
                if (getLogger().isDebugEnabled() && Trace.isMedium()) {
                    getLogger().debug("writeArray: array[ " + i + " ] = " + obj);
                }
                value[i] = obj;
            }
            if (doPop) {
                popReadIndirect();
            }
            return value;
        }
    }
