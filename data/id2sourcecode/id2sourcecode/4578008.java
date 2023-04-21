    public static final Object typeCast(Object obj, Class type, Class toType) throws NoSupportTypeCastException, NumberFormatException, IllegalArgumentException {
        if (obj == null) return null;
        if (isSameType(type, toType)) return obj;
        if (type.isAssignableFrom(toType)) {
            if (!java.util.Date.class.isAssignableFrom(type)) return toType.cast(obj);
        }
        if (type == byte[].class && toType == String.class) {
            return new String((byte[]) obj);
        } else if (type == String.class && toType == byte[].class) {
            return ((String) obj).getBytes();
        } else if (type == byte[].class && File.class.isAssignableFrom(toType)) {
            Object[] object = (Object[]) obj;
            java.io.ByteArrayInputStream byteIn = null;
            java.io.FileOutputStream fileOut = null;
            try {
                byteIn = new java.io.ByteArrayInputStream((byte[]) object[0]);
                fileOut = new java.io.FileOutputStream((File) object[1]);
                byte v[] = new byte[1024];
                int i = 0;
                while ((i = byteIn.read(v)) > 0) {
                    fileOut.write(v, 0, i);
                }
                fileOut.flush();
                return object[1];
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteIn != null) byteIn.close();
                } catch (Exception e) {
                }
                try {
                    if (fileOut != null) fileOut.close();
                } catch (Exception e) {
                }
            }
        } else if (File.class.isAssignableFrom(toType) && toType == byte[].class) {
            java.io.FileInputStream in = null;
            java.io.ByteArrayOutputStream out = null;
            try {
                int i = 0;
                in = new FileInputStream((File) obj);
                out = new ByteArrayOutputStream();
                byte v[] = new byte[1024];
                while ((i = in.read(v)) > 0) {
                    out.write(v, 0, i);
                }
                return out.toByteArray();
            } catch (Exception e) {
            } finally {
                try {
                    if (in != null) in.close();
                } catch (Exception e) {
                }
                try {
                    if (out != null) out.close();
                } catch (Exception e) {
                }
            }
        } else if (type.isArray() && !toType.isArray() || !type.isArray() && toType.isArray()) {
            throw new IllegalArgumentException(new StringBuffer("�����޷�ת��,��֧��[").append(type.getName()).append("]��[").append(toType.getName()).append("]ת��").toString());
        }
        Object arrayObj;
        if (!type.isArray()) {
            arrayObj = basicTypeCast(obj, type, toType);
        } else {
            arrayObj = arrayTypeCast(obj, type, toType);
        }
        return arrayObj;
    }
