    public ActionForward createUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserForm userForm = (UserForm) form;
            User user = new User();
            UserService userService = Locator.lookupService(UserService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (userService.getUserByUserLogin(userForm.getUserLogin()).size() != 0) {
                response.getWriter().write("{success:false,message:'User: " + userForm.getUserLogin() + " already existed'}");
                return mapping.findForward("");
            }
            user.setUserLogin(userForm.getUserLogin());
            user.setUserName(userForm.getUserName());
            user.setUserRemark(userForm.getUserRemark());
            userService.insertUser(user);
            response.getWriter().write("{success:true,message:'New user successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
