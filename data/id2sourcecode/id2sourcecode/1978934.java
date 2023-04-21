    private void writeFile() {
        try {
            if (prop.getProperty("is_dao").equals("true")) {
                FileUtils.writeStringToFile(daoImplFile, readTpl(daoImplTpl));
                FileUtils.writeStringToFile(daoFile, readTpl(daoTpl));
            }
            if (prop.getProperty("is_manager").equals("true")) {
                FileUtils.writeStringToFile(managerImplFile, readTpl(managerImplTpl));
                FileUtils.writeStringToFile(managerFile, readTpl(managerTpl));
            }
            if (prop.getProperty("is_action").equals("true")) {
                FileUtils.writeStringToFile(actionFile, readTpl(actionTpl));
            }
            if (prop.getProperty("is_page").equals("true")) {
                FileUtils.writeStringToFile(pageListFile, readTpl(pageListTpl));
                FileUtils.writeStringToFile(pageAddFile, readTpl(pageAddTpl));
                FileUtils.writeStringToFile(pageEditFile, readTpl(pageEditTpl));
            }
            if (prop.getProperty("is_spring").equals("true")) {
                String springTplStr = readTpl(springTpl);
                String origSpring = FileUtils.readFileToString(springFile, "UTF-8");
                if (origSpring.indexOf(springTplStr) == -1) {
                    String newSpring = origSpring.replaceAll("</beans>", springTplStr + "</beans>");
                    FileUtils.writeStringToFile(springFile, newSpring, "UTF-8");
                }
            }
            if (prop.getProperty("is_struts").equals("true")) {
                String strutsTplStr = readTpl(strutsTpl);
                String origStruts = FileUtils.readFileToString(strutsFile, "UTF-8");
                if (origStruts.indexOf(strutsTplStr) == -1) {
                    String newStruts = origStruts.replaceAll("</struts>", strutsTplStr + "</struts>");
                    FileUtils.writeStringToFile(strutsFile, newStruts, "UTF-8");
                }
            }
            if (!"false".equals(prop.getProperty("is_validate"))) {
                FileUtils.writeStringToFile(vldSaveFile, readTpl(vldSaveTpl, "UTF-8"), "UTF-8");
                FileUtils.writeStringToFile(vldEditFile, readTpl(vldEditTpl, "UTF-8"), "UTF-8");
                FileUtils.writeStringToFile(vldUpdateFile, readTpl(vldUpdateTpl, "UTF-8"), "UTF-8");
            }
        } catch (IOException e) {
            log.warn("write file faild! " + e.getMessage());
        }
    }
