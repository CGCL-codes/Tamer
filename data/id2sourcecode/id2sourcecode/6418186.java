    public static long computeStructuralUID(boolean hasWriteObject, Class cl) {
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
            if ((parent != null) && (parent != java.lang.Object.class)) {
                boolean hasWriteObjectFlag = false;
                Class[] args = { java.io.ObjectOutputStream.class };
                Method hasWriteObjectMethod = ObjectStreamClassUtil_1_3.getDeclaredMethod(parent, "writeObject", args, Modifier.PRIVATE, Modifier.STATIC);
                if (hasWriteObjectMethod != null) hasWriteObjectFlag = true;
                data.writeLong(ObjectStreamClassUtil_1_3.computeStructuralUID(hasWriteObjectFlag, parent));
            }
            if (hasWriteObject) data.writeInt(2); else data.writeInt(1);
            Field[] field = ObjectStreamClassUtil_1_3.getDeclaredFields(cl);
            Arrays.sort(field, compareMemberByName);
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                int m = f.getModifiers();
                if (Modifier.isTransient(m) || Modifier.isStatic(m)) continue;
                data.writeUTF(f.getName());
                data.writeUTF(getSignature(f.getType()));
            }
            data.flush();
            byte hasharray[] = md.digest();
            int minimum = Math.min(8, hasharray.length);
            for (int i = minimum; i > 0; i--) {
                h += (long) (hasharray[i] & 255) << (i * 8);
            }
        } catch (IOException ignore) {
            h = -1;
        } catch (NoSuchAlgorithmException complain) {
            throw new SecurityException(complain.getMessage());
        }
        return h;
    }
