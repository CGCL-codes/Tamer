    private int transformInvokevirtualsIntoGetfields(ClassFile classfile, CodeIterator iter, int pos) {
        ConstPool cp = classfile.getConstPool();
        int c = iter.byteAt(pos);
        if (c != Opcode.GETFIELD) {
            return pos;
        }
        int index = iter.u16bitAt(pos + 1);
        String fieldName = cp.getFieldrefName(index);
        String className = cp.getFieldrefClassName(index);
        if (!filter.handleReadAccess(className, fieldName)) {
            return pos;
        }
        String desc = "()" + cp.getFieldrefType(index);
        int read_method_index = cp.addMethodrefInfo(cp.getThisClassInfo(), EACH_READ_METHOD_PREFIX + fieldName, desc);
        iter.writeByte(Opcode.INVOKEVIRTUAL, pos);
        iter.write16bit(read_method_index, pos + 1);
        return pos;
    }
