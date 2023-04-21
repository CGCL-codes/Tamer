    public static void copy_stream(TypeCode type, InputStream is, OutputStream os) {
        try {
            type = TypeCodeBase._base_type(type);
            switch(type.kind().value()) {
                case TCKind._tk_null:
                case TCKind._tk_void:
                    return;
                case TCKind._tk_short:
                    os.write_short(is.read_short());
                    return;
                case TCKind._tk_long:
                    os.write_long(is.read_long());
                    return;
                case TCKind._tk_ushort:
                    os.write_ushort(is.read_ushort());
                    return;
                case TCKind._tk_ulong:
                    os.write_ulong(is.read_ulong());
                    return;
                case TCKind._tk_float:
                    os.write_float(is.read_float());
                    return;
                case TCKind._tk_double:
                    os.write_double(is.read_double());
                    return;
                case TCKind._tk_boolean:
                    os.write_boolean(is.read_boolean());
                    return;
                case TCKind._tk_char:
                    os.write_char(is.read_char());
                    return;
                case TCKind._tk_octet:
                    os.write_octet(is.read_octet());
                    return;
                case TCKind._tk_any:
                    os.write_any(is.read_any());
                    return;
                case TCKind._tk_TypeCode:
                    os.write_TypeCode(is.read_TypeCode());
                    return;
                case TCKind._tk_Principal:
                    os.write_Principal(is.read_Principal());
                    return;
                case TCKind._tk_objref:
                    os.write_Object(is.read_Object());
                    return;
                case TCKind._tk_except:
                    os.write_string(is.read_string());
                case TCKind._tk_struct:
                    for (int i = 0; i < type.member_count(); ++i) {
                        copy_stream(type.member_type(i), is, os);
                    }
                    return;
                case TCKind._tk_enum:
                    os.write_ulong(is.read_ulong());
                    return;
                case TCKind._tk_string:
                    os.write_string(is.read_string());
                    return;
                case TCKind._tk_longlong:
                    os.write_longlong(is.read_longlong());
                    return;
                case TCKind._tk_ulonglong:
                    os.write_ulonglong(is.read_ulonglong());
                    return;
                case TCKind._tk_longdouble:
                    throw new NO_IMPLEMENT();
                case TCKind._tk_wchar:
                    os.write_wchar(is.read_wchar());
                    return;
                case TCKind._tk_wstring:
                    os.write_wstring(is.read_wstring());
                    return;
                case TCKind._tk_fixed:
                    {
                        java.math.BigDecimal val;
                        if (is instanceof ExtendedInputStream) {
                            val = ((ExtendedInputStream) is).read_fixed(type);
                        } else {
                            val = is.read_fixed();
                        }
                        if (os instanceof ExtendedOutputStream) {
                            ((ExtendedOutputStream) os).write_fixed(val, type);
                        } else {
                            os.write_fixed(val);
                        }
                    }
                    return;
                case TCKind._tk_value:
                    os.write_value(is.read_value(type.id()));
                    return;
                case TCKind._tk_value_box:
                    {
                        BoxedValueHelper boxhelp = null;
                        String repo_id = type.id();
                        try {
                            String boxname = RepoIDHelper.idToClass(repo_id, RepoIDHelper.TYPE_HELPER);
                            boxhelp = (BoxedValueHelper) Thread.currentThread().getContextClassLoader().loadClass(boxname).newInstance();
                        } catch (Exception ex) {
                            boxhelp = new TypeCodeValueBoxHelper(os.orb(), type);
                        }
                        os.write_value(is.read_value(boxhelp), boxhelp);
                        return;
                    }
                case TCKind._tk_abstract_interface:
                    os.write_abstract_interface(is.read_abstract_interface());
                    return;
                case TCKind._tk_sequence:
                case TCKind._tk_array:
                    {
                        int length = type.length();
                        if (type.kind() == TCKind.tk_sequence) {
                            int truelen = is.read_ulong();
                            if (length != 0 && truelen > length) {
                                throw new MARSHAL("Sequence length out of bounds", MinorCodes.MARSHAL_SEQ_BOUND, CompletionStatus.COMPLETED_MAYBE);
                            }
                            length = truelen;
                            os.write_ulong(length);
                        }
                        TypeCode content = TypeCodeBase._base_type(type.content_type());
                        handleArrayContent(is, os, content, length);
                        return;
                    }
                case TCKind._tk_union:
                    {
                        TypeCode dsct = TypeCodeBase._base_type(type.discriminator_type());
                        handleUnionContent(is, os, type, dsct);
                        return;
                    }
                case TCKind._tk_alias:
                case TCKind._tk_native:
                default:
                    org.openorb.orb.util.Trace.signalIllegalCondition(null, "Unexpected type kind().value()==" + type.kind().value() + ".");
            }
        } catch (BadKind ex) {
            throw new CascadingRuntimeException("Unexpected BadKind exception during value copy", ex);
        } catch (Bounds ex) {
            throw new CascadingRuntimeException("Unexpected Bounds exception during value copy", ex);
        }
    }
