    public static long getHashCode(Class c) {
        Class of = c.isArray() ? c.getComponentType() : null;
        if (c.isArray() && ((!Serializable.class.isAssignableFrom(of) || of.isPrimitive() || Remote.class.isAssignableFrom(of)))) return 0;
        if (!Serializable.class.isAssignableFrom(c)) return 0;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            Class superClass = c.getSuperclass();
            if (superClass != null) out.writeLong(getHashCode(superClass));
            int writeObjectPresentCode;
            try {
                c.getDeclaredMethod("writeObject", new Class[] { ObjectOutputStream.class });
                writeObjectPresentCode = 2;
            } catch (NoSuchMethodException e) {
                writeObjectPresentCode = 1;
            }
            out.writeInt(writeObjectPresentCode);
            Field[] fields = c.getDeclaredFields();
            Arrays.sort(fields, new Comparator() {

                public int compare(Object a, Object b) {
                    Field fa = (Field) a;
                    Field fb = (Field) b;
                    return fa.getName().compareTo(fb.getName());
                }
            });
            Field f;
            for (int i = 0; i < fields.length; i++) {
                f = fields[i];
                if ((f.getModifiers() & NON_WRITABLE) == 0) {
                    out.writeUTF(f.getName());
                    out.writeUTF(getDescriptor(f.getType()));
                }
            }
            out.flush();
            out.close();
            MessageDigest shaDigest;
            try {
                shaDigest = MessageDigest.getInstance("SHA");
            } catch (Exception ex) {
                throw new InternalError("SHA digesting algorithm is not available");
            }
            byte[] sha = shaDigest.digest(bout.toByteArray());
            long hash = 0;
            for (int i = 0; i < Math.min(8, sha.length); i++) {
                hash += (long) (sha[i] & 255) << (i * 8);
            }
            return hash;
        } catch (IOException ioex) {
            throw new Unexpected(ioex);
        }
    }
