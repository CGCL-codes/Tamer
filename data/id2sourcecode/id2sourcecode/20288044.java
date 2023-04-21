    public static void copy_value(org.omg.CORBA.portable.InputStream is, org.omg.CORBA.portable.OutputStream os, TypeCode type) {
        int tk = type.kind().value();
        try {
            switch(tk) {
                case TCKind._tk_null:
                case TCKind._tk_void:
                    break;
                case TCKind._tk_octet:
                    os.write_octet(is.read_octet());
                    break;
                case TCKind._tk_boolean:
                    os.write_boolean(is.read_boolean());
                    break;
                case TCKind._tk_short:
                case TCKind._tk_ushort:
                    os.write_short(is.read_short());
                    break;
                case TCKind._tk_long:
                case TCKind._tk_ulong:
                case TCKind._tk_enum:
                    os.write_long(is.read_long());
                    break;
                case TCKind._tk_longlong:
                case TCKind._tk_ulonglong:
                    os.write_longlong(is.read_longlong());
                    break;
                case TCKind._tk_fixed:
                    os.write_fixed(is.read_fixed());
                    break;
                case TCKind._tk_char:
                    os.write_char(is.read_char());
                    break;
                case TCKind._tk_wchar:
                    os.write_wchar(is.read_wchar());
                    break;
                case TCKind._tk_string:
                    os.write_string(is.read_string());
                    break;
                case TCKind._tk_wstring:
                    os.write_wstring(is.read_wstring());
                    break;
                case TCKind._tk_array:
                    {
                        TypeCode content_type = type.content_type();
                        int len = type.length();
                        for (int i = 0; i < len; ++i) {
                            copy_value(is, os, content_type);
                        }
                    }
                    break;
                case TCKind._tk_sequence:
                    {
                        TypeCode content_type = type.content_type();
                        int len = is.read_long();
                        os.write_long(len);
                        for (int i = 0; i < len; ++i) {
                            copy_value(is, os, content_type);
                        }
                    }
                    break;
                case TCKind._tk_except:
                    os.write_string(is.read_string());
                case TCKind._tk_struct:
                    {
                        int cnt = type.member_count();
                        for (int i = 0; i < cnt; ++i) {
                            TypeCode mbr_type = type.member_type(i);
                            copy_value(is, os, type.member_type(i));
                        }
                    }
                    break;
                case TCKind._tk_alias:
                    copy_value(is, os, type.content_type());
                    break;
                case TCKind._tk_union:
                    {
                        TypeCode dt = type.discriminator_type();
                        Any disc = org.omg.CORBA.ORB.init().create_any();
                        disc.read_value(is, dt);
                        int cnt = type.member_count();
                        int idx = type.default_index();
                        for (int i = 0; i < cnt; ++i) {
                            Any label = type.member_label(i);
                            if (i != idx && disc.equal(type.member_label(i))) {
                                idx = i;
                                break;
                            }
                        }
                        if (idx < 0) throw new org.omg.CORBA.INTERNAL("Unknown union member");
                        copy_value(is, os, type.member_type(idx));
                    }
                    break;
                case TCKind._tk_any:
                    os.write_any(is.read_any());
                    break;
                case TCKind._tk_TypeCode:
                    os.write_TypeCode(is.read_TypeCode());
                    break;
                case TCKind._tk_Principal:
                    os.write_Principal(is.read_Principal());
                    break;
                case TCKind._tk_objref:
                    os.write_Object(is.read_Object());
                    break;
                default:
                    throw new org.omg.CORBA.INTERNAL("Unsupported typecode for copy_value");
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
            throw new org.omg.CORBA.INTERNAL(ex.toString());
        } catch (org.omg.CORBA.TypeCodePackage.Bounds ex) {
            throw new org.omg.CORBA.INTERNAL(ex.toString());
        }
    }
