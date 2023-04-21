    private static synchronized Long findHash(Class clazz, boolean marshaling) throws MarshalException, UnusableEntryException {
        if (classHashes == null) classHashes = new WeakHashMap();
        Long hash = (Long) classHashes.get(clazz);
        if (hash == null) {
            try {
                Field[] fields = getFields(clazz);
                MessageDigest md = MessageDigest.getInstance("SHA");
                DataOutputStream out = new DataOutputStream(new DigestOutputStream(new ByteArrayOutputStream(127), md));
                Class c = clazz.getSuperclass();
                if (c != Object.class) out.writeLong(findHash(c, marshaling).longValue());
                for (int i = 0; i < fields.length; i++) {
                    if (!usableField(fields[i])) continue;
                    out.writeUTF(fields[i].getName());
                    out.writeUTF(fields[i].getType().getName());
                }
                out.flush();
                byte[] digest = md.digest();
                long h = 0;
                for (int i = Math.min(8, digest.length); --i >= 0; ) {
                    h += ((long) (digest[i] & 0xFF)) << (i * 8);
                }
                hash = new Long(h);
            } catch (Exception e) {
                if (marshaling) throw throwNewMarshalException("Exception calculating entry class hash for " + clazz, e); else throw throwNewUnusableEntryException("Exception calculating entry class hash for " + clazz, e);
            }
            classHashes.put(clazz, hash);
        }
        return hash;
    }
