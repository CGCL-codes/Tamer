    public ActionForward updateLanguage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            List<Language> languageList = languageService.getLanguageByLanguageName(languageForm.getLanguageName());
            if (languageList.size() > 0 && languageList.get(0).getLanguageId() != languageForm.getLanguageId()) {
                response.getWriter().write("{success:false,message:'Language name: " + languageForm.getLanguageName() + " already existed'}");
                return mapping.findForward("");
            }
            languageList = languageService.getLanguageByLanguageCode(languageForm.getLanguageCode());
            if (languageList.size() > 0 && languageList.get(0).getLanguageId() != languageForm.getLanguageId()) {
                response.getWriter().write("{success:false,message:'Language code: " + languageForm.getLanguageCode() + " already existed'}");
                return mapping.findForward("");
            }
            language.setLanguageName(languageForm.getLanguageName());
            language.setLanguageCode(languageForm.getLanguageCode());
            language.setLanguageRemark(languageForm.getLanguageRemark());
            languageService.updateLanguage(language);
            response.getWriter().write("{success:true,message:'Modify language information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
