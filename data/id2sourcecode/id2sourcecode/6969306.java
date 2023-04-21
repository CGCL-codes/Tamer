    public ActionForward deleteAttribute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AttributeForm attributeForm = (AttributeForm) form;
            AttributeService attributeService = Locator.lookupService(AttributeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Attribute attribute = attributeService.getAttributeByAttributeId(attributeForm.getAttributeId(), true);
            if (attribute == null) {
                response.getWriter().write("{success:true,message:'This attribute information has already been deleted'}");
                return mapping.findForward("");
            }
            if (attribute.getDocuments().size() != 0) {
                response.getWriter().write("{success:true,message:'This attribute information has been attached to some document numbers, it can not be deleted'}");
                return mapping.findForward("");
            }
            attributeService.deleteAttribute(attributeForm.getAttributeId());
            response.getWriter().write("{success:true,message:'Successfully delete attribute information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
