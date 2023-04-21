package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class represents a (PC offset, line number) pair, i.e., a line number in
 * the source that corresponds to a relative address in the byte code. This
 * is used for debugging purposes.
 *
 * @version $Id: LineNumber.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @see     LineNumberTable
 */
public final class LineNumber implements Cloneable, Node, Serializable {

    private int start_pc;

    private int line_number;

    /**
     * Initialize from another object.
     */
    public LineNumber(LineNumber c) {
        this(c.getStartPC(), c.getLineNumber());
    }

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    LineNumber(DataInputStream file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    /**
     * @param start_pc Program Counter (PC) corresponds to
     * @param line_number line number in source file
     */
    public LineNumber(int start_pc, int line_number) {
        this.start_pc = start_pc;
        this.line_number = line_number;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    public void accept(Visitor v) {
        v.visitLineNumber(this);
    }

    /**
     * Dump line number/pc pair to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException
     */
    public final void dump(DataOutputStream file) throws IOException {
        file.writeShort(start_pc);
        file.writeShort(line_number);
    }

    /**
     * @return Corresponding source line
     */
    public final int getLineNumber() {
        return line_number;
    }

    /**
     * @return PC in code
     */
    public final int getStartPC() {
        return start_pc;
    }

    /**
     * @param line_number the source line number
     */
    public final void setLineNumber(int line_number) {
        this.line_number = line_number;
    }

    /**
     * @param start_pc the pc for this line number
     */
    public final void setStartPC(int start_pc) {
        this.start_pc = start_pc;
    }

    /**
     * @return String representation
     */
    public final String toString() {
        return "LineNumber(" + start_pc + ", " + line_number + ")";
    }

    /**
     * @return deep copy of this object
     */
    public LineNumber copy() {
        try {
            return (LineNumber) clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }
}
