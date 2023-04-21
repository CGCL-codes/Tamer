    @Override
    public String execute() throws Exception {
        if (!LForumRequest.isPost()) {
            bindDataGrid();
        }
        String submitMethod = LForumRequest.getParamValue("submitMethod");
        if (!submitMethod.equals("")) {
            submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
            if (submitMethod.equals("backupfile")) {
                backupfile();
            } else if (submitMethod.equals("delbackupfile")) {
                delbackupfile();
            }
        }
        if (!LForumRequest.getParamValue("filename").equals("")) {
            FileUtils.copyFile(new File(config.getWebpath() + "admin/xml/backup/" + LForumRequest.getParamValue("filename")), new File(config.getWebpath() + "admin/xml/navmenu.xml"));
            MenuManager.createMenuJson();
            registerStartupScript("", "<script>alert('恢复成功！');window.location.href='managemainmenu.action';</script>");
        }
        return SUCCESS;
    }
