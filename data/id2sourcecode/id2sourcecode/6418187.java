    private static long _computeSerialVersionUID(Class cl) {
        ByteArrayOutputStream devnull = new ByteArrayOutputStream(512);
        long h = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DigestOutputStream mdo = new DigestOutputStream(devnull, md);
            DataOutputStream data = new DataOutputStream(mdo);
            data.writeUTF(cl.getName());
            int classaccess = cl.getModifiers();
            classaccess &= (Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT);
            Method[] method = cl.getDeclaredMethods();
            if ((classaccess & Modifier.INTERFACE) != 0) {
                classaccess &= (~Modifier.ABSTRACT);
                if (method.length > 0) {
                    classaccess |= Modifier.ABSTRACT;
                }
            }
            data.writeInt(classaccess);
            if (!cl.isArray()) {
                Class interfaces[] = cl.getInterfaces();
                Arrays.sort(interfaces, compareClassByName);
                for (int i = 0; i < interfaces.length; i++) {
                    data.writeUTF(interfaces[i].getName());
                }
            }
            Field[] field = cl.getDeclaredFields();
            Arrays.sort(field, compareMemberByName);
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                int m = f.getModifiers();
                if (Modifier.isPrivate(m) && (Modifier.isTransient(m) || Modifier.isStatic(m))) continue;
                data.writeUTF(f.getName());
                data.writeInt(m);
                data.writeUTF(getSignature(f.getType()));
            }
            if (hasStaticInitializer(cl)) {
                data.writeUTF("<clinit>");
                data.writeInt(Modifier.STATIC);
                data.writeUTF("()V");
            }
            MethodSignature[] constructors = MethodSignature.removePrivateAndSort(cl.getDeclaredConstructors());
            for (int i = 0; i < constructors.length; i++) {
                MethodSignature c = constructors[i];
                String mname = "<init>";
                String desc = c.signature;
                desc = desc.replace('/', '.');
                data.writeUTF(mname);
                data.writeInt(c.member.getModifiers());
                data.writeUTF(desc);
            }
            MethodSignature[] methods = MethodSignature.removePrivateAndSort(method);
            for (int i = 0; i < methods.length; i++) {
                MethodSignature m = methods[i];
                String desc = m.signature;
                desc = desc.replace('/', '.');
                data.writeUTF(m.member.getName());
                data.writeInt(m.member.getModifiers());
                data.writeUTF(desc);
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
