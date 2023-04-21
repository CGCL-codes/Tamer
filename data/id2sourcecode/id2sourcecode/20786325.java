    public static long calculeSUID(Clazz clazz) throws ClassNotFoundException {
        try {
            if (clazz.hasSerialVersionUID()) {
                return clazz.getExistantSerialVersionUID();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream daos = new DataOutputStream(baos);
            daos.writeUTF(clazz.getName());
            int classMask = Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT;
            daos.writeInt(clazz.getModifiers() & classMask);
            for (String clazzInterface : clazz.getSortedInterfaces()) {
                daos.writeUTF(clazzInterface);
            }
            int fieldMask = Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE | Modifier.TRANSIENT;
            for (Field field : clazz.getSortedValidFields()) {
                daos.writeUTF(field.getName());
                daos.writeInt(field.getModifiers() & fieldMask);
                daos.writeUTF(field.getSignature());
            }
            int methodMask = Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC | Modifier.FINAL | Modifier.SYNCHRONIZED | Modifier.NATIVE | Modifier.ABSTRACT | Modifier.STRICT;
            for (Method constructor : clazz.getSortedValidConstructors()) {
                daos.writeUTF(constructor.getName());
                daos.writeInt(constructor.getModifiers() & methodMask);
                daos.writeUTF(constructor.getSignature());
            }
            for (Method methods : clazz.getSortedValidMethods()) {
                daos.writeUTF(methods.getName());
                daos.writeInt(methods.getModifiers() & methodMask);
                daos.writeUTF(methods.getSignature().replace('/', '.'));
            }
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] sha = md.digest(baos.toByteArray());
            long hash = 0;
            for (int i = 7; i >= 0; i--) {
                hash = (hash << 8) | (sha[i] & 0xFF);
            }
            return hash;
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
    }
