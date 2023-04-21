    public ActionForward deleteType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            TypeForm typeForm = (TypeForm) form;
            TypeService typeService = Locator.lookupService(TypeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Type type = typeService.getTypeByTypeId(typeForm.getTypeId(), true);
            if (type == null) {
                response.getWriter().write("{success:true,message:'This type information has already been deleted'}");
                return mapping.findForward("");
            }
            if (type.getDocuments().size() != 0) {
                response.getWriter().write("{success:true,message:'This type information has been attached to some document numbers, it can not be deleted'}");
                return mapping.findForward("");
            }
            typeService.deleteType(typeForm.getTypeId());
            response.getWriter().write("{success:true,message:'Successfully delete type information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
