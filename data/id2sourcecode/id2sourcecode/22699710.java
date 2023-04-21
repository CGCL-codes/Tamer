    public void validateUrlFormat(FacesContext context, UIComponent toValidate, Object value) {
        if (log.isDebugEnabled()) {
            log.debug("Validating URL: " + value);
        }
        currentPsiReport = null;
        URL url = null;
        UIInput inputCompToValidate = (UIInput) toValidate;
        String toValidateClientId = inputCompToValidate.getClientId(context);
        try {
            url = new URL((String) value);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            inputCompToValidate.setValid(false);
            context.addMessage(toValidateClientId, new FacesMessage("Not a valid URL"));
            return;
        }
        try {
            url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            inputCompToValidate.setValid(false);
            context.addMessage(toValidateClientId, new FacesMessage("Unknown URL"));
        }
    }
