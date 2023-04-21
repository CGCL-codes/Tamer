package tudresden.ocl20.core.lib;

import java.util.*;

/** This class represents the part of the OCL type OclAny common to basic
 *  types and application types, i.e. the availablity of certain properties.
 */
public abstract class OclAny implements OclRoot {

    public OclAny() {
    }

    public abstract OclBoolean isEqualTo(Object o);

    /** @return the negated result of isEqualTo
     */
    public OclBoolean isNotEqualTo(Object o) {
        return isEqualTo(o).not();
    }

    /** This method returns true if the OclType given as parameter is a type
     *  of the object whose method is called or a supertype of such a type.
     */
    public OclBoolean oclIsKindOf(OclType t) {
        if (isUndefined()) {
            return new OclBoolean(0, getUndefinedReason());
        }
        return OclBoolean.getOclRepresentationFor(t.isOfKind(this));
    }

    /** This method returns true if the OclType given as parameter is a "type"
     *  of this object. "Type" is to be understood in the UML/OCL sense of the
     *  word, meaning that an OclAny has exactly one type and
     *  this types supertypes are not types of the object.
     */
    public OclBoolean oclIsTypeOf(OclType t) {
        if (isUndefined()) {
            return new OclBoolean(0, getUndefinedReason());
        }
        return OclBoolean.getOclRepresentationFor(t.isOfType(this));
    }

    public OclAny oclAsType(OclType t) {
        return this;
    }

    /**
     The reason, why this object represents an undefined value.
     Additionally, this is the tag, whether this object represents
     a undefined value.
     Is null, if and only if it is not undefined.
     */
    private String undefinedreason = null;

    /**
     Constructs an instance representing an undefined value.
     @parameter dummy must be 0.
     */
    protected OclAny(int dummy, String undefinedreason) {
        if (dummy != 0) throw new RuntimeException();
        this.undefinedreason = undefinedreason;
    }

    public final boolean isUndefined() {
        return undefinedreason != null;
    }

    public final String getUndefinedReason() {
        if (undefinedreason != null) return undefinedreason; else throw new RuntimeException();
    }
}
