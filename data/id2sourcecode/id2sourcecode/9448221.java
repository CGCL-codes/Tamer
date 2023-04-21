    private static long computeStructuralUID(ObjectStreamClass_1_3_1 osc, Class cl) {
        ByteArrayOutputStream devnull = new ByteArrayOutputStream(512);
        long h = 0;
        try {
            if ((!java.io.Serializable.class.isAssignableFrom(cl)) || (cl.isInterface())) {
                return 0;
            }
            if (java.io.Externalizable.class.isAssignableFrom(cl)) {
                return 1;
            }
            MessageDigest md = MessageDigest.getInstance("SHA");
            DigestOutputStream mdo = new DigestOutputStream(devnull, md);
            DataOutputStream data = new DataOutputStream(mdo);
            Class parent = cl.getSuperclass();
            if ((parent != null)) {
                data.writeLong(computeStructuralUID(lookup(parent), parent));
            }
            if (osc.hasWriteObject()) data.writeInt(2); else data.writeInt(1);
            ObjectStreamField[] fields = osc.getFields();
            int numNonNullFields = 0;
            for (int i = 0; i < fields.length; i++) if (fields[i].getField() != null) numNonNullFields++;
            Field[] field = new java.lang.reflect.Field[numNonNullFields];
            for (int i = 0, fieldNum = 0; i < fields.length; i++) {
                if (fields[i].getField() != null) {
                    field[fieldNum++] = fields[i].getField();
                }
            }
            if (field.length > 1) Arrays.sort(field, compareMemberByName);
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                int m = f.getModifiers();
                data.writeUTF(f.getName());
                data.writeUTF(getSignature(f.getType()));
            }
            data.flush();
            byte hasharray[] = md.digest();
            for (int i = 0; i < Math.min(8, hasharray.length); i++) {
                h += (long) (hasharray[i] & 255) << (i * 8);
            }
        } catch (IOException ignore) {
            h = -1;
        } catch (NoSuchAlgorithmException complain) {
            throw new SecurityException(complain.getMessage());
        }
        return h;
    }
