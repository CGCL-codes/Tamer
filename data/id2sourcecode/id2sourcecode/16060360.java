    @Override
    @SuppressWarnings("unchecked")
    protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {
        if (logVisao.isDebugEnabled()) logVisao.debug(PlcAopProfilingHelper.getInstance().showLogInitial("(Trinidad HTML)" + this.getClass().getSimpleName() + ":encodeAll"));
        TreeMap<String, Object> propsChaveNatural = (TreeMap<String, Object>) bean.getProperty(PlcVinculado.PROPS_CHAVE_NATURAL_PLC);
        String autoRecuperacaoPropriedade = (String) bean.getProperty(PlcVinculado.AUTO_RECUPERACAO_PROPRIEDADE_KEY);
        String detalhe = PlcComponentHelper.getInstance().getNomeDetalheCorrente();
        String propriedade = (String) bean.getProperty(PlcVinculado.PROPRIEDADE_KEY);
        String idTamanho = (String) bean.getProperty(PlcVinculado.ID_TAMANHO_KEY);
        StringBuilder objetoIndexadoSeguranca = new StringBuilder();
        objetoIndexadoSeguranca.append(detalhe).append(".").append(propriedade);
        HttpServletRequest request = PlcContextHelper.getInstance().getRequest();
        Map<String, Boolean> m = (Map<String, Boolean>) request.getAttribute(PlcConstants.GUI.MAP_SECURITY);
        if ((m != null && m.containsKey(objetoIndexadoSeguranca.toString()))) {
            super.encodeAll(context, arc, component, bean);
            bean.setProperty(UIXComponentBase.RENDERED_KEY, false);
        } else {
            ResponseWriter writer = context.getResponseWriter();
            PlcContextHelper helper = PlcContextHelper.getInstance();
            writer.startElement("span", component);
            String styleClass = (String) bean.getProperty(CoreInputText.STYLE_CLASS_KEY);
            if (styleClass != null) writer.writeAttribute("class", bean.getProperty(CoreInputText.STYLE_CLASS_KEY).toString(), null);
            if (bean.getProperty(CoreInputText.INLINE_STYLE_KEY) != null) writer.writeAttribute("style", bean.getProperty(CoreInputText.INLINE_STYLE_KEY).toString(), null);
            writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
            String obrigatorio = (String) bean.getProperty(PlcVinculado.OBRIGATORIO_KEY);
            if (obrigatorio != null) {
                if ("S".equals(obrigatorio)) {
                    writer.writeAttribute("class", (styleClass != null ? styleClass + " " : "") + "p_AFRequired", null);
                }
            }
            String idSomenteLeitura = (String) bean.getProperty(PlcVinculado.ID_SOMENTE_LEITURA_KEY);
            String idExibe = (String) bean.getProperty(PlcVinculado.ID_EXIBE_KEY);
            if (StringUtils.isNotBlank(autoRecuperacaoPropriedade)) {
                writer.startElement("input", component);
                writer.writeAttribute("type", "hidden", null);
                writer.writeAttribute("id", component.getClientId(context), null);
                writer.writeAttribute("name", component.getClientId(context), null);
                writer.writeAttribute("value", bean.getProperty(PlcVinculado.VALUE_KEY), null);
                writer.writeAttribute("title", bean.getProperty(PlcVinculado.AJUDA_KEY), null);
                writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
                writer.writeAttribute("size", "5", null);
                writer.endElement("input");
                writer.startElement("input", component);
                writer.writeAttribute("type", "text", null);
                writer.writeAttribute("id", "inibeFoco_" + component.getClientId(context) + ":" + autoRecuperacaoPropriedade, null);
                writer.writeAttribute("name", "inibeFoco_" + component.getClientId(context) + ":" + autoRecuperacaoPropriedade, null);
                writer.writeAttribute("value", bean.getProperty(PlcVinculado.AUTO_RECUPERACAO_PROPRIEDADE_VALUE), null);
                writer.writeAttribute("title", bean.getProperty(PlcVinculado.AJUDA_KEY), null);
                writer.writeAttribute("onblur", "autoRecuperacaoVinculado('" + component.getClientId(context) + "', \"" + "inibeFoco_" + component.getClientId(context) + ":" + autoRecuperacaoPropriedade + "\");return false;", null);
                writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
                writer.writeAttribute("disabled", PlcTagHelper.verificaChaveNaturalPreenchido(bean, propriedade), null);
                writer.writeAttribute("size", idTamanho, null);
                writer.endElement("input");
            } else if (propsChaveNatural == null || propsChaveNatural.keySet().isEmpty()) {
                writer.startElement("input", component);
                writer.writeAttribute("type", idExibe.equalsIgnoreCase("S") ? "text" : "hidden", null);
                writer.writeAttribute("id", component.getClientId(context), null);
                writer.writeAttribute("name", component.getClientId(context), null);
                writer.writeAttribute("value", bean.getProperty(PlcVinculado.VALUE_KEY), null);
                writer.writeAttribute("title", bean.getProperty(PlcVinculado.AJUDA_KEY), null);
                writer.writeAttribute("onblur", "autoRecuperacaoVinculado('" + component.getClientId(context) + "', null);return false;", null);
                writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
                writer.writeAttribute("disabled", PlcTagHelper.verificaChaveNaturalPreenchido(bean, propriedade), null);
                if (idSomenteLeitura.equals("S")) writer.writeAttribute("readonly", "true", null);
                writer.writeAttribute("size", idTamanho, null);
                writer.endElement("input");
            } else {
                boolean limparValor = StringUtils.isBlank((String) bean.getProperty(PlcVinculado.LOOKUP_VALUE_KEY));
                boolean firstField = true;
                for (Iterator i = propsChaveNatural.keySet().iterator(); i.hasNext(); ) {
                    String propriedadeCN = (String) i.next();
                    writer.startElement("input", component);
                    writer.writeAttribute("type", idExibe.equalsIgnoreCase("S") ? "text" : "hidden", null);
                    writer.writeAttribute("id", firstField == true ? component.getClientId(context) : component.getClientId(context) + propriedadeCN, null);
                    writer.writeAttribute("name", firstField == true ? component.getClientId(context) : component.getClientId(context) + propriedadeCN, null);
                    writer.writeAttribute("value", limparValor == false ? propsChaveNatural.get(propriedadeCN) : "", null);
                    writer.writeAttribute("title", bean.getProperty(PlcVinculado.AJUDA_KEY), null);
                    writer.writeAttribute("disabled", PlcTagHelper.verificaChaveNaturalPreenchido(bean, propriedade), null);
                    writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
                    if (!i.hasNext()) writer.writeAttribute("onblur", "autoRecuperacaoVinculado('" + component.getClientId(context) + "');return false;", null);
                    if (idSomenteLeitura.equals("S")) writer.writeAttribute("readonly", "false", null);
                    writer.writeAttribute("size", idTamanho, null);
                    writer.endElement("input");
                    firstField = false;
                }
            }
            writer.startElement("input", component);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("id", "lookup_" + component.getClientId(context), null);
            writer.writeAttribute("name", "lookup_" + component.getClientId(context), null);
            Object valorLookup = bean.getProperty(PlcVinculado.LOOKUP_VALUE_KEY);
            writer.writeAttribute("value", valorLookup, null);
            writer.writeAttribute("title", bean.getProperty(PlcVinculado.AJUDA_KEY), null);
            writer.writeAttribute("readonly", "false", null);
            writer.writeAttribute("size", bean.getProperty(PlcVinculado.LOOKUP_TAMANHO_KEY), null);
            writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
            writer.endElement("input");
            if (!("S".equals(request.getAttribute(PlcConstants.VISUALIZE_DOCUMENT_PLC) + ""))) {
                writer.startElement("span", component);
                writer.writeAttribute("id", component.getClientId(context) + "Sel", null);
                String contextPath = helper.getRequest().getContextPath();
                String baseAction = bean.getProperty(PlcVinculado.BASE_ACTION_KEY) == null ? "" : bean.getProperty(PlcVinculado.BASE_ACTION_KEY).toString();
                String action = bean.getProperty(PlcVinculado.ACTION_SEL_KEY) == null ? "" : bean.getProperty(PlcVinculado.ACTION_SEL_KEY).toString();
                if (StringUtils.isNotBlank(baseAction)) action = baseAction + "/" + action;
                String evento = bean.getProperty(PlcVinculado.EVENTO_KEY) == null ? "" : bean.getProperty(PlcVinculado.EVENTO_KEY).toString();
                String parametros = bean.getProperty(PlcVinculado.PARAMETRO_KEY) == null ? "" : bean.getProperty(PlcVinculado.PARAMETRO_KEY).toString();
                String separador = "";
                String larg = bean.getProperty(PlcVinculado.LARG_KEY) == null ? "0" : bean.getProperty(PlcVinculado.LARG_KEY).toString();
                String alt = bean.getProperty(PlcVinculado.ALT_KEY) == null ? "0" : bean.getProperty(PlcVinculado.ALT_KEY).toString();
                String posx = bean.getProperty(PlcVinculado.POSX_KEY) == null ? "0" : bean.getProperty(PlcVinculado.POSX_KEY).toString();
                String posy = bean.getProperty(PlcVinculado.POSY_KEY) == null ? "0" : bean.getProperty(PlcVinculado.POSY_KEY).toString();
                String propSelPop = bean.getProperty(PlcVinculado.PROPSEL_POP_KEY) == null ? "" : bean.getProperty(PlcVinculado.PROPSEL_POP_KEY).toString();
                String propsSelPop = bean.getProperty(PlcVinculado.PROPSSEL_POP_KEY) == null ? "" : bean.getProperty(PlcVinculado.PROPSSEL_POP_KEY).toString();
                String limpaPropsSelPop = bean.getProperty(PlcVinculado.LIMPA_PROPSSEL_POP_KEY) == null ? "" : bean.getProperty(PlcVinculado.LIMPA_PROPSSEL_POP_KEY).toString();
                String tituloBotaoSelPop = bean.getProperty(PlcVinculado.TITULO_BOTAO_SEL_POP_KEY) == null ? "" : bean.getProperty(PlcVinculado.TITULO_BOTAO_SEL_POP_KEY).toString();
                String tituloBotaoLimpar = bean.getProperty(PlcVinculado.TITULO_BOTAO_LIMPAR_KEY) == null ? "" : bean.getProperty(PlcVinculado.TITULO_BOTAO_LIMPAR_KEY).toString();
                if (propsSelPop != null && !propsSelPop.startsWith(",")) propsSelPop = ",".concat(propsSelPop);
                String propAutoRecuperacao = "";
                if (StringUtils.isNotBlank(autoRecuperacaoPropriedade)) propAutoRecuperacao = "," + "inibeFoco_" + component.getClientId(context) + ":" + autoRecuperacaoPropriedade + "#" + autoRecuperacaoPropriedade;
                String propriedades = "";
                String propriedadePrincipal = "";
                if (propsChaveNatural == null || propsChaveNatural.keySet().isEmpty()) {
                    propriedadePrincipal = component.getClientId(context) + "#" + propSelPop + ",lookup_" + component.getClientId(context) + "#" + propSelPop + "Lookup";
                    propriedades = "'" + propriedadePrincipal + propsSelPop + propAutoRecuperacao + "'";
                } else {
                    String props = "";
                    boolean firstField = true;
                    for (String propriedadeCN : propsChaveNatural.keySet()) {
                        if (firstField) {
                            props = component.getClientId(context) + "#" + propSelPop;
                        } else {
                            props += "," + component.getClientId(context) + propriedadeCN + "#" + propriedadeCN;
                        }
                        firstField = false;
                    }
                    propriedadePrincipal = props + ",lookup_" + component.getClientId(context) + "#" + propSelPop + "Lookup";
                    propriedades = "'" + propriedadePrincipal + propsSelPop + "'";
                }
                if (StringUtils.isNotBlank(evento)) {
                    parametros = "evento=" + evento + parametros;
                }
                String alvo = action.replaceAll("/", "_");
                String onclick = "selecaoPopup('" + contextPath + "/f/t/" + action + "?" + parametros + "&modoJanelaPlc=popup'," + (!"".equals(propriedades) ? propriedades + "," : "") + "'" + separador + "','" + larg + "','" + alt + "','" + posx + "','" + posy + "','" + alvo + "'" + "); return false;";
                writer.writeAttribute("onclick", onclick, null);
                writer.writeAttribute("class", "bt", null);
                writer.writeAttribute("onkeydown", "selecionaPorTecla(event,this);", null);
                writer.writeAttribute("title", tituloBotaoSelPop, null);
                writer.writeText(tituloBotaoSelPop, null);
                writer.endElement("span");
                String[] arrayPropriedades = ("S".equals(limpaPropsSelPop)) ? propriedades.replaceAll("'", "").split(",") : propriedadePrincipal.split(",");
                String campos = "";
                for (String prop : arrayPropriedades) {
                    String[] arrayPropriedade = prop.split("#");
                    if (campos == "") {
                        campos = arrayPropriedade[0];
                    } else {
                        campos += "," + arrayPropriedade[0];
                    }
                }
                boolean exibeBotaoLimpar = bean.getProperty(PlcVinculado.EXIBE_BOTAO_LIMPAR_KEY) == null ? false : Boolean.parseBoolean(bean.getProperty(PlcVinculado.EXIBE_BOTAO_LIMPAR_KEY).toString());
                if (exibeBotaoLimpar) {
                    writer.startElement("span", component);
                    writer.writeAttribute("id", component.getClientId(context) + "Limpar", null);
                    writer.writeAttribute("onclick", "limparVinculado(this, '" + campos + "')", null);
                    writer.writeAttribute("class", "bt", null);
                    if (!StringUtils.isNotBlank((String) valorLookup)) {
                        writer.writeAttribute("style", "display:none", null);
                    }
                    writer.writeAttribute("title", tituloBotaoLimpar, null);
                    writer.writeText(tituloBotaoLimpar, null);
                    writer.endElement("span");
                }
                boolean selecaoMultipla = false;
                if ((Boolean) bean.getProperty(PlcVinculado.EXIBE_BOTAO_MULTISEL) != null) selecaoMultipla = (Boolean) bean.getProperty(PlcVinculado.EXIBE_BOTAO_MULTISEL);
                if (selecaoMultipla) {
                    String tituloMulti = (String) bean.getProperty(PlcVinculado.BOTAO_MULTISEL_TITULO);
                    String onclickMulti = "selecaoPopupMulti('" + contextPath + "/f/t/" + action + "?" + parametros + "&modoJanelaPlc=popup&indMultiSelPlc=S'," + (!"".equals(propriedades) ? propriedades + "," : "") + "'" + separador + "','" + larg + "','" + alt + "','" + posx + "','" + posy + "','" + alvo + "'" + "); return false;";
                    writer.startElement("span", component);
                    writer.writeAttribute("id", component.getClientId(context) + "SelecaoMultipla", null);
                    writer.writeAttribute("onclick", onclickMulti, null);
                    writer.writeAttribute("class", "bt", null);
                    writer.writeAttribute("title", tituloMulti, null);
                    writer.writeText(tituloMulti, null);
                    writer.endElement("span");
                }
            }
            String autoRecuperacaoClasse = (String) bean.getProperty(PlcVinculado.AUTO_RECUPERACAO_CLASSE_KEY);
            if (StringUtils.isNotBlank(autoRecuperacaoClasse)) {
                writer.endElement("span");
            }
        }
        if (logVisao.isDebugEnabled()) logVisao.debug(PlcAopProfilingHelper.getInstance().showLogFinal("(Trinidad HTML)" + this.getClass().getSimpleName() + ":encodeAll"));
    }
