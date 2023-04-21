    private void uploadFromUrl() throws IOException {
        if (psiUrl == null) {
            throw new IllegalStateException("Failed to read the URL");
        }
        if (log.isInfoEnabled()) {
            log.info("Uploading Url: " + psiUrl);
        }
        try {
            URL url = new URL(psiUrl);
            String name = psiUrl.substring(psiUrl.lastIndexOf(File.separator) + 1, psiUrl.length());
            boolean successful;
            if (psiUrl.endsWith(ZIP_EXTENSION)) {
                successful = unpackArchive(url.openStream());
            } else {
                setUpValidatorReport(name, url.openStream(), url.openStream());
                successful = true;
            }
            if (!successful) {
                final String msg = "The given URL (" + psiUrl + ") does not point to any XML files to validate";
                log.error(msg);
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                context.addMessage(null, message);
            }
        } catch (Throwable e) {
            currentPsiReport = null;
            final String msg = "The given URL wasn't valid";
            log.error(msg, e);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
            context.addMessage(null, message);
        }
    }
