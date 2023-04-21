    public static void dump(JavaClass clazz, ZipOutputStream zos) throws Exception {
        if (!dest.contains(clazz)) return;
        ConstantPoolGen newcpg = new ConstantPoolGen(clazz.getConstantPool());
        ClassGen cg = new ClassGen(clazz);
        InstructionFactory factory = new InstructionFactory(cg, newcpg);
        cg.setMajor(46);
        cg.setMinor(0);
        cg.setConstantPool(newcpg);
        boolean isconstructed = false;
        Method[] methods = getMethods(clazz);
        for (int i = 0; i < methods.length; i++) if (dest.contains(methods[i]) && methods[i].getName().equals("<init>")) isconstructed = true;
        Field[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            if ((!dest.contains(fields[i]) && fields[i].isStatic()) || ((!(constructed.contains(clazz))) && !fields[i].isStatic())) {
                System.out.println("  pruning field " + clazz.getClassName() + "." + fields[i].getName());
            }
        }
        int numMethods = 0;
        boolean good = false;
        for (int i = 0; i < methods.length; i++) {
            if (dest.contains(methods[i]) && (isconstructed || methods[i].isStatic())) {
                good = true;
            } else {
                if (methods[i].getCode() == null) {
                    System.out.println("  empty codeblock: " + clazz.getClassName() + "." + methods[i].getName());
                } else {
                    System.out.println("  pruning " + (isconstructed ? "" : "unconstructed") + " method " + clazz.getClassName() + "." + methods[i].getName());
                    if (deleteMethods) {
                        cg.removeMethod(methods[i]);
                        continue;
                    }
                    MethodGen mg = new MethodGen(methods[i], clazz.getClassName(), newcpg);
                    mg.removeExceptions();
                    InstructionList il = new InstructionList();
                    mg.setInstructionList(il);
                    InstructionHandle ih_0 = il.append(factory.createNew("java.lang.UnsatisfiedLinkError"));
                    il.append(InstructionConstants.DUP);
                    il.append(factory.createInvoke("java.lang.UnsatisfiedLinkError", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
                    il.append(InstructionConstants.ATHROW);
                    mg.setMaxStack();
                    mg.setMaxLocals();
                    mg.removeExceptions();
                    mg.removeLocalVariables();
                    mg.removeExceptionHandlers();
                    mg.removeLineNumbers();
                    cg.replaceMethod(methods[i], mg.getMethod());
                    il.dispose();
                }
            }
        }
        good = true;
        if (!good && !clazz.isAbstract() && !clazz.isInterface()) {
            System.out.println("DROPPING " + clazz.getClassName());
            JavaClass[] ifaces = clazz.getInterfaces();
            String[] ifacestrings = new String[ifaces.length];
            for (int i = 0; i < ifaces.length; i++) ifacestrings[i] = ifaces[i].getClassName();
            cg = new ClassGen(clazz.getClassName(), clazz.getSuperClass().getClassName(), clazz.getFileName(), clazz.getAccessFlags(), ifacestrings, newcpg);
        } else {
            System.out.println("dumping " + clazz.getClassName());
        }
        FilterOutputStream noclose = new FilterOutputStream(zos) {

            public void close() throws IOException {
                flush();
            }
        };
        zos.putNextEntry(new ZipEntry(clazz.getClassName().replace('.', '/') + ".class"));
        cg.getJavaClass().dump(noclose);
        noclose.flush();
    }
