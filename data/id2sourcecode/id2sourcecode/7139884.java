    public ActionForward createLanguage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LanguageForm languageForm = (LanguageForm) form;
            Language language = new Language();
            LanguageService languageService = Locator.lookupService(LanguageService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (languageService.getLanguageByLanguageName(languageForm.getLanguageName()).size() > 0) {
                response.getWriter().write("{success:false,message:'Language name: " + languageForm.getLanguageName() + " already existed'}");
                return mapping.findForward("");
            }
            if (languageService.getLanguageByLanguageCode(languageForm.getLanguageCode()).size() > 0) {
                response.getWriter().write("{success:false,message:'Language code: " + languageForm.getLanguageCode() + " already existed'}");
                return mapping.findForward("");
            }
            language.setLanguageName(languageForm.getLanguageName());
            language.setLanguageCode(languageForm.getLanguageCode());
            language.setLanguageRemark(languageForm.getLanguageRemark());
            languageService.insertLanguage(language);
            response.getWriter().write("{success:true,message:'New language successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
