package com.android.dx.dex.file;

import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstUtf8;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import com.android.dx.util.Leb128Utils;
import java.io.PrintWriter;

/**
 * Representation of a field of a class, of any sort.
 */
public final class EncodedField extends EncodedMember implements Comparable<EncodedField> {

    /** {@code non-null;} constant for the field */
    private final CstFieldRef field;

    /**
     * Constructs an instance.
     * 
     * @param field {@code non-null;} constant for the field
     * @param accessFlags access flags
     */
    public EncodedField(CstFieldRef field, int accessFlags) {
        super(accessFlags);
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        this.field = field;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return field.hashCode();
    }

    /** {@inheritDoc} */
    public boolean equals(Object other) {
        if (!(other instanceof EncodedField)) {
            return false;
        }
        return compareTo((EncodedField) other) == 0;
    }

    /**
     * {@inheritDoc}
     * 
     * <p><b>Note:</b> This compares the method constants only,
     * ignoring any associated code, because it should never be the
     * case that two different items with the same method constant
     * ever appear in the same list (or same file, even).</p>
     */
    public int compareTo(EncodedField other) {
        return field.compareTo(other.field);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(100);
        sb.append(getClass().getName());
        sb.append('{');
        sb.append(Hex.u2(getAccessFlags()));
        sb.append(' ');
        sb.append(field);
        sb.append('}');
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void addContents(DexFile file) {
        FieldIdsSection fieldIds = file.getFieldIds();
        fieldIds.intern(field);
    }

    /** {@inheritDoc} */
    @Override
    public CstUtf8 getName() {
        return field.getNat().getName();
    }

    /** {@inheritDoc} */
    public String toHuman() {
        return field.toHuman();
    }

    /** {@inheritDoc} */
    @Override
    public void debugPrint(PrintWriter out, boolean verbose) {
        out.println(toString());
    }

    /**
     * Gets the constant for the field.
     * 
     * @return {@code non-null;} the constant
     */
    public CstFieldRef getRef() {
        return field;
    }

    /** {@inheritDoc} */
    @Override
    public int encode(DexFile file, AnnotatedOutput out, int lastIndex, int dumpSeq) {
        int fieldIdx = file.getFieldIds().indexOf(field);
        int diff = fieldIdx - lastIndex;
        int accessFlags = getAccessFlags();
        if (out.annotates()) {
            out.annotate(0, String.format("  [%x] %s", dumpSeq, field.toHuman()));
            out.annotate(Leb128Utils.unsignedLeb128Size(diff), "    field_idx:    " + Hex.u4(fieldIdx));
            out.annotate(Leb128Utils.unsignedLeb128Size(accessFlags), "    access_flags: " + AccessFlags.fieldString(accessFlags));
        }
        out.writeUnsignedLeb128(diff);
        out.writeUnsignedLeb128(accessFlags);
        return fieldIdx;
    }
}
