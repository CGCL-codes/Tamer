    private static final long computeInterfaceVersionUID(Class c) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DataOutputStream dos = new DataOutputStream(new DigestOutputStream(new ByteArrayOutputStream(512), md));
            Constructor[] constructors = c.getConstructors();
            Arrays.sort(constructors, new Comparator() {

                public int compare(Object o1, Object o2) {
                    int rc = ((Constructor) o1).getName().compareTo(((Constructor) o2).getName());
                    if (rc == 0) rc = getSignature((Constructor) o1).compareTo(getSignature((Constructor) o2));
                    return rc;
                }
            });
            for (int i = 0; i < constructors.length; i++) {
                dos.writeUTF("<init>");
                dos.writeUTF(getSignature(constructors[i]));
            }
            Method[] methods = c.getMethods();
            Arrays.sort(methods, new Comparator() {

                public int compare(Object o1, Object o2) {
                    int rc = ((Method) o1).getName().compareTo(((Method) o2).getName());
                    if (rc == 0) rc = getSignature((Method) o1).compareTo(getSignature((Method) o2));
                    return rc;
                }
            });
            for (int i = 0; i < methods.length; i++) {
                int mod = methods[i].getModifiers();
                if (!(Modifier.isStatic(mod) || Modifier.isFinal(mod))) {
                    dos.writeUTF(methods[i].getName());
                    dos.writeUTF(getSignature(methods[i]));
                }
            }
            dos.flush();
            byte[] hash = md.digest();
            long uid = 0;
            for (int i = 0; i < Math.min(8, hash.length); i++) uid += (long) (hash[i] & 0xff) << (i * 8);
            return uid;
        } catch (IOException e) {
            throw new ProgrammingErrorException("shouldn't happen: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new ProgrammingErrorException("shouldn't happen: " + e.getMessage());
        }
    }
