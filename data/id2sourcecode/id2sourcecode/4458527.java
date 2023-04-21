    public static void setParameter(int index, PreparedStatement cs, SqlParameter param) throws SQLException {
        Object value = param.getValue();
        SqlType type = param.getType();
        if (value == null) {
            cs.setNull(index, type.type());
            return;
        }
        switch(type) {
            case ARRAY:
                cs.setArray(index, (Array) value);
                break;
            case BIGINT:
                if (value instanceof String) {
                    value = Long.valueOf((String) value);
                }
                cs.setLong(index, ((Long) value).longValue());
                break;
            case BLOB:
                try {
                    if (value instanceof File) {
                        value = new FileInputStream((File) value);
                    } else if (value instanceof Reader) {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        Reader reader = (Reader) value;
                        int i = -1;
                        while ((i = reader.read()) != -1) {
                            output.write(i);
                        }
                        value = new ByteArrayInputStream(output.toByteArray());
                    }
                    cs.setBinaryStream(index, (InputStream) value, ((InputStream) value).available());
                } catch (IOException e) {
                    throw new SQLException(e.getMessage());
                }
                break;
            case BOOLEAN:
                if (value instanceof String) {
                    value = Boolean.valueOf((String) value);
                }
                cs.setBoolean(index, ((Boolean) value).booleanValue());
                break;
            case CHAR:
                if (value instanceof Character) {
                    value = new String(((Character) value).toString());
                }
                cs.setString(index, (String) value);
                break;
            case CLOB:
                try {
                    if (value instanceof File) {
                        value = new FileInputStream((File) value);
                    } else if (value instanceof Reader) {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        Reader reader = (Reader) value;
                        int i = -1;
                        while ((i = reader.read()) != -1) {
                            output.write(i);
                        }
                        value = new ByteArrayInputStream(output.toByteArray());
                    }
                    int size = ((InputStream) value).available();
                    value = new InputStreamReader((InputStream) value);
                    cs.setCharacterStream(index, (Reader) value, size);
                } catch (IOException e) {
                    throw new SQLException(e.getMessage());
                }
                break;
            case DATE:
                if (value instanceof Long) {
                    value = new Date(((Long) value).longValue());
                } else if (value instanceof String) {
                    value = Date.valueOf((String) value);
                } else if ((value instanceof java.util.Date) && (!(value instanceof Date))) {
                    value = new Date(((java.util.Date) value).getTime());
                }
                cs.setDate(index, (Date) value);
                break;
            case DECIMAL:
                if (value instanceof String) {
                    value = Integer.valueOf((String) value);
                }
                cs.setInt(index, ((Integer) value).intValue());
                break;
            case DOUBLE:
                if (value instanceof String) {
                    value = Double.valueOf((String) value);
                }
                cs.setDouble(index, ((Double) value).doubleValue());
                break;
            case FLOAT:
                if (value instanceof String) {
                    value = Float.valueOf((String) value);
                }
                cs.setFloat(index, ((Float) value).floatValue());
                break;
            case INTEGER:
                if (value instanceof String) {
                    value = Integer.valueOf((String) value);
                }
                cs.setInt(index, ((Integer) value).intValue());
                break;
            case NUMERIC:
                if (value instanceof String) {
                    value = Integer.valueOf((String) value);
                    value = new BigDecimal(((Integer) value).intValue());
                } else if (value instanceof Integer) {
                    value = new BigDecimal(((Integer) value).intValue());
                }
                cs.setBigDecimal(index, (BigDecimal) value);
                break;
            case OBJECT:
                cs.setObject(index, value);
                break;
            case OTHER:
                cs.setObject(index, value);
                break;
            case REAL:
                if (value instanceof String) {
                    value = Float.valueOf((String) value);
                }
                cs.setFloat(index, ((Float) value).floatValue());
                break;
            case REF:
                cs.setRef(index, (Ref) value);
                break;
            case STRUCT:
                cs.setObject(index, value);
                break;
            case SMALLINT:
                if (value instanceof String) {
                    value = Short.valueOf((String) value);
                }
                cs.setShort(index, ((Short) value).shortValue());
                break;
            case TIME:
                if (value instanceof Long) {
                    value = new Date(((Long) value).longValue());
                } else if (value instanceof String) {
                    value = Date.valueOf((String) value);
                }
                cs.setTime(index, (Time) value);
                break;
            case TIMESTAMP:
                if (value instanceof Long) {
                    value = new Timestamp(((Long) value).longValue());
                } else if (value instanceof String) {
                    value = Timestamp.valueOf((String) value);
                } else if ((value instanceof java.util.Date) && (!(value instanceof Timestamp))) {
                    value = new Timestamp(((java.util.Date) value).getTime());
                }
                cs.setTimestamp(index, (Timestamp) value);
                break;
            case TINYINT:
                if (value instanceof String) {
                    value = Byte.valueOf((String) value);
                }
                cs.setByte(index, ((Byte) value).byteValue());
                break;
            case VARCHAR:
                if (!(value instanceof String)) {
                    value = value.toString();
                }
                cs.setString(index, (String) value);
                break;
            default:
                throw new SQLException("database.SqlType.type-unsupported");
        }
    }
