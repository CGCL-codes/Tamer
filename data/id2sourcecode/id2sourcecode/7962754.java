    public ActionForward deleteUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            userService.deleteUser(userForm.getUserId());
            response.getWriter().write("{success:true,message:'Successfully delete user information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
