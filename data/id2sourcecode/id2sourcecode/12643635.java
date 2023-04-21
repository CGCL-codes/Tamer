    @Override
    protected void renderAllAttributes(FacesContext context, RenderingContext rc, FacesBean bean, boolean renderStyleAttrs) throws IOException {
        super.renderAllAttributes(context, rc, bean, false);
        ResponseWriter rw = context.getResponseWriter();
        boolean isTextArea = isTextArea(bean);
        Object columns = getColumns(bean);
        if (columns == null) {
            columns = getDefaultColumns(rc, bean);
        } else {
            if (columns instanceof Number) {
                int intCol = ((Number) columns).intValue();
                if (intCol > _MAX_COLUMNS) {
                    intCol = _MAX_COLUMNS;
                    columns = intCol;
                }
            }
        }
        rw.writeAttribute(isTextArea ? XhtmlConstants.COLS_ATTRIBUTE : XhtmlConstants.SIZE_ATTRIBUTE, columns, "columns");
        if (isTextArea) {
            Object rows = getRows(bean);
            if (rows == null) rows = getDefaultRows(); else {
                if (rows instanceof Number) {
                    int intRow = ((Number) rows).intValue();
                    if (intRow > _MAX_ROWS) {
                        rows = _MAX_ROWS;
                    }
                }
            }
            rw.writeAttribute("rows", rows, "rows");
            rw.writeAttribute("wrap", getWrap(bean), "wrap");
            if ((getReadOnly(context, bean) || !supportsEditing(rc)) && supportsReadonlyFormElements(rc)) rw.writeAttribute("readonly", Boolean.TRUE, "readOnly");
        } else {
            if (supportsAutoCompleteFormElements(rc)) {
                String autocomplete = getAutoComplete(bean);
                if (autocomplete.equalsIgnoreCase(CoreInputText.AUTO_COMPLETE_OFF)) {
                    rw.writeAttribute("autocomplete", "off", "autoComplete");
                }
            }
            Number maximumLength = getMaximumLength(bean);
            if (maximumLength != null && maximumLength.intValue() > 0) rw.writeAttribute("maxlength", maximumLength, "maximumLength");
        }
    }
