    protected void encodeDisabledAttributesInput(BaseInputComponent aComponent, ResponseWriter aWriter) throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("readonly", "readonly", null);
        }
    }
