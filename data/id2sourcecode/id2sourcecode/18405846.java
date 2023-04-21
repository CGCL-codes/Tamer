    public static long computeSerialVersionUID(JavaClass clazz) {
        ByteArrayOutputStream devnull = new ByteArrayOutputStream(512);
        long h = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DigestOutputStream mdo = new DigestOutputStream(devnull, md);
            DataOutputStream data = new DataOutputStream(mdo);
            data.writeUTF(clazz.getClassName());
            int classaccess = clazz.getAccessFlags();
            classaccess &= (Constants.ACC_PUBLIC | Constants.ACC_FINAL | Constants.ACC_INTERFACE | Constants.ACC_ABSTRACT);
            Method[] method = clazz.getMethods();
            if ((classaccess & Constants.ACC_INTERFACE) != 0) {
                classaccess &= (~Constants.ACC_ABSTRACT);
                if (method.length > 0) {
                    classaccess |= Constants.ACC_ABSTRACT;
                }
            }
            data.writeInt(classaccess);
            String interfaces[] = clazz.getInterfaceNames();
            Arrays.sort(interfaces, compareStringByName);
            for (int i = 0; i < interfaces.length; i++) {
                data.writeUTF(interfaces[i]);
            }
            com.versant.lib.bcel.classfile.Field[] field = clazz.getFields();
            Arrays.sort(field, compareFieldByName);
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                int m = f.getAccessFlags();
                if ((f.isPrivate() && f.isStatic()) || (f.isPrivate() && f.isTransient())) {
                    continue;
                }
                data.writeUTF(f.getName());
                data.writeInt(m);
                data.writeUTF(f.getSignature());
            }
            if (hasStaticInitializer(clazz)) {
                data.writeUTF("<clinit>");
                data.writeInt(Constants.ACC_STATIC);
                data.writeUTF("()V");
            }
            Iterator nonPrivateConstructorsIter = removePrivateConstructorsAndSort(clazz).iterator();
            while (nonPrivateConstructorsIter.hasNext()) {
                Method m = (Method) nonPrivateConstructorsIter.next();
                String mname = "<init>";
                String desc = m.getSignature();
                desc = desc.replace('/', '.');
                data.writeUTF(mname);
                data.writeInt(m.getAccessFlags());
                data.writeUTF(desc);
            }
            Iterator nonPrivateAndNoConstructorsIter = removePrivateAndConstructorsAndSort(clazz).iterator();
            while (nonPrivateAndNoConstructorsIter.hasNext()) {
                Method m = (Method) nonPrivateAndNoConstructorsIter.next();
                String mname = m.getName();
                String desc = m.getSignature();
                desc = desc.replace('/', '.');
                data.writeUTF(mname);
                data.writeInt(m.getAccessFlags());
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
