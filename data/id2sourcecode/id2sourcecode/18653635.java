    @Override
    protected void delegateRenderer(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean, CoreRenderer arg4) throws IOException {
        PlcFileEntity value = (PlcFileEntity) bean.getProperty(PlcFile.VALUE_ARQUIVO_KEY);
        if (value != null && value.getId() != null) {
            ResponseWriter rw = context.getResponseWriter();
            rw.startElement("span", component);
            String estilos = (String) bean.getProperty(CoreInputFile.STYLE_CLASS_KEY);
            if (!StringUtils.isBlank(estilos)) rw.writeAttribute("class", estilos, null); else rw.writeAttribute("class", "af_inputText", null);
            estilos = (String) bean.getProperty(CoreInputFile.INLINE_STYLE_KEY);
            if (!StringUtils.isBlank(estilos)) rw.writeAttribute("style", estilos, null); else rw.writeAttribute("style", "float: left;", null);
            rw.startElement("input", component);
            renderId(context, component);
            renderAllAttributes(context, arc, bean, false);
            rw.writeAttribute("type", "text", "type");
            rw.writeAttribute("value", value.getName(), null);
            rw.writeAttribute("readonly", true, null);
            rw.writeAttribute("name", value, "name");
            rw.endElement("input");
            rw.endElement("span");
        } else {
            ResponseWriter rw = context.getResponseWriter();
            rw.startElement("span", component);
            String estilos = (String) bean.getProperty(CoreInputFile.STYLE_CLASS_KEY);
            if (!StringUtils.isBlank(estilos)) rw.writeAttribute("class", estilos, null); else rw.writeAttribute("class", "af_inputText", null);
            estilos = (String) bean.getProperty(CoreInputFile.INLINE_STYLE_KEY);
            if (!StringUtils.isBlank(estilos)) rw.writeAttribute("style", estilos, null); else rw.writeAttribute("style", "float: left;", null);
            super.delegateRenderer(context, arc, component, bean, arg4);
            rw.endElement("span");
        }
    }
