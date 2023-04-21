    private void computeHash(ClassMapper.EntryField[] fields) throws MarshalException {
        hash = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DataOutputStream out = new DataOutputStream(new DigestOutputStream(new ByteArrayOutputStream(127), md));
            if (superclass != null) out.writeLong(superclass.hash);
            out.writeUTF(name);
            int startDeclaredFields = superclass != null ? superclass.numFields : 0;
            for (int i = startDeclaredFields; i < fields.length; i++) {
                out.writeUTF(fields[i].field.getName());
                out.writeUTF(fields[i].field.getType().getName());
            }
            out.flush();
            byte[] digest = md.digest();
            for (int i = Math.min(8, digest.length); --i >= 0; ) {
                hash += ((long) (digest[i] & 0xFF)) << (i * 8);
            }
        } catch (Exception e) {
            throw new MarshalException("Unable to calculate type hash for " + name, e);
        }
    }
