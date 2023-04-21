    public org.omg.CORBA.Object string_to_object(String str) {
        int sch_idx = str.indexOf(':');
        if (sch_idx < 0) {
            throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 7, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
        String scheme = str.substring(0, sch_idx).toLowerCase();
        if (scheme.equals("ior")) {
            int len = str.length();
            if ((len % 2) != 0) {
                throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 9, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
            }
            if (len < 28) {
                throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 9, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
            }
            byte[] buf = new byte[(len - 4) / 2];
            int j = 0;
            char c;
            for (int i = 4; i < len; i += 2) {
                c = str.charAt(i);
                if (c >= '0' && c <= '9') {
                    buf[j] = (byte) ((c - '0') << 4);
                } else if (c >= 'a' && c <= 'f') {
                    buf[j] = (byte) ((c - 'a' + 0xA) << 4);
                } else if (c >= 'A' && c <= 'F') {
                    buf[j] = (byte) ((c - 'A' + 0xA) << 4);
                } else {
                    throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 9, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
                }
                c = str.charAt(i + 1);
                if (c >= '0' && c <= '9') {
                    buf[j] += (byte) (c - '0');
                } else if (c >= 'a' && c <= 'f') {
                    buf[j] += (byte) (c - 'a' + 0xA);
                } else if (c >= 'A' && c <= 'F') {
                    buf[j] += (byte) (c - 'A' + 0xA);
                } else {
                    throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 9, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
                }
                ++j;
            }
            try {
                org.omg.IOP.CodecFactory factory = (org.omg.IOP.CodecFactory) resolve_initial_references("CodecFactory");
                org.omg.IOP.Codec codec = factory.create_codec(new org.omg.IOP.Encoding(org.omg.IOP.ENCODING_CDR_ENCAPS.value, (byte) 1, (byte) 2));
                org.omg.CORBA.Any any = codec.decode_value(buf, get_primitive_tc(org.omg.CORBA.TCKind.tk_objref));
                return any.extract_Object();
            } catch (final org.omg.CORBA.ORBPackage.InvalidName ex) {
                getLogger().error("Unable to resolve CodecFactory.", ex);
            } catch (final org.omg.IOP.CodecFactoryPackage.UnknownEncoding ex) {
                getLogger().error("An encoding could not be created.", ex);
            } catch (final org.omg.IOP.CodecPackage.FormatMismatch ex) {
                getLogger().error("Encoding format does not match.", ex);
            } catch (final org.omg.IOP.CodecPackage.TypeMismatch ex) {
                getLogger().error("Encoding types do not match.", ex);
            }
            throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 9, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        } else if (scheme.equals("corbaloc")) {
            return scan_url_loc(str.substring(9));
        } else if (scheme.equals("corbaname")) {
            String loc = str.substring(10);
            return scan_url_name(loc);
        } else if (scheme.equals("file") || scheme.equals("ftp") || scheme.equals("http")) {
            try {
                java.net.URL url = new java.net.URL(str);
                java.io.InputStream is = url.openStream();
                java.io.InputStreamReader rd = new java.io.InputStreamReader(is);
                java.io.BufferedReader inpt = new java.io.BufferedReader(rd);
                String string_ref = inpt.readLine();
                inpt.close();
                return string_to_object(string_ref);
            } catch (final java.net.MalformedURLException ex) {
                final String msg = "Invalid URL: " + str + ".";
                getLogger().error(msg, ex);
                throw ExceptionTool.initCause(new org.omg.CORBA.BAD_PARAM(msg, org.omg.CORBA.OMGVMCID.value | 9, org.omg.CORBA.CompletionStatus.COMPLETED_NO), ex);
            } catch (final java.io.IOException ex) {
                final String msg = "Unexpected IOException.";
                getLogger().error(msg, ex);
                throw ExceptionTool.initCause(new org.omg.CORBA.BAD_PARAM(msg, org.omg.CORBA.OMGVMCID.value | 10, org.omg.CORBA.CompletionStatus.COMPLETED_NO), ex);
            }
        } else {
            throw new org.omg.CORBA.BAD_PARAM(org.omg.CORBA.OMGVMCID.value | 7, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
    }
