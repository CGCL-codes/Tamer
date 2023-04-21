    public void readOptions() {
        try {
            prop.load(new FileInputStream(options_file));
            current_month_index = Integer.parseInt(prop.getProperty("current_month_index"));
            current_year_index = Integer.parseInt(prop.getProperty("current_year_index"));
            current_sector_index = Integer.parseInt(prop.getProperty("current_section_index"));
            current_locale = prop.getProperty("current_locale");
            windowWidth_FinanceGui = Integer.parseInt(prop.getProperty("windowWidth_FinanceGui"));
            windowHeight_FinanceGui = Integer.parseInt(prop.getProperty("WindowHeight_FinanceGui"));
            windowWidth_OverviewGui = Integer.parseInt(prop.getProperty("windowWidth_OverviewGui"));
            windowHeight_OverviewGui = Integer.parseInt(prop.getProperty("windowHeight_OverviewGui"));
            dividerLoc_Splitpane = Integer.parseInt(prop.getProperty("dividerLoc_Splitpane"));
            if (getColumns_file().exists()) {
                readArray(JammEnum.READ_COLUMNSIZES);
            }
            if (list_columnSizes.size() != 6) list_columnSizes = getDefaultColumnSizes();
            file_logs = Boolean.parseBoolean(prop.getProperty("write_logs"));
            console_logs = Boolean.parseBoolean(prop.getProperty("console_logs"));
            file_log_type = JammEnum.valueOf(prop.getProperty("write_log_type"));
            console_log_type = JammEnum.valueOf(prop.getProperty("console_log_type"));
            db_folder = new File(prop.getProperty("db_folder"));
            logger.writeLog(JammEnum.INFO, "Finished Options reading");
        } catch (IOException ex) {
            logger.writeLog(JammEnum.ERROR, "IOException during Options read");
            ex.printStackTrace();
        }
    }
