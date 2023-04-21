    protected void copy(org.omg.CORBA.portable.InputStream src, org.omg.CORBA.portable.OutputStream dst) {
        switch(_kind) {
            case TCKind._tk_null:
            case TCKind._tk_void:
            case TCKind._tk_native:
            case TCKind._tk_abstract_interface:
                break;
            case TCKind._tk_short:
            case TCKind._tk_ushort:
                dst.write_short(src.read_short());
                break;
            case TCKind._tk_long:
            case TCKind._tk_ulong:
                dst.write_long(src.read_long());
                break;
            case TCKind._tk_float:
                dst.write_float(src.read_float());
                break;
            case TCKind._tk_double:
                dst.write_double(src.read_double());
                break;
            case TCKind._tk_longlong:
            case TCKind._tk_ulonglong:
                dst.write_longlong(src.read_longlong());
                break;
            case TCKind._tk_longdouble:
                throw wrapper.tkLongDoubleNotSupported();
            case TCKind._tk_boolean:
                dst.write_boolean(src.read_boolean());
                break;
            case TCKind._tk_char:
                dst.write_char(src.read_char());
                break;
            case TCKind._tk_wchar:
                dst.write_wchar(src.read_wchar());
                break;
            case TCKind._tk_octet:
                dst.write_octet(src.read_octet());
                break;
            case TCKind._tk_string:
                {
                    String s;
                    s = src.read_string();
                    if ((_length != 0) && (s.length() > _length)) throw wrapper.badStringBounds(new Integer(s.length()), new Integer(_length));
                    dst.write_string(s);
                }
                break;
            case TCKind._tk_wstring:
                {
                    String s;
                    s = src.read_wstring();
                    if ((_length != 0) && (s.length() > _length)) throw wrapper.badStringBounds(new Integer(s.length()), new Integer(_length));
                    dst.write_wstring(s);
                }
                break;
            case TCKind._tk_fixed:
                {
                    dst.write_ushort(src.read_ushort());
                    dst.write_short(src.read_short());
                }
                break;
            case TCKind._tk_any:
                {
                    Any tmp = ((CDRInputStream) src).orb().create_any();
                    TypeCodeImpl t = new TypeCodeImpl((ORB) dst.orb());
                    t.read_value((org.omg.CORBA_2_3.portable.InputStream) src);
                    t.write_value((org.omg.CORBA_2_3.portable.OutputStream) dst);
                    tmp.read_value(src, t);
                    tmp.write_value(dst);
                    break;
                }
            case TCKind._tk_TypeCode:
                {
                    dst.write_TypeCode(src.read_TypeCode());
                    break;
                }
            case TCKind._tk_Principal:
                {
                    dst.write_Principal(src.read_Principal());
                    break;
                }
            case TCKind._tk_objref:
                {
                    dst.write_Object(src.read_Object());
                    break;
                }
            case TCKind._tk_except:
                dst.write_string(src.read_string());
            case TCKind._tk_value:
            case TCKind._tk_struct:
                {
                    for (int i = 0; i < _memberTypes.length; i++) {
                        _memberTypes[i].copy(src, dst);
                    }
                    break;
                }
            case TCKind._tk_union:
                {
                    Any tagValue = new AnyImpl((ORB) src.orb());
                    switch(realType(_discriminator).kind().value()) {
                        case TCKind._tk_short:
                            {
                                short value = src.read_short();
                                tagValue.insert_short(value);
                                dst.write_short(value);
                                break;
                            }
                        case TCKind._tk_long:
                            {
                                int value = src.read_long();
                                tagValue.insert_long(value);
                                dst.write_long(value);
                                break;
                            }
                        case TCKind._tk_ushort:
                            {
                                short value = src.read_short();
                                tagValue.insert_ushort(value);
                                dst.write_short(value);
                                break;
                            }
                        case TCKind._tk_ulong:
                            {
                                int value = src.read_long();
                                tagValue.insert_ulong(value);
                                dst.write_long(value);
                                break;
                            }
                        case TCKind._tk_float:
                            {
                                float value = src.read_float();
                                tagValue.insert_float(value);
                                dst.write_float(value);
                                break;
                            }
                        case TCKind._tk_double:
                            {
                                double value = src.read_double();
                                tagValue.insert_double(value);
                                dst.write_double(value);
                                break;
                            }
                        case TCKind._tk_boolean:
                            {
                                boolean value = src.read_boolean();
                                tagValue.insert_boolean(value);
                                dst.write_boolean(value);
                                break;
                            }
                        case TCKind._tk_char:
                            {
                                char value = src.read_char();
                                tagValue.insert_char(value);
                                dst.write_char(value);
                                break;
                            }
                        case TCKind._tk_enum:
                            {
                                int value = src.read_long();
                                tagValue.type(_discriminator);
                                tagValue.insert_long(value);
                                dst.write_long(value);
                                break;
                            }
                        case TCKind._tk_longlong:
                            {
                                long value = src.read_longlong();
                                tagValue.insert_longlong(value);
                                dst.write_longlong(value);
                                break;
                            }
                        case TCKind._tk_ulonglong:
                            {
                                long value = src.read_longlong();
                                tagValue.insert_ulonglong(value);
                                dst.write_longlong(value);
                                break;
                            }
                        case TCKind._tk_wchar:
                            {
                                char value = src.read_wchar();
                                tagValue.insert_wchar(value);
                                dst.write_wchar(value);
                                break;
                            }
                        default:
                            throw wrapper.illegalUnionDiscriminatorType();
                    }
                    int labelIndex;
                    for (labelIndex = 0; labelIndex < _unionLabels.length; labelIndex++) {
                        if (tagValue.equal(_unionLabels[labelIndex])) {
                            _memberTypes[labelIndex].copy(src, dst);
                            break;
                        }
                    }
                    if (labelIndex == _unionLabels.length) {
                        if (_defaultIndex == -1) throw wrapper.unexpectedUnionDefault(); else _memberTypes[_defaultIndex].copy(src, dst);
                    }
                    break;
                }
            case TCKind._tk_enum:
                dst.write_long(src.read_long());
                break;
            case TCKind._tk_sequence:
                int seqLength = src.read_long();
                if ((_length != 0) && (seqLength > _length)) throw wrapper.badSequenceBounds(new Integer(seqLength), new Integer(_length));
                dst.write_long(seqLength);
                lazy_content_type();
                for (int i = 0; i < seqLength; i++) _contentType.copy(src, dst);
                break;
            case TCKind._tk_array:
                for (int i = 0; i < _length; i++) _contentType.copy(src, dst);
                break;
            case TCKind._tk_alias:
            case TCKind._tk_value_box:
                _contentType.copy(src, dst);
                break;
            case tk_indirect:
                indirectType().copy(src, dst);
                break;
            default:
                throw wrapper.invalidTypecodeKindMarshal();
        }
    }
