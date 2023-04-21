    @SuppressWarnings("unchecked")
    private static Class<?> generateProxyClass(Class<?> actorClass, final ActorClassDescriptor acd) throws NoSuchMethodException {
        BeanClassDescriptor bcd = acd.getBeanClassDescriptor();
        String className = String.format("%s__ACTORPROXY", actorClass.getName());
        final String classNameInternal = className.replace('.', '/');
        String classNameDescriptor = "L" + classNameInternal + ";";
        final Type actorState = Type.getType(acd.getConcurrencyModel().isMultiThreadingCapable() ? MultiThreadedActorState.class : SingleThreadedActorState.class);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;
        cw.visit(codeVersion, Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_SUPER + Opcodes.ACC_SYNTHETIC, classNameInternal, null, Type.getInternalName(actorClass), new String[] { "org/actorsguildframework/internal/ActorProxy" });
        cw.visitSource(null, null);
        {
            for (int i = 0; i < acd.getMessageCount(); i++) cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, String.format(MESSAGE_CALLER_NAME_FORMAT, i), "Lorg/actorsguildframework/internal/MessageCaller;", "Lorg/actorsguildframework/internal/MessageCaller<*>;", null).visitEnd();
            cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, "actorState__ACTORPROXY", actorState.getDescriptor(), null, null).visitEnd();
        }
        BeanCreator.writePropFields(bcd, cw);
        {
            mv = cw.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            for (int i = 0; i < acd.getMessageCount(); i++) {
                Class<?> caller = createMessageCaller(acd.getMessage(i).getOwnerClass(), acd.getMessage(i).getMethod());
                String mcName = Type.getInternalName(caller);
                mv.visitTypeInsn(Opcodes.NEW, mcName);
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, mcName, "<init>", "()V");
                mv.visitFieldInsn(Opcodes.PUTSTATIC, classNameInternal, String.format(MESSAGE_CALLER_NAME_FORMAT, i), "Lorg/actorsguildframework/internal/MessageCaller;");
            }
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        BeanCreator.writeConstructor(actorClass, bcd, classNameInternal, cw, new BeanCreator.SnippetWriter() {

            @Override
            public void write(MethodVisitor mv) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitTypeInsn(Opcodes.NEW, actorState.getInternalName());
                mv.visitInsn(Opcodes.DUP);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, actorState.getInternalName(), "<init>", "(Lorg/actorsguildframework/internal/Controller;Lorg/actorsguildframework/Actor;)V");
                mv.visitFieldInsn(Opcodes.PUTFIELD, classNameInternal, "actorState__ACTORPROXY", actorState.getDescriptor());
            }
        });
        BeanCreator.writePropAccessors(bcd, classNameInternal, cw);
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getState__ACTORPROXYMETHOD", "()Lorg/actorsguildframework/internal/ActorState;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, classNameInternal, "actorState__ACTORPROXY", actorState.getDescriptor());
            mv.visitInsn(Opcodes.ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", classNameDescriptor, null, l0, l1, 0);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        for (int i = 0; i < acd.getMessageCount(); i++) {
            MessageImplDescriptor mid = acd.getMessage(i);
            Method method = mid.getMethod();
            String simpleDescriptor = Type.getMethodDescriptor(method);
            String genericSignature = GenericTypeHelper.getSignature(method);
            writeProxyMethod(classNameInternal, classNameDescriptor, cw, i, actorState, acd.getConcurrencyModel(), mid, method, simpleDescriptor, genericSignature);
            writeSuperProxyMethod(actorClass, classNameDescriptor, cw, method, simpleDescriptor, genericSignature, !acd.getConcurrencyModel().isMultiThreadingCapable());
        }
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_SYNCHRONIZED, "toString", "()Ljava/lang/String;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
            mv.visitInsn(Opcodes.ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", classNameDescriptor, null, l0, l1, 0);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        try {
            return (Class<? extends ActorProxy>) GenerationUtils.loadClass(className, cw.toByteArray());
        } catch (Exception e) {
            throw new ConfigurationException("Failure loading ActorProxy", e);
        }
    }
