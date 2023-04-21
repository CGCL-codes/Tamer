    @Override
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
        Object defaultValue = this.getDefaultValue();
        boolean isReadOnly = this.getOnlyRead() != null ? this.getOnlyRead().booleanValue() : false;
        if (isReadOnly) {
            String value = null;
            if (defaultValue == null || StringUtils.blank(defaultValue.toString())) {
                value = getTranslatedFieldSupport().format(this, getValue());
            } else {
                if (StringUtils.isNumber(getValue())) {
                    if (checkNumberUseDefValue(this)) {
                        value = getTranslatedFieldSupport().format(this, this.getDefaultValue());
                    } else {
                        value = getTranslatedFieldSupport().format(this, getValue());
                    }
                } else {
                    if (getValue() != null && StringUtils.notBlank(getValue().toString())) {
                        value = getTranslatedFieldSupport().format(this, getValue());
                    } else {
                        value = getTranslatedFieldSupport().format(this, this.getDefaultValue());
                    }
                }
            }
            renderDelegatePrefix(writer, cycle);
            writer.beginEmpty("input");
            writer.attribute("type", isHidden() ? "password" : "text");
            writer.attribute("name", getName());
            if (isDisabled()) writer.attribute("disabled", "disabled");
            if (value != null) writer.attribute("value", value);
            writer.attribute("readonly", "true");
            renderIdAttribute(writer, cycle);
            renderDelegateAttributes(writer, cycle);
            getTranslatedFieldSupport().renderContributions(this, writer, cycle);
            getValidatableFieldSupport().renderContributions(this, writer, cycle);
            renderInformalParameters(writer, cycle);
            writer.closeTag();
            renderDelegateSuffix(writer, cycle);
        } else {
            if (defaultValue == null || StringUtils.blank(defaultValue.toString())) {
                super.renderFormComponent(writer, cycle);
            } else {
                String value = getTranslatedFieldSupport().format(this, getValue());
                if (StringUtils.isNumber(getValue()) || (this.getTranslator() instanceof NumTranslator)) {
                    if (checkNumberUseDefValue(this)) {
                        this.setValue(this.getDefaultValue());
                        super.renderFormComponent(writer, cycle);
                    } else {
                        super.renderFormComponent(writer, cycle);
                    }
                } else {
                    if (value != null && StringUtils.notBlank(value)) {
                        super.renderFormComponent(writer, cycle);
                    } else {
                        this.setValue(this.getDefaultValue());
                        super.renderFormComponent(writer, cycle);
                    }
                }
            }
        }
    }
