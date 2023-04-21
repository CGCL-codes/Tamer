    private Map<String, Serializable> LoadMapJR(HttpServletRequest req, List<JRParameter> jrParamJrxml, MDJasperReports jasperReportsObj, Map<String, Serializable> parameters, MDConfig config) throws Exception {
        for (MDJasperReportsParam jasperReportsParamObj : jasperReportsObj.fillParameter()) {
            JRParameter param = RunJasperReports2.getParameter(jrParamJrxml, jasperReportsParamObj);
            if (param == null) {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, config.getLocale(), "JasperReports_error_Parameter_not_found"), jasperReportsParamObj.getName(), jasperReportsObj.getLocation());
                throw new Exception(msg);
            }
            int type;
            String classType = param.getValueClassName();
            if (classType.equals("java.lang.String")) type = java.sql.Types.VARCHAR; else if (classType.equals("java.lang.Integer") || classType.equals("java.lang.Long") || classType.equals("java.lang.Short") || classType.equals("java.lang.Byte")) type = java.sql.Types.INTEGER; else if (classType.equals("java.lang.Boolean")) type = java.sql.Types.BOOLEAN; else if (classType.equals("java.sql.Date") || classType.equals("java.util.Date")) type = java.sql.Types.DATE; else if (classType.equals("java.lang.Double") || classType.equals("java.lang.Float") || classType.equals("java.math.BigDecimal")) type = java.sql.Types.DECIMAL; else {
                String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, config.getLocale(), "Unknown_Type"), classType, jasperReportsParamObj.getName());
                throw new Exception(msg);
            }
            String value = req.getParameter("attr" + jasperReportsParamObj.getId());
            String defaultValue = jasperReportsParamObj.getForceValue();
            switch(type) {
                case java.sql.Types.VARCHAR:
                    if (defaultValue != null && !defaultValue.equals("")) parameters.put(jasperReportsParamObj.getName(), defaultValue); else if (value != null && !value.equals("")) parameters.put(jasperReportsParamObj.getName(), value);
                    break;
                case java.sql.Types.INTEGER:
                    if (defaultValue != null && !defaultValue.equals("")) parameters.put(jasperReportsParamObj.getName(), new java.lang.Integer(defaultValue)); else if (value != null && !value.equals("")) parameters.put(jasperReportsParamObj.getName(), new java.lang.Integer(value));
                    break;
                case java.sql.Types.BOOLEAN:
                    if (defaultValue != null && defaultValue.equals("1")) parameters.put(jasperReportsParamObj.getName(), true); else if (value != null && value.equals("on")) parameters.put(jasperReportsParamObj.getName(), true); else parameters.put(jasperReportsParamObj.getName(), false);
                    break;
                case java.sql.Types.DECIMAL:
                    DecimalFormat df = new DecimalFormat(Defs.DECIMAL_FORMAT);
                    if (defaultValue != null && !defaultValue.equals("")) {
                        BigDecimal app = new BigDecimal(defaultValue);
                        parameters.put(jasperReportsParamObj.getName(), new java.math.BigDecimal(df.format(app.doubleValue())));
                    } else if (value != null && !value.equals("")) {
                        BigDecimal app = new BigDecimal(value);
                        parameters.put(jasperReportsParamObj.getName(), new java.math.BigDecimal(df.format(app.doubleValue())));
                    }
                    break;
                case java.sql.Types.DATE:
                    SimpleDateFormat sdf = new SimpleDateFormat(config.getLocaleInfo().getDateFormat());
                    sdf.setLenient(false);
                    if (defaultValue != null && !defaultValue.equals("")) parameters.put(jasperReportsParamObj.getName(), sdf.parse(defaultValue)); else if (value != null && !value.equals("")) parameters.put(jasperReportsParamObj.getName(), sdf.parse(value));
                    break;
                default:
                    String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, config.getLocale(), "Unknown_Type"), type, jasperReportsParamObj.getName());
                    throw new Exception(msg);
            }
        }
        return parameters;
    }
