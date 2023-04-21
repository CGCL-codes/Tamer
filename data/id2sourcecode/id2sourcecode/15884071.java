    private ReadWriteFields getReadWriteFields(Class<?> cls, MethodNode methodNode) {
        ReadWriteFields readWrites = new ReadWriteFields();
        InsnList mlist = methodNode.instructions;
        for (int i = 0; i < mlist.size(); i++) {
            AbstractInsnNode insn = mlist.get(i);
            int opCode = insn.getOpcode();
            switch(opCode) {
                case GETFIELD:
                    FieldInsnNode fieldGet = (FieldInsnNode) insn;
                    readWrites.read(fieldGet.name);
                    break;
                case PUTFIELD:
                    FieldInsnNode fieldPut = (FieldInsnNode) insn;
                    readWrites.write(fieldPut.name);
                    break;
                case GETSTATIC:
                    FieldInsnNode fieldStaticGet = (FieldInsnNode) insn;
                    readWrites.read(fieldStaticGet.name);
                    break;
                case PUTSTATIC:
                    FieldInsnNode fieldStaticPut = (FieldInsnNode) insn;
                    readWrites.write(fieldStaticPut.name);
                    break;
                case INVOKEVIRTUAL:
                    MethodInsnNode virtualMethod = (MethodInsnNode) insn;
                    readWrites.call(this.getMethod(cls, virtualMethod.name, virtualMethod.desc));
                    break;
                case INVOKESTATIC:
                    MethodInsnNode staticMethod = (MethodInsnNode) insn;
                    readWrites.call(this.getMethod(cls, staticMethod.name, staticMethod.desc));
                    break;
            }
        }
        return readWrites;
    }
