package org.apache.harmony.unpack200.bytecode.forms;

import org.apache.harmony.pack200.Pack200Exception;
import org.apache.harmony.unpack200.SegmentConstantPool;
import org.apache.harmony.unpack200.bytecode.ByteCode;
import org.apache.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.harmony.unpack200.bytecode.OperandManager;

/**
 * Abstract class of all ByteCodeForms which add a nested entry from the
 * globalConstantPool.
 */
public abstract class ReferenceForm extends ByteCodeForm {

    public ReferenceForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    protected abstract int getPoolID();

    protected abstract int getOffset(OperandManager operandManager);

    protected void setNestedEntries(ByteCode byteCode, OperandManager operandManager, int offset) throws Pack200Exception {
        final SegmentConstantPool globalPool = operandManager.globalConstantPool();
        ClassFileEntry[] nested = null;
        nested = new ClassFileEntry[] { globalPool.getConstantPoolEntry(getPoolID(), offset) };
        if (nested[0] == null) {
            throw new NullPointerException("Null nested entries are not allowed");
        }
        byteCode.setNested(nested);
        byteCode.setNestedPositions(new int[][] { { 0, 2 } });
    }

    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        final int offset = getOffset(operandManager);
        try {
            setNestedEntries(byteCode, operandManager, offset);
        } catch (final Pack200Exception ex) {
            throw new Error("Got a pack200 exception. What to do?");
        }
    }
}
