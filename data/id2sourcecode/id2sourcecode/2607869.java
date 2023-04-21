    public static Object simpleTypeConvert(Object obj, String type, String format, Locale locale, boolean noTypeFail) throws GeneralException {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().getName().equals(type)) {
            return obj;
        }
        if ("PlainString".equals(type)) {
            return obj.toString();
        }
        if ("Object".equals(type) || "java.lang.Object".equals(type)) {
            return obj;
        }
        String fromType = null;
        if ((type.equals("List") || type.equals("java.util.List")) && obj.getClass().isArray()) {
            List newObj = FastList.newInstance();
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                newObj.add(Array.get(obj, i));
            }
            return newObj;
        } else if (obj instanceof java.lang.String) {
            fromType = "String";
            String str = (String) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                return obj;
            }
            if (str.length() == 0) {
                return null;
            }
            if ("Boolean".equals(type) || "java.lang.Boolean".equals(type)) {
                str = StringUtil.removeSpaces(str);
                Boolean value = null;
                if (str.equalsIgnoreCase("TRUE")) {
                    value = Boolean.TRUE;
                } else {
                    value = Boolean.FALSE;
                }
                return value;
            } else if ("Locale".equals(type) || "java.util.Locale".equals(type)) {
                Locale loc = UtilMisc.parseLocale(str);
                if (loc != null) {
                    return loc;
                } else {
                    throw new GeneralException("Could not convert " + str + " to " + type + ": ");
                }
            } else if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type)) {
                str = StringUtil.removeSpaces(str);
                try {
                    NumberFormat nf = null;
                    if (locale == null) {
                        nf = NumberFormat.getNumberInstance();
                    } else {
                        nf = NumberFormat.getNumberInstance(locale);
                    }
                    Number tempNum = nf.parse(str);
                    return new BigDecimal(tempNum.toString());
                } catch (ParseException e) {
                    throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                }
            } else if ("Double".equals(type) || "java.lang.Double".equals(type)) {
                str = StringUtil.removeSpaces(str);
                try {
                    NumberFormat nf = null;
                    if (locale == null) {
                        nf = NumberFormat.getNumberInstance();
                    } else {
                        nf = NumberFormat.getNumberInstance(locale);
                    }
                    Number tempNum = nf.parse(str);
                    return new Double(tempNum.doubleValue());
                } catch (ParseException e) {
                    throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                }
            } else if ("Float".equals(type) || "java.lang.Float".equals(type)) {
                str = StringUtil.removeSpaces(str);
                try {
                    NumberFormat nf = null;
                    if (locale == null) {
                        nf = NumberFormat.getNumberInstance();
                    } else {
                        nf = NumberFormat.getNumberInstance(locale);
                    }
                    Number tempNum = nf.parse(str);
                    return new Float(tempNum.floatValue());
                } catch (ParseException e) {
                    throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                }
            } else if ("Long".equals(type) || "java.lang.Long".equals(type)) {
                str = StringUtil.removeSpaces(str);
                try {
                    NumberFormat nf = null;
                    if (locale == null) {
                        nf = NumberFormat.getNumberInstance();
                    } else {
                        nf = NumberFormat.getNumberInstance(locale);
                    }
                    nf.setMaximumFractionDigits(0);
                    Number tempNum = nf.parse(str);
                    return new Long(tempNum.longValue());
                } catch (ParseException e) {
                    throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                }
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                str = StringUtil.removeSpaces(str);
                try {
                    NumberFormat nf = null;
                    if (locale == null) {
                        nf = NumberFormat.getNumberInstance();
                    } else {
                        nf = NumberFormat.getNumberInstance(locale);
                    }
                    nf.setMaximumFractionDigits(0);
                    Number tempNum = nf.parse(str);
                    return new Integer(tempNum.intValue());
                } catch (ParseException e) {
                    throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                }
            } else if ("Date".equals(type) || "java.sql.Date".equals(type)) {
                if (format == null || format.length() == 0) {
                    try {
                        return java.sql.Date.valueOf(str);
                    } catch (Exception e) {
                        try {
                            DateFormat df = null;
                            if (locale != null) {
                                df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                            } else {
                                df = DateFormat.getDateInstance(DateFormat.SHORT);
                            }
                            Date fieldDate = df.parse(str);
                            return new java.sql.Date(fieldDate.getTime());
                        } catch (ParseException e1) {
                            throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                        }
                    }
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        java.util.Date fieldDate = sdf.parse(str);
                        return new java.sql.Date(fieldDate.getTime());
                    } catch (ParseException e) {
                        throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                    }
                }
            } else if ("Time".equals(type) || "java.sql.Time".equals(type)) {
                if (format == null || format.length() == 0) {
                    try {
                        return java.sql.Time.valueOf(str);
                    } catch (Exception e) {
                        try {
                            DateFormat df = null;
                            if (locale != null) {
                                df = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
                            } else {
                                df = DateFormat.getTimeInstance(DateFormat.SHORT);
                            }
                            Date fieldDate = df.parse(str);
                            return new java.sql.Time(fieldDate.getTime());
                        } catch (ParseException e1) {
                            throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                        }
                    }
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        java.util.Date fieldDate = sdf.parse(str);
                        return new java.sql.Time(fieldDate.getTime());
                    } catch (ParseException e) {
                        throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                    }
                }
            } else if ("Timestamp".equals(type) || "java.sql.Timestamp".equals(type)) {
                if (format == null || format.length() == 0) {
                    try {
                        return java.sql.Timestamp.valueOf(str);
                    } catch (Exception e) {
                        try {
                            DateFormat df = null;
                            if (locale != null) {
                                df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
                            } else {
                                df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                            }
                            Date fieldDate = df.parse(str);
                            return new java.sql.Timestamp(fieldDate.getTime());
                        } catch (ParseException e1) {
                            throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                        }
                    }
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        java.util.Date fieldDate = sdf.parse(str);
                        return new java.sql.Timestamp(fieldDate.getTime());
                    } catch (ParseException e) {
                        throw new GeneralException("Could not convert " + str + " to " + type + ": ", e);
                    }
                }
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                if (str.startsWith("[") && str.endsWith("]")) {
                    return StringUtil.toList(str);
                } else {
                    List tempList = FastList.newInstance();
                    tempList.add(str);
                    return tempList;
                }
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                if (str.startsWith("[") && str.endsWith("]")) {
                    return StringUtil.toSet(str);
                } else {
                    Set tempSet = FastSet.newInstance();
                    tempSet.add(str);
                    return tempSet;
                }
            } else if (("Map".equals(type) || "java.util.Map".equals(type)) && (str.startsWith("{") && str.endsWith("}"))) {
                return StringUtil.toMap(str);
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof Double) {
            fromType = "Double";
            Double dbl = (Double) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                NumberFormat nf = null;
                if (locale == null) {
                    nf = NumberFormat.getNumberInstance();
                } else {
                    nf = NumberFormat.getNumberInstance(locale);
                }
                return nf.format(dbl.doubleValue());
            } else if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type)) {
                return new BigDecimal(dbl.doubleValue());
            } else if ("Double".equals(type) || "java.lang.Double".equals(type)) {
                return obj;
            } else if ("Float".equals(type) || "java.lang.Float".equals(type)) {
                return new Float(dbl.floatValue());
            } else if ("Long".equals(type) || "java.lang.Long".equals(type)) {
                return new Long(Math.round(dbl.doubleValue()));
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                return new Integer((int) Math.round(dbl.doubleValue()));
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(dbl);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(dbl);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof Float) {
            fromType = "Float";
            Float flt = (Float) obj;
            if ("String".equals(type)) {
                NumberFormat nf = null;
                if (locale == null) nf = NumberFormat.getNumberInstance(); else nf = NumberFormat.getNumberInstance(locale);
                return nf.format(flt.doubleValue());
            } else if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type)) {
                return new BigDecimal(flt.doubleValue());
            } else if ("Double".equals(type)) {
                return new Double(flt.doubleValue());
            } else if ("Float".equals(type)) {
                return obj;
            } else if ("Long".equals(type)) {
                return new Long(Math.round(flt.doubleValue()));
            } else if ("Integer".equals(type)) {
                return new Integer((int) Math.round(flt.doubleValue()));
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(flt);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(flt);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof Long) {
            fromType = "Long";
            Long lng = (Long) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                NumberFormat nf = null;
                if (locale == null) {
                    nf = NumberFormat.getNumberInstance();
                } else {
                    nf = NumberFormat.getNumberInstance(locale);
                }
                return nf.format(lng.longValue());
            } else if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type)) {
                return BigDecimal.valueOf(lng.longValue());
            } else if ("Double".equals(type) || "java.lang.Double".equals(type)) {
                return new Double(lng.doubleValue());
            } else if ("Float".equals(type) || "java.lang.Float".equals(type)) {
                return new Float(lng.floatValue());
            } else if ("Long".equals(type) || "java.lang.Long".equals(type)) {
                return obj;
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                return new Integer(lng.intValue());
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(lng);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(lng);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof Integer) {
            fromType = "Integer";
            Integer intgr = (Integer) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                NumberFormat nf = null;
                if (locale == null) {
                    nf = NumberFormat.getNumberInstance();
                } else {
                    nf = NumberFormat.getNumberInstance(locale);
                }
                return nf.format(intgr.longValue());
            } else if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type)) {
                return BigDecimal.valueOf(intgr.longValue());
            } else if ("Double".equals(type) || "java.lang.Double".equals(type)) {
                return new Double(intgr.doubleValue());
            } else if ("Float".equals(type) || "java.lang.Float".equals(type)) {
                return new Float(intgr.floatValue());
            } else if ("Long".equals(type) || "java.lang.Long".equals(type)) {
                return new Long(intgr.longValue());
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                return obj;
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(intgr);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(intgr);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof BigDecimal) {
            fromType = "BigDecimal";
            BigDecimal bigDec = (BigDecimal) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                NumberFormat nf = null;
                if (locale == null) {
                    nf = NumberFormat.getNumberInstance();
                } else {
                    nf = NumberFormat.getNumberInstance(locale);
                }
                return nf.format(bigDec.doubleValue());
            } else if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type)) {
                return obj;
            } else if ("Double".equals(type) || "java.lang.Double".equals(type)) {
                return new Double(bigDec.doubleValue());
            } else if ("Float".equals(type) || "java.lang.Float".equals(type)) {
                return new Float(bigDec.floatValue());
            } else if ("Long".equals(type) || "java.lang.Long".equals(type)) {
                return new Long(bigDec.longValue());
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                return new Integer(bigDec.intValue());
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(bigDec);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(bigDec);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.sql.Date) {
            fromType = "Date";
            java.sql.Date dte = (java.sql.Date) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                if (format == null || format.length() == 0) {
                    return dte.toString();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    return sdf.format(new java.util.Date(dte.getTime()));
                }
            } else if ("Date".equals(type) || "java.sql.Date".equals(type)) {
                return obj;
            } else if ("Time".equals(type) || "java.sql.Time".equals(type)) {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            } else if ("Timestamp".equals(type) || "java.sql.Timestamp".equals(type)) {
                return new java.sql.Timestamp(dte.getTime());
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(dte);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(dte);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.sql.Time) {
            fromType = "Time";
            java.sql.Time tme = (java.sql.Time) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                if (format == null || format.length() == 0) {
                    return tme.toString();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    return sdf.format(new java.util.Date(tme.getTime()));
                }
            } else if ("Date".equals(type) || "java.sql.Date".equals(type)) {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            } else if ("Time".equals(type) || "java.sql.Time".equals(type)) {
                return obj;
            } else if ("Timestamp".equals(type) || "java.sql.Timestamp".equals(type)) {
                return new java.sql.Timestamp(tme.getTime());
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(tme);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(tme);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.sql.Timestamp) {
            fromType = "Timestamp";
            java.sql.Timestamp tme = (java.sql.Timestamp) obj;
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                if (format == null || format.length() == 0) {
                    return tme.toString();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    return sdf.format(new java.util.Date(tme.getTime()));
                }
            } else if ("Date".equals(type) || "java.sql.Date".equals(type)) {
                return new java.sql.Date(tme.getTime());
            } else if ("Time".equals(type) || "java.sql.Time".equals(type)) {
                return new java.sql.Time(tme.getTime());
            } else if ("Timestamp".equals(type) || "java.sql.Timestamp".equals(type)) {
                return obj;
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(tme);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(tme);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.lang.Boolean) {
            fromType = "Boolean";
            Boolean bol = (Boolean) obj;
            if ("Boolean".equals(type) || "java.lang.Boolean".equals(type)) {
                return bol;
            } else if ("String".equals(type) || "java.lang.String".equals(type)) {
                return bol.toString();
            } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                if (bol.booleanValue()) {
                    return new Integer(1);
                } else {
                    return new Integer(0);
                }
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(bol);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(bol);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.util.Locale) {
            fromType = "Locale";
            Locale loc = (Locale) obj;
            if ("Locale".equals(type) || "java.util.Locale".equals(type)) {
                return loc;
            } else if ("String".equals(type) || "java.lang.String".equals(type)) {
                return loc.toString();
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj.getClass().getName().equals("org.ofbiz.entity.GenericValue")) {
            fromType = "GenericValue";
            if ("GenericValue".equals(type) || "org.ofbiz.entity.GenericValue".equals(type)) {
                return obj;
            } else if ("Map".equals(type) || "java.util.Map".equals(type)) {
                return obj;
            } else if ("String".equals(type) || "java.lang.String".equals(type)) {
                return obj.toString();
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(obj);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(obj);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.util.Map) {
            fromType = "Map";
            Map map = (Map) obj;
            if ("Map".equals(type) || "java.util.Map".equals(type)) {
                return map;
            } else if ("String".equals(type) || "java.lang.String".equals(type)) {
                return map.toString();
            } else if ("List".equals(type) || "java.util.List".equals(type)) {
                List tempList = FastList.newInstance();
                tempList.add(obj);
                return tempList;
            } else if ("Set".equals(type) || "java.util.Set".equals(type)) {
                Set tempSet = FastSet.newInstance();
                tempSet.add(obj);
                return tempSet;
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else if (obj instanceof java.util.List) {
            fromType = "List";
            List list = (List) obj;
            if ("List".equals(type) || "java.util.List".equals(type)) {
                return list;
            } else if ("String".equals(type) || "java.lang.String".equals(type)) {
                return list.toString();
            } else {
                throw new GeneralException("Conversion from " + fromType + " to " + type + " not currently supported");
            }
        } else {
            if ("String".equals(type) || "java.lang.String".equals(type)) {
                Debug.logWarning("No special conversion available for " + obj.getClass().getName() + " to String, returning object.toString().", module);
                return obj.toString();
            }
            if (noTypeFail) {
                throw new GeneralException("Conversion from " + obj.getClass().getName() + " to " + type + " not currently supported");
            } else {
                Debug.logWarning("No type conversion available for " + obj.getClass().getName() + " to " + type + ", returning original object.", module);
                return obj;
            }
        }
    }
