    public DefaultFieldMetadata(FormModel formModel, FormModelMediatingValueModel valueModel, String field, Class fieldType, FieldFace fieldFace, boolean readable, boolean writeable) {
        this.formModel = formModel;
        this.valueModel = valueModel;
        this.valueModel.addPropertyChangeListener(FormModelMediatingValueModel.DIRTY_PROPERTY, dirtyChangeHandler);
        this.field = field;
        this.fieldType = fieldType;
        this.fieldFace = fieldFace;
        this.readable = readable;
        this.writeable = writeable;
        this.formModel.addPropertyChangeListener(ENABLED_PROPERTY, formChangeHandler);
        this.oldReadOnly = isReadOnly();
        this.oldEnabled = isEnabled();
    }
