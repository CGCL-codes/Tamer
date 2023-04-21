    private void configureDb(Configuration configuration) {
        List<Parameter> parameterList = configuration.getCubridParameters();
        if (parameterList == null) {
            return;
        }
        StringBuilder currentSb = new StringBuilder();
        StringBuilder defaultSb = new StringBuilder();
        for (int i = 0; i < parameterList.size(); i++) {
            Parameter parameter = (Parameter) parameterList.get(i);
            if (parameter == null) {
                continue;
            }
            String name = parameter.getParameter();
            if (name == null) {
                continue;
            }
            String value = StringUtil.nullToEmpty(parameter.getCurrentValue());
            if (value != null) {
                boolean isNumber = true;
                try {
                    Double.parseDouble(value);
                } catch (Exception e) {
                    isNumber = false;
                }
                if (isNumber) {
                } else {
                    if (value.equalsIgnoreCase("null")) {
                        value = "NULL";
                    } else {
                        value = value;
                    }
                }
            } else {
                value = "";
            }
            currentSb.append(name + "=" + value + System.getProperty("line.separator"));
            defaultSb.append(name + "=" + value + System.getProperty("line.separator"));
        }
        String currentConf = currentSb.toString();
        String defaultConf = defaultSb.toString();
        String confFileName = FileUtil.getFileName(CubridUtil.getCubridBrokerConfFile());
        String currentConfFile = SystemUtil.getUserHomePath() + "/" + confFileName;
        FileUtil.writeToFile(currentConfFile, currentConf);
        currentDbConfFile = currentConfFile;
        String defaultConfFile = CubridUtil.getDefaultSqlXConfFile();
        String readFile = FileUtil.readFile(defaultConfFile);
        FileUtil.writeToFile(defaultConfFile, readFile + System.getProperty("line.separator") + defaultConf);
        this.onMessage("[configure /conf/cubrid.conf]:" + defaultConfFile);
    }
