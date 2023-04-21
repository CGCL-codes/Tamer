package org.omg.TerminologyServices;

/** 
 * Helper class for : CodingSchemeVersion
 *  
 * @author OpenORB Compiler
 */
public class CodingSchemeVersionHelper {

    /**
     * Insert CodingSchemeVersion into an any
     * @param a an any
     * @param t CodingSchemeVersion value
     */
    public static void insert(org.omg.CORBA.Any a, org.omg.TerminologyServices.CodingSchemeVersion t) {
        a.insert_Object(t, type());
    }

    /**
     * Extract CodingSchemeVersion from an any
     *
     * @param a an any
     * @return the extracted CodingSchemeVersion value
     */
    public static org.omg.TerminologyServices.CodingSchemeVersion extract(org.omg.CORBA.Any a) {
        if (!a.type().equivalent(type())) {
            throw new org.omg.CORBA.MARSHAL();
        }
        try {
            return org.omg.TerminologyServices.CodingSchemeVersionHelper.narrow(a.extract_Object());
        } catch (final org.omg.CORBA.BAD_PARAM e) {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the CodingSchemeVersion TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type() {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc(id(), "CodingSchemeVersion");
        }
        return _tc;
    }

    /**
     * Return the CodingSchemeVersion IDL ID
     * @return an ID
     */
    public static String id() {
        return _id;
    }

    private static final String _id = "IDL:omg.org/TerminologyServices/CodingSchemeVersion:1.0";

    /**
     * Read CodingSchemeVersion from a marshalled stream
     * @param istream the input stream
     * @return the readed CodingSchemeVersion value
     */
    public static org.omg.TerminologyServices.CodingSchemeVersion read(org.omg.CORBA.portable.InputStream istream) {
        return (org.omg.TerminologyServices.CodingSchemeVersion) istream.read_Object(org.omg.TerminologyServices._CodingSchemeVersionStub.class);
    }

    /**
     * Write CodingSchemeVersion into a marshalled stream
     * @param ostream the output stream
     * @param value CodingSchemeVersion value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.TerminologyServices.CodingSchemeVersion value) {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl) value);
    }

    /**
     * Narrow CORBA::Object to CodingSchemeVersion
     * @param obj the CORBA Object
     * @return CodingSchemeVersion Object
     */
    public static CodingSchemeVersion narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof CodingSchemeVersion) return (CodingSchemeVersion) obj;
        if (obj._is_a(id())) {
            _CodingSchemeVersionStub stub = new _CodingSchemeVersionStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        }
        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to CodingSchemeVersion
     * @param obj the CORBA Object
     * @return CodingSchemeVersion Object
     */
    public static CodingSchemeVersion unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null) return null;
        if (obj instanceof CodingSchemeVersion) return (CodingSchemeVersion) obj;
        _CodingSchemeVersionStub stub = new _CodingSchemeVersionStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
        return stub;
    }
}
