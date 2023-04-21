    @SuppressWarnings("unchecked")
    private Object convertPrimitive(Class clazz, Object value, Method method) throws JSONException {
        if (value == null) {
            if (Short.TYPE.equals(clazz) || Short.class.equals(clazz)) return (short) 0; else if (Byte.TYPE.equals(clazz) || Byte.class.equals(clazz)) return (byte) 0; else if (Integer.TYPE.equals(clazz) || Integer.class.equals(clazz)) return 0; else if (Long.TYPE.equals(clazz) || Long.class.equals(clazz)) return 0L; else if (Float.TYPE.equals(clazz) || Float.class.equals(clazz)) return 0f; else if (Double.TYPE.equals(clazz) || Double.class.equals(clazz)) return 0d; else if (Boolean.TYPE.equals(clazz) || Boolean.class.equals(clazz)) return Boolean.FALSE; else return null;
        } else if (value instanceof Number) {
            Number number = (Number) value;
            if (Short.TYPE.equals(clazz)) return number.shortValue(); else if (Short.class.equals(clazz)) return new Short(number.shortValue()); else if (Byte.TYPE.equals(clazz)) return number.byteValue(); else if (Byte.class.equals(clazz)) return new Byte(number.byteValue()); else if (Integer.TYPE.equals(clazz)) return number.intValue(); else if (Integer.class.equals(clazz)) return new Integer(number.intValue()); else if (Long.TYPE.equals(clazz)) return number.longValue(); else if (Long.class.equals(clazz)) return new Long(number.longValue()); else if (Float.TYPE.equals(clazz)) return number.floatValue(); else if (Float.class.equals(clazz)) return new Float(number.floatValue()); else if (Double.TYPE.equals(clazz)) return number.doubleValue(); else if (Double.class.equals(clazz)) return new Double(number.doubleValue()); else if (String.class.equals(clazz)) return value.toString();
        } else if (clazz.equals(Date.class)) {
            try {
                JSON json = method.getAnnotation(JSON.class);
                DateFormat formatter = new SimpleDateFormat((json != null) && (json.format().length() > 0) ? json.format() : this.dateFormat);
                return formatter.parse((String) value);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
                throw new JSONException("Unable to parse date from: " + value);
            }
        } else if (clazz.isEnum()) {
            String sValue = (String) value;
            return Enum.valueOf(clazz, sValue);
        } else if (value instanceof String) {
            String sValue = (String) value;
            if (Boolean.TYPE.equals(clazz)) return Boolean.parseBoolean(sValue); else if (Boolean.class.equals(clazz)) return Boolean.valueOf(sValue); else if (Short.TYPE.equals(clazz)) return Short.parseShort(sValue); else if (Short.class.equals(clazz)) return Short.valueOf(sValue); else if (Byte.TYPE.equals(clazz)) return Byte.parseByte(sValue); else if (Byte.class.equals(clazz)) return Byte.valueOf(sValue); else if (Integer.TYPE.equals(clazz)) return Integer.parseInt(sValue); else if (Integer.class.equals(clazz)) return Integer.valueOf(sValue); else if (Long.TYPE.equals(clazz)) return Long.parseLong(sValue); else if (Long.class.equals(clazz)) return Long.valueOf(sValue); else if (Float.TYPE.equals(clazz)) return Float.parseFloat(sValue); else if (Float.class.equals(clazz)) return Float.valueOf(sValue); else if (Double.TYPE.equals(clazz)) return Double.parseDouble(sValue); else if (Double.class.equals(clazz)) return Double.valueOf(sValue); else if (Character.TYPE.equals(clazz) || Character.class.equals(clazz)) {
                char charValue = 0;
                if (sValue.length() > 0) {
                    charValue = sValue.charAt(0);
                }
                if (Character.TYPE.equals(clazz)) return charValue; else return new Character(charValue);
            } else if (clazz.equals(Locale.class)) {
                String[] components = sValue.split("_", 2);
                if (components.length == 2) {
                    return new Locale(components[0], components[1]);
                } else {
                    return new Locale(sValue);
                }
            } else if (Enum.class.isAssignableFrom(clazz)) {
                return Enum.valueOf(clazz, sValue);
            }
        }
        return value;
    }
