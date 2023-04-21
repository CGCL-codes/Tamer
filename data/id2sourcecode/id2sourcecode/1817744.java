    @SuppressWarnings("unchecked")
    public Object transfereBeans(PlcBaseContextEntity context, PlcActionMapping plcMapping, HttpServletRequest request, java.lang.Object destino, java.lang.Object origem, String sufixoDet) throws PlcException {
        Logger log = Logger.getLogger("transferebeans");
        Logger logCc = Logger.getLogger("transferebeansCc");
        log.debug("########## Entrou no transfereBeans");
        Method metodo = null;
        Class[] parametros = null;
        Object retObj = null;
        Class voClass = null;
        BeanInfo info = null;
        PropertyDescriptor[] pd = null;
        DynaProperty[] dynaProperty = null;
        String nomePropForm = "";
        try {
            if (destino instanceof DynaActionForm || destino instanceof DynaValidatorActionForm) {
                log.debug("###########TransfereBeans: ORIGEM vo ==> DESTINO form");
                dynaProperty = ((DynaActionForm) destino).getDynaClass().getDynaProperties();
                voClass = origem.getClass();
                info = Introspector.getBeanInfo(voClass);
                pd = info.getPropertyDescriptors();
                PlcBaseEntity voOrigem = (PlcBaseEntity) origem;
                for (int i = 0; i < pd.length; i++) {
                    PlcPrimaryKey annotation = (PlcPrimaryKey) voClass.getAnnotation(PlcPrimaryKey.class);
                    if ((plcMapping != null && plcMapping.getClassePrimaryKey() != null && !plcMapping.getClassePrimaryKey().equals("") && !plcMapping.getClassePrimaryKey().equals((String) config.get(plcMapping, PlcConstantsCommons.ENTITY_ROOT)) && pd[i].getName().equals("idComposto")) || (annotation != null && !annotation.classe().isAssignableFrom(voClass.getClass()) && pd[i].getName().equals("idComposto"))) {
                        logCc.debug("Entrou para copiar chave composta para o form-bean");
                        metodo = pd[i].getReadMethod();
                        parametros = metodo.getParameterTypes();
                        retObj = metodo.invoke(origem, (Object[]) parametros);
                        transfereBeans(context, plcMapping, request, destino, retObj, "");
                        continue;
                    }
                    for (int k = 0; k < dynaProperty.length; k++) {
                        nomePropForm = dynaProperty[k].getName();
                        if (log.isDebugEnabled()) log.debug("mapping=" + plcMapping + " prop" + nomePropForm);
                        if ((plcMapping == null || plcMapping.getDesprezaTransfereBeans().indexOf("," + nomePropForm + ",") == -1) && PlcAnnotationHelper.getInstance().isCopyable(pd[i])) {
                            if ((pd[i].getName() + sufixoDet).equals(nomePropForm) && !(Set.class.isAssignableFrom(pd[i].getPropertyType()))) {
                                transfereUmBeanVOParaForm(context, plcMapping, voOrigem, (DynaActionForm) destino, pd[i], sufixoDet, nomePropForm);
                            }
                        }
                    }
                }
                if (plcMapping.getComponentes() != null) transfereBeansComponenteDescendenteVOParaForm(context, plcMapping, plcMapping.getComponentes(), origem, ((DynaActionForm) destino));
            } else {
                log.debug("############ TransfereBeans: ORIGEM form ==> DESTINO vo  ");
                dynaProperty = ((DynaActionForm) origem).getDynaClass().getDynaProperties();
                voClass = destino.getClass();
                info = Introspector.getBeanInfo(voClass);
                pd = info.getPropertyDescriptors();
                for (int i = 0; i < dynaProperty.length; i++) {
                    for (int k = 0; k < pd.length; k++) {
                        if ((plcMapping == null || plcMapping.getDesprezaTransfereBeans().indexOf("," + pd[k].getName() + ",") == -1) && !PlcAnnotationHelper.getInstance().existsAnnotationToComponent(pd[k].getPropertyType())) {
                            if (dynaProperty[i].getName().equals(pd[k].getName())) {
                                log.debug(" transfereBeans: atributos iguais : form = " + dynaProperty[i].getName() + " vo = " + pd[k].getName());
                                if (transfereBeansFormParaVOEspecificoApi(context, plcMapping, request, (DynaActionForm) origem, dynaProperty[i], (PlcBaseEntity) destino, pd[k])) continue;
                                Object valorInformado = ((DynaActionForm) origem).get(dynaProperty[i].getName());
                                Object prop = null;
                                if (PropertyUtils.isReadable(destino, dynaProperty[i].getName())) prop = PropertyUtils.getProperty(destino, dynaProperty[i].getName());
                                if (!destino.getClass().getName().endsWith("KeyVO")) {
                                    boolean isAuxliar = dynaProperty[i].getName().endsWith("Str") || dynaProperty[i].getName().endsWith("Aux");
                                    boolean existeAux = ((DynaActionForm) origem).getMap().containsKey(dynaProperty[i].getName() + "Str") || ((DynaActionForm) origem).getMap().containsKey(dynaProperty[i].getName() + "Aux");
                                    String modoGravacao = (String) ((DynaActionForm) origem).get(PlcConstantsCommons.MODES.MODE);
                                    PlcBaseEntity voAnterior = montaVOAnterior(modoGravacao, request, voClass.getName());
                                    if (isAuxliar) {
                                        String propSemAuxliar = dynaProperty[i].getName().substring(0, dynaProperty[i].getName().length() - 3);
                                        String valorAtualSemAuxiliar = (String) (((DynaActionForm) origem).getMap().get(propSemAuxliar));
                                        if (voAnterior != null) {
                                            Object valorVoAnterior = null;
                                            try {
                                                valorVoAnterior = PropertyUtils.getProperty(voAnterior, propSemAuxliar);
                                            } catch (NoSuchMethodException e) {
                                            }
                                            Object valorVoAnteriorAux;
                                            if (valorVoAnterior != null && PlcBaseEntity.class.isAssignableFrom(valorVoAnterior.getClass())) valorVoAnteriorAux = ((PlcBaseEntity) valorVoAnterior).getIdAux(); else valorVoAnteriorAux = valorVoAnterior == null ? null : valorVoAnterior;
                                            Object valorAuxiliarVoAnterior = PropertyUtils.getProperty(voAnterior, dynaProperty[i].getName());
                                            String valorAuxiliarVoAnteriorStr;
                                            if (valorAuxiliarVoAnterior != null && PlcBaseEntity.class.isAssignableFrom(valorAuxiliarVoAnterior.getClass())) valorAuxiliarVoAnteriorStr = ((PlcBaseEntity) valorAuxiliarVoAnterior).getIdAux(); else valorAuxiliarVoAnteriorStr = valorAuxiliarVoAnterior == null ? "" : valorAuxiliarVoAnterior.toString();
                                            if (valorVoAnteriorAux != null && BigDecimal.class.isAssignableFrom(valorVoAnteriorAux.getClass())) {
                                                if ((valorVoAnteriorAux.toString().equals(valorAtualSemAuxiliar)) || (valorAuxiliarVoAnteriorStr.equals(valorInformado))) continue;
                                            } else if (valorVoAnteriorAux != null && Date.class.isAssignableFrom(valorVoAnteriorAux.getClass())) {
                                                Date data = null;
                                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                                formatter.setLenient(false);
                                                if (StringUtils.isNotBlank(valorAtualSemAuxiliar)) {
                                                    data = formatter.parse(valorAtualSemAuxiliar);
                                                }
                                                boolean isDiferente = data != null ? (!formatter.format(valorVoAnterior).equals(formatter.format(data))) : true;
                                                if (isDiferente || (valorAuxiliarVoAnteriorStr.equals(valorInformado))) continue;
                                            } else if ((valorVoAnteriorAux != null && valorVoAnteriorAux.toString().equals(valorAtualSemAuxiliar)) || (valorAuxiliarVoAnteriorStr != null && valorAuxiliarVoAnteriorStr.equals(valorInformado))) continue; else if (StringUtils.isNotBlank(valorAtualSemAuxiliar) && (valorAuxiliarVoAnterior == null || valorAuxiliarVoAnterior.equals(valorInformado))) continue;
                                        } else if (StringUtils.isNotBlank(valorAtualSemAuxiliar) && StringUtils.isBlank((String) valorInformado)) continue;
                                    } else if (existeAux) {
                                        String valorAux = (String) ((DynaActionForm) origem).getMap().get(dynaProperty[i].getName() + "Str");
                                        String valorNormal = (String) ((DynaActionForm) origem).getMap().get(dynaProperty[i].getName());
                                        if (voAnterior != null) {
                                            String valorAuxAnt = null;
                                            try {
                                                valorAuxAnt = (String) PropertyUtils.getProperty(voAnterior, dynaProperty[i].getName() + "Str");
                                            } catch (NoSuchMethodException e) {
                                            }
                                            Object valorNormalAnt = PropertyUtils.getProperty(voAnterior, dynaProperty[i].getName());
                                            if (StringUtils.isBlank(valorAuxAnt) && valorNormalAnt == null) {
                                                if (StringUtils.isNotBlank(valorAux)) continue;
                                            } else {
                                                if (StringUtils.isNotBlank(valorAux) && (valorAuxAnt == null || !valorAuxAnt.equals(valorAux))) continue; else if (valorInformado != null && StringUtils.isBlank(valorAux)) continue;
                                            }
                                        } else if (StringUtils.isNotBlank(valorAux)) continue;
                                    }
                                } else {
                                    String valorAux = (String) ((DynaActionForm) origem).getMap().get(dynaProperty[i].getName() + "Str");
                                    if (StringUtils.isNotBlank(valorAux)) continue;
                                }
                                PlcPrimaryKey annotation = null;
                                PlcAggregatePropLocator agregadoPropLocator = PlcAggregatePropLocator.getInstance();
                                Class classPropDestino = PropertyUtils.getPropertyType(destino, dynaProperty[i].getName());
                                if (PlcBaseEntity.class.isAssignableFrom(classPropDestino)) {
                                    PlcBaseEntity objClasse = agregadoPropLocator.getAggregatedClassObject(classPropDestino);
                                    if (objClasse != null) annotation = (PlcPrimaryKey) objClasse.getClass().getAnnotation(PlcPrimaryKey.class);
                                }
                                if (annotation == null && prop == null && (valorInformado == null || StringUtils.isBlank(valorInformado.toString())) && PlcBaseEntity.class.isAssignableFrom(classPropDestino)) continue;
                                if (prop != null && PlcBaseEntity.class.isAssignableFrom(prop.getClass()) && ((PlcBaseEntity) prop).getId() != null && ((PlcBaseEntity) prop).getIdAux().equals((String) valorInformado)) continue;
                                transfereUmBeanDoFormParaVO(pd[k], valorInformado, (DynaActionForm) origem, destino, nomePropForm);
                            }
                        }
                    }
                }
                if (plcMapping != null && plcMapping.getComponentes() != null) transfereBeansComponenteDescendenteFormParaVO(context, destino, plcMapping.getComponentes(), ((DynaActionForm) origem), nomePropForm);
                PlcPrimaryKey annotation = (PlcPrimaryKey) voClass.getAnnotation(PlcPrimaryKey.class);
                if ((plcMapping != null && plcMapping.getClassePrimaryKey() != null && !plcMapping.getClassePrimaryKey().equals("") && !plcMapping.getClassePrimaryKey().equals((String) config.get(plcMapping, PlcConstantsCommons.ENTITY_ROOT))) || (annotation != null && !annotation.classe().isAssignableFrom(voClass.getClass()))) {
                    log.debug("Entrou para copiar do form-bean para chave composta");
                    Object chaveNatural = null;
                    if (StringUtils.isNotBlank(plcMapping.getClassePrimaryKey())) {
                        chaveNatural = Class.forName(plcMapping.getClassePrimaryKey()).newInstance();
                    } else if (annotation != null) {
                        chaveNatural = annotation.classe().newInstance();
                    }
                    transfereBeans(context, null, request, chaveNatural, origem, "");
                    return chaveNatural;
                }
            }
            if (log.isDebugEnabled()) log.debug("NO VAI RETORNAR CHAVE NATURAL");
            return null;
        } catch (Exception ex) {
            throw new PlcException("jcompany.erros.transfereBeans", new Object[] { ex }, ex, log);
        }
    }
