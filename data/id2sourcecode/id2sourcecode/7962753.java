    public ActionForward updateUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserForm userForm = (UserForm) form;
            UserService userService = Locator.lookupService(UserService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            User user = userService.getUserByUserId(userForm.getUserId());
            if (user == null) {
                response.getWriter().write("{success:true,message:'This user information has already been deleted'}");
                return mapping.findForward("");
            }
            List<User> userList = userService.getUserByUserLogin(userForm.getUserLogin());
            if (userList.size() > 0 && userList.get(0).getUserId() != userForm.getUserId()) {
                response.getWriter().write("{success:false,message:'User: " + userForm.getUserLogin() + " already existed'}");
                return mapping.findForward("");
            }
            user.setUserLogin(userForm.getUserLogin());
            user.setUserName(userForm.getUserName());
            user.setUserRemark(userForm.getUserRemark());
            userService.updateUser(user);
            response.getWriter().write("{success:true,message:'Modify user information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
