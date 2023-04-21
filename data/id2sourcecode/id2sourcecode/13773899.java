    @Override
    public Object[] getSpectrumValue(final String readString, final String writeString, final int dataType) {
        int size = 0;
        StringTokenizer readTokenizer;
        if (readString == null || "".equals(readString) || "null".equals(readString)) {
            readTokenizer = null;
        } else {
            readTokenizer = new StringTokenizer(readString, GlobalConst.CLOB_SEPARATOR);
            size += readTokenizer.countTokens();
        }
        StringTokenizer writeTokenizer;
        if (writeString == null || "".equals(writeString) || "null".equals(writeString)) {
            writeTokenizer = null;
        } else {
            writeTokenizer = new StringTokenizer(writeString, GlobalConst.CLOB_SEPARATOR);
            size += writeTokenizer.countTokens();
        }
        Double[] dvalueArr = null;
        Integer[] lvalueArr = null;
        Long[] lvalueArr2 = null;
        Short[] svalueArr = null;
        Boolean[] bvalueArr = null;
        Float[] fvalueArr = null;
        String[] stvalueArr = null;
        switch(dataType) {
            case TangoConst.Tango_DEV_BOOLEAN:
                bvalueArr = new Boolean[size];
                break;
            case TangoConst.Tango_DEV_STATE:
            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_ULONG:
                lvalueArr = new Integer[size];
                break;
            case TangoConst.Tango_DEV_LONG64:
            case TangoConst.Tango_DEV_ULONG64:
                lvalueArr2 = new Long[size];
                break;
            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_UCHAR:
                svalueArr = new Short[size];
                break;
            case TangoConst.Tango_DEV_FLOAT:
                fvalueArr = new Float[size];
                break;
            case TangoConst.Tango_DEV_STRING:
                stvalueArr = new String[size];
                break;
            case TangoConst.Tango_DEV_DOUBLE:
            default:
                dvalueArr = new Double[size];
        }
        int i = 0;
        if (readTokenizer != null) {
            while (readTokenizer.hasMoreTokens()) {
                final String currentValRead = readTokenizer.nextToken();
                if (currentValRead == null || currentValRead.trim().equals("")) {
                    break;
                }
                switch(dataType) {
                    case TangoConst.Tango_DEV_BOOLEAN:
                        try {
                            if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                                bvalueArr[i] = null;
                            } else {
                                bvalueArr[i] = new Boolean(Double.valueOf(currentValRead).intValue() != 0);
                            }
                        } catch (final NumberFormatException n) {
                            bvalueArr[i] = new Boolean("true".equalsIgnoreCase(currentValRead.trim()));
                        }
                        break;
                    case TangoConst.Tango_DEV_STATE:
                    case TangoConst.Tango_DEV_LONG:
                    case TangoConst.Tango_DEV_ULONG:
                        try {
                            if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                                lvalueArr[i] = null;
                            } else {
                                lvalueArr[i] = Integer.valueOf(currentValRead);
                            }
                        } catch (final NumberFormatException n) {
                            lvalueArr[i] = new Integer(Double.valueOf(currentValRead).intValue());
                        }
                        break;
                    case TangoConst.Tango_DEV_LONG64:
                    case TangoConst.Tango_DEV_ULONG64:
                        try {
                            if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                                lvalueArr2[i] = null;
                            } else {
                                lvalueArr2[i] = Long.valueOf(currentValRead);
                            }
                        } catch (final NumberFormatException n) {
                            lvalueArr2[i] = new Long(Double.valueOf(currentValRead).longValue());
                        }
                        break;
                    case TangoConst.Tango_DEV_SHORT:
                    case TangoConst.Tango_DEV_USHORT:
                    case TangoConst.Tango_DEV_UCHAR:
                        try {
                            if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                                svalueArr[i] = null;
                            } else {
                                svalueArr[i] = Short.valueOf(currentValRead);
                            }
                        } catch (final NumberFormatException n) {
                            svalueArr[i] = new Short(Double.valueOf(currentValRead).shortValue());
                        }
                        break;
                    case TangoConst.Tango_DEV_FLOAT:
                        if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                            fvalueArr[i] = null;
                        } else {
                            fvalueArr[i] = Float.valueOf(currentValRead);
                        }
                        break;
                    case TangoConst.Tango_DEV_STRING:
                        if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                            stvalueArr[i] = null;
                        } else {
                            stvalueArr[i] = StringFormater.formatStringToRead(new String(currentValRead));
                        }
                        break;
                    case TangoConst.Tango_DEV_DOUBLE:
                    default:
                        if (currentValRead == null || "".equals(currentValRead) || "null".equals(currentValRead) || "NaN".equalsIgnoreCase(currentValRead)) {
                            dvalueArr[i] = null;
                        } else {
                            dvalueArr[i] = Double.valueOf(currentValRead);
                        }
                }
                i++;
            }
        }
        if (writeTokenizer != null) {
            while (writeTokenizer.hasMoreTokens()) {
                final String currentValWrite = writeTokenizer.nextToken();
                if (currentValWrite == null || currentValWrite.trim().equals("")) {
                    break;
                }
                switch(dataType) {
                    case TangoConst.Tango_DEV_BOOLEAN:
                        try {
                            if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                                bvalueArr[i] = null;
                            } else {
                                bvalueArr[i] = new Boolean(Double.valueOf(currentValWrite).intValue() != 0);
                            }
                        } catch (final NumberFormatException n) {
                            bvalueArr[i] = new Boolean("true".equalsIgnoreCase(currentValWrite.trim()));
                        }
                        break;
                    case TangoConst.Tango_DEV_STATE:
                    case TangoConst.Tango_DEV_LONG:
                    case TangoConst.Tango_DEV_ULONG:
                        try {
                            if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                                lvalueArr[i] = null;
                            } else {
                                lvalueArr[i] = Integer.valueOf(currentValWrite);
                            }
                        } catch (final NumberFormatException n) {
                            lvalueArr[i] = new Integer(Double.valueOf(currentValWrite).intValue());
                        }
                        break;
                    case TangoConst.Tango_DEV_LONG64:
                    case TangoConst.Tango_DEV_ULONG64:
                        try {
                            if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                                lvalueArr2[i] = null;
                            } else {
                                lvalueArr2[i] = Long.valueOf(currentValWrite);
                            }
                        } catch (final NumberFormatException n) {
                            lvalueArr2[i] = new Long(Double.valueOf(currentValWrite).intValue());
                        }
                        break;
                    case TangoConst.Tango_DEV_UCHAR:
                    case TangoConst.Tango_DEV_SHORT:
                    case TangoConst.Tango_DEV_USHORT:
                        try {
                            if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                                svalueArr[i] = null;
                            } else {
                                svalueArr[i] = Short.valueOf(currentValWrite);
                            }
                        } catch (final NumberFormatException n) {
                            svalueArr[i] = new Short(Double.valueOf(currentValWrite).shortValue());
                        }
                        break;
                    case TangoConst.Tango_DEV_FLOAT:
                        if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                            fvalueArr[i] = null;
                        } else {
                            fvalueArr[i] = Float.valueOf(currentValWrite);
                        }
                        break;
                    case TangoConst.Tango_DEV_STRING:
                        if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                            stvalueArr[i] = null;
                        } else {
                            stvalueArr[i] = StringFormater.formatStringToRead(new String(currentValWrite));
                        }
                        break;
                    case TangoConst.Tango_DEV_DOUBLE:
                    default:
                        if (currentValWrite == null || "".equals(currentValWrite) || "null".equals(currentValWrite) || "NaN".equalsIgnoreCase(currentValWrite)) {
                            dvalueArr[i] = null;
                        } else {
                            dvalueArr[i] = Double.valueOf(currentValWrite);
                        }
                }
                i++;
            }
        }
        if (readTokenizer == null && writeTokenizer == null) {
            return null;
        }
        switch(dataType) {
            case TangoConst.Tango_DEV_BOOLEAN:
                return bvalueArr;
            case TangoConst.Tango_DEV_STATE:
            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_ULONG:
                return lvalueArr;
            case TangoConst.Tango_DEV_LONG64:
            case TangoConst.Tango_DEV_ULONG64:
                return lvalueArr2;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_USHORT:
                return svalueArr;
            case TangoConst.Tango_DEV_FLOAT:
                return fvalueArr;
            case TangoConst.Tango_DEV_STRING:
                return stvalueArr;
            case TangoConst.Tango_DEV_DOUBLE:
            default:
                return dvalueArr;
        }
    }
