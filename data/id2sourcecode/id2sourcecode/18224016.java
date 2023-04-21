        private Object transTypedArray(String[] array) {
            String type = array[0].substring(TYPED_ARRAY_SYMBOL.length());
            monitor.debug(" Transformed typed array: " + type);
            if (type.equals(TA_INT1) || type.equals(TA_INT1J)) {
                byte[] ret = new byte[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Byte.parseByte(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_INT2) || type.equals(TA_INT2J)) {
                short[] ret = new short[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Short.parseShort(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_INT4) || type.equals(TA_INT4J)) {
                int[] ret = new int[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Integer.parseInt(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_INT8) || type.equals(TA_INT8J)) {
                long[] ret = new long[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Long.parseLong(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_FLOAT)) {
                float[] ret = new float[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Float.parseFloat(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_DOUBLE)) {
                double[] ret = new double[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Double.parseDouble(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_DECIMAL)) {
                BigDecimal[] ret = new BigDecimal[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = new BigDecimal(array[i + 1]);
                }
                return ret;
            } else if (type.equals(TA_STRING)) {
                String[] ret = new String[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = array[i + 1];
                }
                return ret;
            } else if (type.equals(TA_BOOLEAN)) {
                boolean[] ret = new boolean[array.length - 1];
                for (int i = 0; i < ret.length; i++) {
                    String a = array[i + 1];
                    ret[i] = (a != null || a.equalsIgnoreCase("true"));
                }
                return ret;
            } else {
                throw new RuntimeException("Wrong array type symbol:" + type);
            }
        }
