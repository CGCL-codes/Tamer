    public String updateUser() {
        String result = "";
        String fn = "";
        try {
            String path = servletRequest.getSession().getServletContext().getRealPath("/upload/user/photo");
            if (upload != null && upload.exists()) {
                fn = "pi_" + uVO.getSabun() + ".jpg";
                saveFile = new File(path + "\\" + fn);
                FileUtils.copyFile(upload, saveFile);
                imgProc.createFixedSize(path, fn);
                uVO.setPhotofn(fn);
            }
            uVO.setSlevel(orgDAO.getSlevel(uVO.getJobno()));
            userDAO.updateUser(uVO);
            ActionContext context = ActionContext.getContext();
            Map<String, UserVO> session = (Map<String, UserVO>) context.getSession();
            uVO = userDAO.getUserInfo(uVO.getSabun());
            session.put("user", uVO);
            context.setSession(session);
            result = "success";
        } catch (Exception e) {
            System.out.println("UserInputAction.updateUser():" + e.toString());
            result = "error";
        }
        return result;
    }
