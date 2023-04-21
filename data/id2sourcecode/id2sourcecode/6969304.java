    public ActionForward createAttribute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AttributeForm attributeForm = (AttributeForm) form;
            Attribute attribute = new Attribute();
            AttributeService attributeService = Locator.lookupService(AttributeService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (attributeService.getAttributeByAttributeName(attributeForm.getAttributeName()).size() > 0) {
                response.getWriter().write("{success:false,message:'Attribute name: " + attributeForm.getAttributeName() + " already existed'}");
                return mapping.findForward("");
            }
            if (attributeService.getAttributeByAttributeCode(attributeForm.getAttributeCode()).size() > 0) {
                response.getWriter().write("{success:false,message:'Attribute code: " + attributeForm.getAttributeCode() + " already existed'}");
                return mapping.findForward("");
            }
            attribute.setAttributeName(attributeForm.getAttributeName());
            attribute.setAttributeCode(attributeForm.getAttributeCode());
            attribute.setAttributeRemark(attributeForm.getAttributeRemark());
            attributeService.insertAttribute(attribute);
            response.getWriter().write("{success:true,message:'New attribute successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
