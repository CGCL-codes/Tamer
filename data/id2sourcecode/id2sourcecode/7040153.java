    @Override
    public void writeAttributes(XMLWriter writer) {
        super.writeAttributes(writer);
        writer.addAttribute(FormField.LABEL_TEXT, labelText);
        writer.addAttribute(FormField.NAME, getName());
        writer.addAttribute(FormField.TARGET_ERROR_ID, targetErrorId);
        if (tabIndex != -1) {
            writer.addAttribute(FormField.TAB_INDEX, tabIndex);
        }
        writer.addAttribute(FormField.INVALID_TEXT, invalidText);
        writer.addAttribute(FormField.LABEL_SEPARATOR, labelSeparator);
        if (validationDelay > 0) {
            writer.addAttribute(FormField.VALIDATION_DELAY, validationDelay);
        }
        writer.addAttribute(FormField.VALIDATION_EVENT, validationEvent);
        if (readonly) {
            writer.addAttribute(FormField.READONLY, readonly);
        }
        if (disabled) {
            writer.addAttribute(FormField.DISABLED, disabled);
        }
    }
