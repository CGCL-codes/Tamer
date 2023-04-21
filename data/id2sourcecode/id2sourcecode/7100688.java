    private void uploadFromUrl() throws IOException {
        if (log.isInfoEnabled()) {
            log.info("Uploading Url: " + psiUrl);
        }
        try {
            URL url = new URL(psiUrl);
            File f = storeAsTemporaryFile(url.openStream());
            String name = psiUrl.substring(psiUrl.lastIndexOf("/") + 1, psiUrl.length());
            PsiReportBuilder builder = new PsiReportBuilder(name, url, f, model, validationScope, progressModel);
            log.warn("About to start building the PSI report");
            this.currentPsiReport = builder.createPsiReport();
            log.warn("After uploading a URL the report was " + (this.currentPsiReport == null ? "not present" : "present"));
        } catch (Throwable e) {
            currentPsiReport = null;
            final String msg = "The given URL wasn't valid";
            log.error(msg, e);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
            context.addMessage(null, message);
        }
    }
