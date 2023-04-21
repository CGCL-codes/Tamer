    public void setValue(NameValueUnit value) throws AoException {
        String valName = value.valName;
        ApplAttr applAttr = this.applElem.getApplAttrByAaname(valName);
        if (applAttr == null) {
            throw new AoException(ErrorCode.AO_NOT_IMPLEMENTED, SeverityFlag.ERROR, 0, "InstanceElement::setValue()");
        }
        if ("AoLocalColumn".equals(this.applElem.getBename())) {
            String baName = applAttr.getBaName();
            if ("flags".equals(baName)) {
                this.getSvcVal().setFlags(value);
                return;
            } else if ("generation_parameters".equals(baName)) {
                this.getSvcVal().setGenerationParameters(value);
                return;
            } else if ("independent".equals(baName)) {
                this.getSvcVal().setIndepFlag(value.value.u.shortVal());
            } else if ("values".equals(baName)) {
                this.getSvcVal().setValues(value);
                return;
            }
        }
        if (("AoUser".equals(this.applElem.getBename())) && ("password".equals(applAttr.getBaName()))) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                value.value.u.stringVal(new String(md.digest(value.value.u.stringVal().getBytes())));
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage());
                throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "InstanceElement::setValue()");
            }
        }
        boolean isArray = false;
        boolean isBlob = false;
        boolean isByteStr = false;
        String dbtName = this.applElem.getSvcent().getDbtname();
        String dbcName = applAttr.getSvcAttr().getDbcname();
        StringBuffer sql = new StringBuffer(SqlHelper.format("update %s set %s=", new String[] { dbtName, dbcName }));
        switch(value.value.u.discriminator().value()) {
            case DataType._DT_UNKNOWN:
                break;
            case DataType._DT_STRING:
                sql.append("'");
                sql.append(value.value.u.stringVal());
                sql.append("'");
                break;
            case DataType._DT_SHORT:
                sql.append(value.value.u.shortVal());
                break;
            case DataType._DT_FLOAT:
                sql.append(value.value.u.floatVal());
                break;
            case DataType._DT_BOOLEAN:
                sql.append(value.value.u.booleanVal());
                break;
            case DataType._DT_BYTE:
                sql.append(value.value.u.byteVal());
                break;
            case DataType._DT_LONG:
                sql.append(value.value.u.longVal());
                break;
            case DataType._DT_DOUBLE:
                sql.append(value.value.u.doubleVal());
                break;
            case DataType._DT_LONGLONG:
            case DataType._DT_ID:
                long lval = de.ibk.ods.util.DataType.longlongToLong(value.value.u.longlongVal());
                sql.append(lval);
                break;
            case DataType._DT_DATE:
                sql.append("'");
                sql.append(value.value.u.dateVal());
                sql.append("'");
                break;
            case DataType._DT_BYTESTR:
                sql.append("?");
                isByteStr = true;
                break;
            case DataType._DT_BLOB:
                String header = value.value.u.blobVal().getHeader();
                isBlob = true;
                sql.append("'");
                sql.append(header);
                sql.append("'");
                break;
            case DataType._DT_COMPLEX:
            case DataType._DT_DCOMPLEX:
            case DataType._DS_STRING:
            case DataType._DS_SHORT:
            case DataType._DS_FLOAT:
            case DataType._DS_BOOLEAN:
            case DataType._DS_BYTE:
            case DataType._DS_LONG:
            case DataType._DS_DOUBLE:
            case DataType._DS_LONGLONG:
            case DataType._DS_COMPLEX:
            case DataType._DS_DCOMPLEX:
            case DataType._DS_ID:
            case DataType._DS_DATE:
            case DataType._DS_BYTESTR:
            case DataType._DT_EXTERNALREFERENCE:
            case DataType._DS_EXTERNALREFERENCE:
            case DataType._DS_ENUM:
                sql = new StringBuffer(SqlHelper.format("delete from %s_ARRAY where %s<>NULL and IID=%s", new String[] { dbtName, dbcName, Long.toString(this.iid) }));
                try {
                    kernel.getQueryHandler().executeUpdate(sql.toString());
                } catch (SQLException e) {
                    log.info(e.getMessage());
                }
                isArray = true;
                break;
            case DataType._DT_ENUM:
                sql.append(value.value.u.enumVal());
                break;
            default:
                break;
        }
        try {
            if (isArray) {
                saveArray(Long.toString(iid), dbtName, dbcName, value.value.u);
            } else {
                String idColName = this.applElem.getDbcnameByBaname("id");
                sql.append(" where ");
                sql.append(idColName);
                sql.append("=");
                sql.append(this.iid);
                if (isByteStr) {
                    kernel.getQueryHandler().executeUpdate(sql.toString(), value.value.u.bytestrVal());
                } else {
                    kernel.getQueryHandler().executeUpdate(sql.toString());
                    if (isBlob) {
                        sql = new StringBuffer(SqlHelper.format("update %s set BLOB=? where %s=%s", new String[] { dbtName, idColName, Long.toString(iid) }));
                        int len = value.value.u.blobVal().getLength();
                        kernel.getQueryHandler().executeUpdate(sql.toString(), value.value.u.blobVal().get(0, len));
                    }
                }
            }
        } catch (SQLException e) {
            log.fatal(e.getMessage());
            throw new AoException(ErrorCode.AO_IMPLEMENTATION_PROBLEM, SeverityFlag.ERROR, 0, "InstanceElement::setValue()");
        }
    }
