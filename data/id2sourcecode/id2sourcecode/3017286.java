    @Override
    protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        dictionary.put(PdfNameConstants.E, helper.getElement(this.onEnterAction));
        dictionary.put(PdfNameConstants.X, helper.getElement(this.onExitAction));
        dictionary.put(PdfNameConstants.D, helper.getElement(this.onMouseDownAction));
        dictionary.put(PdfNameConstants.U, helper.getElement(this.onMouseUpAction));
        dictionary.put(PdfNameConstants.FO, helper.getElement(this.onFocusAction));
        dictionary.put(PdfNameConstants.BL, helper.getElement(this.onBlurAction));
        if (this.onPageOpenAction != null && context.supportsPdfVersion(PdfVersion.VERSION_1_5)) {
            dictionary.put(PdfNameConstants.P_O, helper.getElement(this.onPageOpenAction));
        }
        if (this.onPageCloseAction != null && context.supportsPdfVersion(PdfVersion.VERSION_1_5)) {
            dictionary.put(PdfNameConstants.P_C, helper.getElement(this.onPageCloseAction));
        }
        if (this.onPageVisibleAction != null && context.supportsPdfVersion(PdfVersion.VERSION_1_5)) {
            dictionary.put(PdfNameConstants.P_V, helper.getElement(this.onPageVisibleAction));
        }
        if (this.onPageNotVisibleAction != null && context.supportsPdfVersion(PdfVersion.VERSION_1_5)) {
            dictionary.put(PdfNameConstants.P_I, helper.getElement(this.onPageNotVisibleAction));
        }
    }
