    protected void updateBeanSet() {
        try {
            if (beanManager != null) {
                Object value = null;
                try {
                    if (this.isPasswordEnable()) {
                        if (this.getDigestAlgorithm() == null || this.getDigestAlgorithm().length() == 0) value = this.getLength() == 0 ? null : this.getPassword(); else value = this.getLength() == 0 ? null : Digest.digest(this.getPassword(), getDigestAlgorithm());
                    } else if (this.getDateFormat() != null) {
                        value = this.getDateFormat().parse(this.getText(0, this.getLength()));
                        Class fieldClass = beanManager.getCurrentBean().getFieldClass(fieldName);
                        if (!Date.class.isAssignableFrom(fieldClass)) {
                            if (fieldClass == int.class || fieldClass == Integer.class) value = Integer.parseInt(DateUtils.dateToString((Date) value, ((SimpleDateFormat) this.getDateFormat()).toPattern())); else if (fieldClass == String.class) value = DateUtils.dateToString((Date) value, ((SimpleDateFormat) this.getDateFormat()).toPattern());
                        }
                    } else if (this.getNumberFormat() != null) {
                        String str = this.getText(0, this.getLength());
                        value = !str.equals("") ? this.getNumberFormat().parse(str) : "0";
                    } else if (this.getMaskFormat() != null) value = this.getMaskFormat().parse(this.getText(0, this.getLength())); else value = this.getText(0, this.getLength());
                    if (fieldName != null) beanManager.getCurrentBean().setFieldValue(fieldName, value);
                } catch (ParseException ex) {
                    if (log.isDebugEnabled()) log.debug(ex.getMessage(), ex);
                }
            }
        } catch (BadLocationException badlocationexception) {
            if (log.isDebugEnabled()) log.debug(badlocationexception.getMessage(), badlocationexception);
        }
    }
