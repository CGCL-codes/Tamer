    public void read_wchar_array(char[] val, int voff, int vlen) {
        if (0 == vlen) {
            return;
        }
        if (m_wchar_enc == null) {
            if (m_version.minor == 0) {
                cancel(new org.omg.CORBA.BAD_OPERATION("Wchar not supported in IIOP 1.0", IIOPMinorCodes.BAD_OPERATION_IIOP_VERSION, CompletionStatus.COMPLETED_MAYBE));
            } else {
                cancel(new org.omg.CORBA.MARSHAL("Missing wchar encoder.", IIOPMinorCodes.MARSHAL_WCHAR, CompletionStatus.COMPLETED_MAYBE));
            }
            return;
        }
        byte[] buf;
        int off;
        int len;
        switch(m_version.minor) {
            case 0:
                cancel(new org.omg.CORBA.MARSHAL("Wchar not supported in IIOP 1.0", IIOPMinorCodes.BAD_OPERATION_IIOP_VERSION, CompletionStatus.COMPLETED_MAYBE));
                break;
            case 1:
                alignment(2);
                m_tmp_len.value = 2 * vlen;
                len = m_tmp_len.value;
                next(m_tmp_buf, m_tmp_off, m_tmp_len);
                postread(len - m_tmp_len.value);
                if (m_tmp_len.value != 0) {
                    buf = new byte[len];
                    off = 0;
                    System.arraycopy(m_tmp_buf.value, m_tmp_off.value, buf, 0, len - m_tmp_len.value);
                    read_octet_array(buf, len - m_tmp_len.value, m_tmp_len.value);
                    if (m_wchar_reverse) {
                        byte tmp;
                        for (int i = 0; i < len; i += 2) {
                            tmp = buf[i];
                            buf[i] = buf[i + 1];
                            buf[i + 1] = tmp;
                        }
                    }
                } else if (m_wchar_reverse) {
                    buf = new byte[len];
                    off = 0;
                    for (int i = 0; i < len; i += 2) {
                        buf[0] = m_tmp_buf.value[m_tmp_off.value + i + 1];
                        buf[1] = m_tmp_buf.value[m_tmp_off.value + i];
                    }
                } else {
                    buf = m_tmp_buf.value;
                    off = m_tmp_off.value;
                }
                try {
                    String s = new String(buf, off, len, m_wchar_enc);
                    if (s.length() != vlen) {
                        cancel(new org.omg.CORBA.MARSHAL("Unable to decode char value", IIOPMinorCodes.MARSHAL_CHAR, CompletionStatus.COMPLETED_MAYBE));
                    }
                    s.getChars(0, vlen, val, voff);
                } catch (final UnsupportedEncodingException ex) {
                    getLogger().error("Unsupported encoding should be impossible.", ex);
                }
                return;
            case 2:
                alignment(1);
                int post = 0;
                for (int c = 0; c < vlen; ++c) {
                    m_tmp_len.value = 1;
                    next(m_tmp_buf, m_tmp_off, m_tmp_len);
                    len = m_tmp_buf.value[m_tmp_off.value];
                    m_tmp_len.value = m_tmp_buf.value[m_tmp_off.value];
                    next(m_tmp_buf, m_tmp_off, m_tmp_len);
                    if (m_tmp_len.value != 0) {
                        postread(post + len + 1 - m_tmp_len.value);
                        post = 0;
                        buf = new byte[len];
                        off = 0;
                        System.arraycopy(m_tmp_buf.value, m_tmp_off.value, buf, 0, len - m_tmp_len.value);
                        read_octet_array(buf, len - m_tmp_len.value, m_tmp_len.value);
                        if (m_wchar_reverse) {
                            byte tmp;
                            for (int i = 0; i < m_wchar_align / 2; ++i) {
                                tmp = buf[off + i];
                                buf[off + i] = buf[off + m_wchar_align - i];
                                buf[off + m_wchar_align - i] = tmp;
                            }
                        }
                    } else if (m_wchar_reverse) {
                        buf = new byte[len];
                        off = 0;
                        for (int i = 0; i < m_wchar_align; ++i) {
                            buf[i] = m_tmp_buf.value[m_tmp_off.value + m_wchar_align - 1];
                        }
                    } else {
                        post += len + 1;
                        buf = m_tmp_buf.value;
                        off = m_tmp_off.value;
                    }
                    try {
                        String s = new String(buf, off, len, m_wchar_enc);
                        if (s.length() != 1) {
                            cancel(new org.omg.CORBA.MARSHAL("Unable to decode wchar value", IIOPMinorCodes.MARSHAL_WCHAR, CompletionStatus.COMPLETED_MAYBE));
                        }
                        val[voff + c] = s.charAt(0);
                    } catch (final UnsupportedEncodingException ex) {
                        getLogger().error("Unsupported encoding should be impossible.", ex);
                    }
                }
                if (post > 0) {
                    postread(post);
                }
                break;
        }
    }
