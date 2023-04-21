    public ActionForward createType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            TypeForm typeForm = (TypeForm) form;
            Type type = new Type();
            TypeService typeService = Locator.lookupService(TypeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (typeService.getTypeByTypeName(typeForm.getTypeName()).size() > 0) {
                response.getWriter().write("{success:false,message:'Type name: " + typeForm.getTypeName() + " already existed'}");
                return mapping.findForward("");
            }
            if (typeService.getTypeByTypeCode(typeForm.getTypeCode()).size() > 0) {
                response.getWriter().write("{success:false,message:'Type code: " + typeForm.getTypeCode() + " already existed'}");
                return mapping.findForward("");
            }
            if (typeService.getTypeByTypeAbbreviation(typeForm.getTypeAbbreviation()).size() > 0) {
                response.getWriter().write("{success:false,message:'Type abbreviation: " + typeForm.getTypeAbbreviation() + " already existed'}");
                return mapping.findForward("");
            }
            type.setTypeName(typeForm.getTypeName());
            type.setTypeCode(typeForm.getTypeCode());
            type.setTypeAbbreviation(typeForm.getTypeAbbreviation());
            type.setShouldReviewed(typeForm.isShouldReviewed());
            type.setTypeRemark(typeForm.getTypeRemark());
            typeService.insertType(type);
            response.getWriter().write("{success:true,message:'New type successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
