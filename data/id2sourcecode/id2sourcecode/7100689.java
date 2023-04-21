    public void validateUrlFormat(FacesContext context, UIComponent toValidate, Object value) {
        if (log.isDebugEnabled()) {
            log.debug("Validating URL: " + value);
        }
        currentPsiReport = null;
        URL url = null;
        CoreInputText inputCompToValidate = (CoreInputText) toValidate;
        String toValidateClientId = inputCompToValidate.getClientId(context);
        try {
            url = new URL((String) value);
        } catch (MalformedURLException e) {
            log.warn("Invalid URL given by the user: " + value, e);
            inputCompToValidate.setValid(false);
            context.addMessage(toValidateClientId, new FacesMessage("The given URL was not valid."));
            return;
        }
        try {
            url.openStream();
        } catch (Throwable e) {
            log.error("Error while validating the URL.", e);
            inputCompToValidate.setValid(false);
            context.addMessage(toValidateClientId, new FacesMessage("Could not read URL content."));
        }
    }
