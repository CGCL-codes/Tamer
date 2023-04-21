    public String execute() {
        String result = "";
        try {
            String path = servletRequest.getSession().getServletContext().getRealPath("/ssteam/upload/user/photo");
            path = "d:\\upload";
            if (upload != null && upload.exists()) {
                saveFile = new File(path + "\\" + uploadFileName);
                FileUtils.copyFile(upload, saveFile);
            }
            uVO.setPhotofn(uploadFileName);
            uVO.setSlevel(orgDAO.getSlevel(uVO.getJobno()));
            adminDAO.UpdateOk(uVO);
            result = "success";
        } catch (Exception e) {
            result = "error";
        }
        return "success";
    }
