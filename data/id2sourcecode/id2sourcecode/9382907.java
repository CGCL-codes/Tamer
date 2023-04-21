    public String getTemplate(String templateType) {
        String template = Setting.systemPathBirt + "/" + getBirtApplName() + "/template/" + templateType + ".rptdesign";
        String nameRptdesign = BirtUtil.randomNameFile();
        String renameTemplate = System.getProperty("java.io.tmpdir") + File.separator + nameRptdesign;
        try {
            FileUtils.copyFile(new File(template), new File(renameTemplate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(template);
        System.out.println(renameTemplate);
        return nameRptdesign;
    }
