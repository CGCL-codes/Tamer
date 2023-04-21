    public ActionForward updateType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            List<Type> typeList = typeService.getTypeByTypeName(typeForm.getTypeName());
            if (typeList.size() > 0 && typeList.get(0).getTypeId() != typeForm.getTypeId()) {
                response.getWriter().write("{success:false,message:'Type name: " + typeForm.getTypeName() + " already existed'}");
                return mapping.findForward("");
            }
            typeList = typeService.getTypeByTypeCode(typeForm.getTypeCode());
            if (typeList.size() > 0 && typeList.get(0).getTypeId() != typeForm.getTypeId()) {
                response.getWriter().write("{success:false,message:'Type code: " + typeForm.getTypeCode() + " already existed'}");
                return mapping.findForward("");
            }
            typeList = typeService.getTypeByTypeAbbreviation(typeForm.getTypeAbbreviation());
            if (typeList.size() > 0 && typeList.get(0).getTypeId() != typeForm.getTypeId()) {
                response.getWriter().write("{success:false,message:'Type abbreviation: " + typeForm.getTypeAbbreviation() + " already existed'}");
                return mapping.findForward("");
            }
            type.setTypeName(typeForm.getTypeName());
            type.setTypeCode(typeForm.getTypeCode());
            type.setTypeAbbreviation(typeForm.getTypeAbbreviation());
            type.setShouldReviewed(typeForm.isShouldReviewed());
            type.setTypeRemark(typeForm.getTypeRemark());
            typeService.updateType(type);
            response.getWriter().write("{success:true,message:'Modify type information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
