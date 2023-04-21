    public ActionForward updateAttribute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            List<Attribute> attributeList = attributeService.getAttributeByAttributeName(attributeForm.getAttributeName());
            if (attributeList.size() > 0 && attributeList.get(0).getAttributeId() != attributeForm.getAttributeId()) {
                response.getWriter().write("{success:false,message:'Attribute name: " + attributeForm.getAttributeName() + " already existed'}");
                return mapping.findForward("");
            }
            attributeList = attributeService.getAttributeByAttributeCode(attributeForm.getAttributeCode());
            if (attributeList.size() > 0 && attributeList.get(0).getAttributeId() != attributeForm.getAttributeId()) {
                response.getWriter().write("{success:false,message:'Attribute code: " + attributeForm.getAttributeCode() + " already existed'}");
                return mapping.findForward("");
            }
            attribute.setAttributeName(attributeForm.getAttributeName());
            attribute.setAttributeCode(attributeForm.getAttributeCode());
            attribute.setAttributeRemark(attributeForm.getAttributeRemark());
            attributeService.updateAttribute(attribute);
            response.getWriter().write("{success:true,message:'Modify attribute information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
