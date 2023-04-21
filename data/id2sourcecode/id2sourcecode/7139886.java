    public ActionForward deleteLanguage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LanguageForm languageForm = (LanguageForm) form;
            LanguageService languageService = Locator.lookupService(LanguageService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Language language = languageService.getLanguageByLanguageId(languageForm.getLanguageId(), true);
            if (language == null) {
                response.getWriter().write("{success:true,message:'This language information has already been deleted'}");
                return mapping.findForward("");
            }
            if (language.getDocuments().size() != 0) {
                response.getWriter().write("{success:true,message:'This language information has been attached to some document numbers, it can not be deleted'}");
                return mapping.findForward("");
            }
            languageService.deleteLanguage(languageForm.getLanguageId());
            response.getWriter().write("{success:true,message:'Successfully delete language information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
