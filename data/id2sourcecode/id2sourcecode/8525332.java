    private static void handleArrayContent(InputStream is, OutputStream os, TypeCode content, int length) throws BadKind, Bounds {
        switch(content.kind().value()) {
            case TCKind._tk_null:
            case TCKind._tk_void:
                return;
            case TCKind._tk_short:
                {
                    short[] tmp = new short[length];
                    is.read_short_array(tmp, 0, length);
                    os.write_short_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_ushort:
                {
                    short[] tmp = new short[length];
                    is.read_ushort_array(tmp, 0, length);
                    os.write_ushort_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_long:
                {
                    int[] tmp = new int[length];
                    is.read_long_array(tmp, 0, length);
                    os.write_long_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_ulong:
                {
                    int[] tmp = new int[length];
                    is.read_ulong_array(tmp, 0, length);
                    os.write_ulong_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_float:
                {
                    float[] tmp = new float[length];
                    is.read_float_array(tmp, 0, length);
                    os.write_float_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_double:
                {
                    double[] tmp = new double[length];
                    is.read_double_array(tmp, 0, length);
                    os.write_double_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_boolean:
                {
                    boolean[] tmp = new boolean[length];
                    is.read_boolean_array(tmp, 0, length);
                    os.write_boolean_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_char:
                {
                    char[] tmp = new char[length];
                    is.read_char_array(tmp, 0, length);
                    os.write_char_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_octet:
                {
                    byte[] tmp = new byte[length];
                    is.read_octet_array(tmp, 0, length);
                    os.write_octet_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_any:
                for (int i = 0; i < length; ++i) {
                    os.write_any(is.read_any());
                }
                return;
            case TCKind._tk_TypeCode:
                for (int i = 0; i < length; ++i) {
                    os.write_TypeCode(is.read_TypeCode());
                }
                return;
            case TCKind._tk_Principal:
                for (int i = 0; i < length; ++i) {
                    os.write_Principal(is.read_Principal());
                }
                return;
            case TCKind._tk_objref:
                for (int i = 0; i < length; ++i) {
                    os.write_Object(is.read_Object());
                }
                return;
            case TCKind._tk_enum:
                for (int i = 0; i < length; ++i) {
                    os.write_ulong(is.read_ulong());
                }
                return;
            case TCKind._tk_string:
                for (int i = 0; i < length; ++i) {
                    os.write_string(is.read_string());
                }
                return;
            case TCKind._tk_longlong:
                {
                    long[] tmp = new long[length];
                    is.read_longlong_array(tmp, 0, length);
                    os.write_longlong_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_ulonglong:
                {
                    long[] tmp = new long[length];
                    is.read_ulonglong_array(tmp, 0, length);
                    os.write_ulonglong_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_longdouble:
                throw new NO_IMPLEMENT();
            case TCKind._tk_wchar:
                {
                    char[] tmp = new char[length];
                    is.read_wchar_array(tmp, 0, length);
                    os.write_wchar_array(tmp, 0, length);
                }
                return;
            case TCKind._tk_wstring:
                for (int i = 0; i < length; ++i) {
                    os.write_wstring(is.read_wstring());
                }
                return;
            case TCKind._tk_fixed:
                for (int i = 0; i < length; ++i) {
                    os.write_fixed(is.read_fixed());
                }
                return;
            case TCKind._tk_value:
            case TCKind._tk_value_box:
                for (int i = 0; i < length; ++i) {
                    os.write_value(is.read_value());
                }
                return;
            case TCKind._tk_abstract_interface:
                for (int i = 0; i < length; ++i) {
                    os.write_abstract_interface(is.read_abstract_interface());
                }
                return;
            case TCKind._tk_struct:
            case TCKind._tk_except:
                for (int i = 0; i < length; ++i) {
                    for (int j = 0; j < content.member_count(); ++j) {
                        copy_stream(content.member_type(j), is, os);
                    }
                }
                return;
            case TCKind._tk_union:
            case TCKind._tk_sequence:
            case TCKind._tk_array:
                for (int i = 0; i < length; ++i) {
                    copy_stream(content, is, os);
                }
                return;
            case TCKind._tk_alias:
            case TCKind._tk_native:
            default:
                org.openorb.orb.util.Trace.signalIllegalCondition(null, "Unexpected array content type kind().value()==" + content.kind().value() + ".");
        }
    }
