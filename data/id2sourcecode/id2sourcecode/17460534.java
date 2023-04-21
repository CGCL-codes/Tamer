    public List<VariableScope> execute(String name, VariableScope in) throws NBenchException {
        TrSpec spec = be.prepared_map.get(name);
        boolean autocommitable = (spec.action_specs.size() == 1);
        boolean dirty = false;
        List<VariableScope> rmap = new ArrayList<VariableScope>();
        int ret_val;
        try {
            conn.setAutoCommit(false);
            for (ActionSpec as : spec.action_specs) {
                PreparedStatement pstmt = lazy_get_pstmt(as);
                int pi = 1;
                for (String arg : as.arg_names) {
                    Variable var = in.getVariable(arg.toLowerCase());
                    Value val = var.getValue();
                    switch(var.getType()) {
                        case ValueType.STRING:
                            pstmt.setString(pi++, (String) val.getAs(ValueType.STRING));
                            break;
                        case ValueType.INT:
                            pstmt.setInt(pi++, ((Integer) val.getAs(ValueType.INT)).intValue());
                            break;
                        case ValueType.TIMESTAMP:
                            pstmt.setTimestamp(pi++, (Timestamp) val.getAs(ValueType.TIMESTAMP));
                            break;
                        case ValueType.NUMERIC:
                            pstmt.setBigDecimal(pi++, (BigDecimal) val.getAs(ValueType.NUMERIC));
                            break;
                        default:
                            throw error("unsupported variable" + var.toString());
                    }
                }
                switch(as.stmt_type) {
                    case ActionSpec.SELECT:
                        if (as.out) {
                            ResultSet rs = pstmt.executeQuery();
                            while (rs != null && rs.next()) {
                                rmap.add(res2vs(spec.out, rs));
                            }
                        } else {
                            ResultSet rs = pstmt.executeQuery();
                            if (rs != null) rs.close();
                        }
                        break;
                    case ActionSpec.INSERT:
                    case ActionSpec.DELETE:
                    case ActionSpec.UPDATE:
                        ret_val = pstmt.executeUpdate();
                        dirty = true;
                        break;
                    default:
                        throw error("unkown action type:" + as.stmt_type);
                }
            }
            if (dirty) conn.commit(); else conn.rollback();
            return rmap;
        } catch (Exception e) {
            e.printStackTrace();
            throw error(e.toString());
        }
    }
