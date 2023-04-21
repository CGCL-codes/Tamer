    public ModelObject convert(ConverterContext context, Class<?> scribbleType, CDLType cdlType) {
        org.scribble.model.Block ret = new org.scribble.model.Block();
        org.pi4soa.cdl.Interaction cdl = (org.pi4soa.cdl.Interaction) cdlType;
        Role fromRole = null;
        Role toRole = null;
        if (cdl.getFromParticipant() != null) {
            fromRole = (Role) context.getState(XMLUtils.getLocalname(cdl.getFromParticipant().getName()));
            if (fromRole == null) {
                logger.severe("Failed to get 'from' role '" + XMLUtils.getLocalname(cdl.getFromParticipant().getName()) + "'");
            }
        } else if (cdl.getFromRoleType() != null) {
            ParticipantType ptype = PackageUtil.getParticipantForRoleType(cdl.getFromRoleType());
            fromRole = (Role) context.getState(XMLUtils.getLocalname(ptype.getName()));
            if (fromRole == null) {
                logger.severe("Failed to get 'from' role '" + XMLUtils.getLocalname(ptype.getName()) + "'");
            }
        }
        if (cdl.getToParticipant() != null) {
            toRole = (Role) context.getState(XMLUtils.getLocalname(cdl.getToParticipant().getName()));
            if (toRole == null) {
                logger.severe("Failed to get 'to' role '" + XMLUtils.getLocalname(cdl.getFromParticipant().getName()) + "'");
            }
        } else if (cdl.getToRoleType() != null) {
            ParticipantType ptype = PackageUtil.getParticipantForRoleType(cdl.getToRoleType());
            toRole = (Role) context.getState(XMLUtils.getLocalname(ptype.getName()));
            if (toRole == null) {
                logger.severe("Failed to get 'to' role '" + XMLUtils.getLocalname(ptype.getName()) + "'");
            }
        }
        Block block = ret;
        if (NamesUtil.isSet(cdl.getTimeToComplete())) {
            TryEscape te = new TryEscape();
            ret.getContents().add(te);
            block = te.getBlock();
            InterruptBlock interrupt = new InterruptBlock();
            te.getEscapeBlocks().add(interrupt);
            XPathExpression exp = new XPathExpression();
            exp.setQuery("cdl:hasDurationPassed('" + cdl.getTimeToComplete() + "')");
            interrupt.setExpression(exp);
            for (int i = 0; i < cdl.getRecordDetails().size(); i++) {
                org.pi4soa.cdl.RecordDetails rd = cdl.getRecordDetails().get(i);
                if (rd.getWhen() == WhenType.TIMEOUT) {
                    if (cdl.getTimeoutFromRoleTypeRecordDetails().contains(rd)) {
                        if (rd.getCauseException() != null && rd.getCauseException().trim().length() > 0) {
                            Raise raise = new Raise();
                            raise.getRoles().add(new Role(fromRole.getName()));
                            TypeReference tref = new TypeReference();
                            tref.setLocalpart(XMLUtils.getLocalname(rd.getCauseException()));
                            raise.setType(tref);
                            interrupt.getContents().add(raise);
                        }
                    }
                    if (cdl.getTimeoutToRoleTypeRecordDetails().contains(rd)) {
                        if (rd.getCauseException() != null && rd.getCauseException().trim().length() > 0) {
                            Raise raise = new Raise();
                            raise.getRoles().add(new Role(toRole.getName()));
                            TypeReference tref = new TypeReference();
                            tref.setLocalpart(XMLUtils.getLocalname(rd.getCauseException()));
                            raise.setType(tref);
                            interrupt.getContents().add(raise);
                        }
                    }
                }
            }
        }
        java.util.Iterator<ExchangeDetails> iter = cdl.getExchangeDetails().iterator();
        java.util.List<ConditionalBlock> cbs = new java.util.Vector<ConditionalBlock>();
        while (iter.hasNext()) {
            ExchangeDetails details = iter.next();
            ConditionalBlock cb = new ConditionalBlock();
            for (int i = 0; i < details.getSendRecordDetails().size(); i++) {
                RecordDetails rd = details.getSendRecordDetails().get(i);
                if (rd.getWhen() == WhenType.BEFORE) {
                    String fromRoleName = (details.getAction() == ExchangeActionType.REQUEST ? fromRole.getName() : toRole.getName());
                    Assignment assign = new Assignment();
                    assign.getSource().setComponentURI(CDLTypeUtil.getURIFragment(rd));
                    Role role = new Role(fromRoleName);
                    assign.getRoles().add(role);
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(rd.getTargetVariable().getName());
                    assign.setVariable(var);
                    org.scribble.expression.xpath.model.XPathExpression expr = new org.scribble.expression.xpath.model.XPathExpression();
                    if (rd.getSourceExpression() != null) {
                        expr.setQuery(rd.getSourceExpression());
                    } else if (rd.getSourceVariable() != null) {
                        expr.setQuery("cdl:getVariable('" + rd.getSourceVariable().getName() + "','','')");
                    }
                    assign.setExpression(expr);
                    cb.getContents().add(assign);
                    if (rd.getCauseException() != null && rd.getCauseException().trim().length() > 0) {
                        Raise raise = new Raise();
                        raise.getRoles().add(new Role(fromRoleName));
                        TypeReference tref = new TypeReference();
                        tref.setLocalpart(XMLUtils.getLocalname(rd.getCauseException()));
                        raise.setType(tref);
                        cb.getContents().add(raise);
                    }
                }
            }
            for (int i = 0; i < details.getReceiveRecordDetails().size(); i++) {
                RecordDetails rd = details.getReceiveRecordDetails().get(i);
                if (rd.getWhen() == WhenType.BEFORE) {
                    String toRoleName = (details.getAction() == ExchangeActionType.REQUEST ? toRole.getName() : fromRole.getName());
                    Assignment assign = new Assignment();
                    assign.getSource().setComponentURI(CDLTypeUtil.getURIFragment(rd));
                    Role role = new Role(toRoleName);
                    assign.getRoles().add(role);
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(rd.getTargetVariable().getName());
                    assign.setVariable(var);
                    org.scribble.expression.xpath.model.XPathExpression expr = new org.scribble.expression.xpath.model.XPathExpression();
                    if (rd.getSourceExpression() != null) {
                        expr.setQuery(rd.getSourceExpression());
                    } else if (rd.getSourceVariable() != null) {
                        expr.setQuery("cdl:getVariable('" + rd.getSourceVariable().getName() + "','','')");
                    }
                    assign.setExpression(expr);
                    cb.getContents().add(assign);
                    if (rd.getCauseException() != null && rd.getCauseException().trim().length() > 0) {
                        Raise raise = new Raise();
                        raise.getRoles().add(new Role(toRoleName));
                        TypeReference tref = new TypeReference();
                        tref.setLocalpart(XMLUtils.getLocalname(rd.getCauseException()));
                        raise.setType(tref);
                        cb.getContents().add(raise);
                    }
                }
            }
            ConversationInteraction interaction = new ConversationInteraction();
            interaction.getSource().setComponentURI(CDLTypeUtil.getURIFragment(details));
            Object ch = context.getState(cdl.getChannelVariable().getName());
            if (ch instanceof Channel) {
                interaction.setChannel((Channel) ch);
            }
            MessageSignature ms = new MessageSignature();
            ms.setOperation(cdl.getOperation());
            if (NamesUtil.isSet(details.getFaultName())) {
                ms.getAnnotations().put(FAULT_NAME, details.getFaultName());
            }
            interaction.setMessageSignature(ms);
            if (details.getType() instanceof InformationType) {
                InformationType itype = (InformationType) details.getType();
                TypeReference ref = ConverterUtil.getTypeReference(itype);
                ms.getTypes().add(ref);
                if (NamesUtil.isSet(itype.getTypeName())) {
                    String ns = CDLTypeUtil.getNamespace(itype.getTypeName(), cdl);
                    String lp = XMLUtils.getLocalname(itype.getTypeName());
                    if (lp != null) {
                        ms.getAnnotations().put(MESSAGE_TYPE_LOCALPART, lp);
                    }
                    if (ns != null) {
                        ms.getAnnotations().put(MESSAGE_TYPE_NAMESPACE, ns);
                    }
                } else if (NamesUtil.isSet(itype.getElementName())) {
                    String ns = CDLTypeUtil.getNamespace(itype.getElementName(), cdl);
                    String lp = XMLUtils.getLocalname(itype.getElementName());
                    if (lp != null) {
                        ms.getAnnotations().put(MESSAGE_TYPE_LOCALPART, lp);
                    }
                    if (ns != null) {
                        ms.getAnnotations().put(MESSAGE_TYPE_NAMESPACE, ns);
                    }
                }
            }
            cb.getContents().add(interaction);
            if (details.getAction() == ExchangeActionType.REQUEST) {
                interaction.setFromRole(fromRole);
                interaction.setToRole(toRole);
                if (details.getSendVariable() != null) {
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(details.getSendVariable().getName());
                    interaction.setFromVariable(var);
                }
                if (details.getReceiveVariable() != null) {
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(details.getReceiveVariable().getName());
                    interaction.setToVariable(var);
                }
                java.util.List<ExchangeDetails> resps = InteractionUtil.getResponseExchangeDetails(details);
                if (resps != null && resps.size() > 0) {
                    interaction.setRequestLabel(getLabel(details));
                }
                if (details.getSendCauseException() != null && details.getSendCauseException().trim().length() > 0) {
                    Raise raise = new Raise();
                    raise.getRoles().add(new Role(fromRole.getName()));
                    TypeReference tref = new TypeReference();
                    tref.setLocalpart(XMLUtils.getLocalname(details.getSendCauseException()));
                    raise.setType(tref);
                    cb.getContents().add(raise);
                }
                if (details.getReceiveCauseException() != null && details.getReceiveCauseException().trim().length() > 0) {
                    Raise raise = new Raise();
                    raise.getRoles().add(new Role(toRole.getName()));
                    TypeReference tref = new TypeReference();
                    tref.setLocalpart(XMLUtils.getLocalname(details.getReceiveCauseException()));
                    raise.setType(tref);
                    cb.getContents().add(raise);
                }
            } else {
                interaction.setFromRole(toRole);
                interaction.setToRole(fromRole);
                if (details.getReceiveVariable() != null) {
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(details.getReceiveVariable().getName());
                    interaction.setFromVariable(var);
                }
                if (details.getSendVariable() != null) {
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(details.getSendVariable().getName());
                    interaction.setToVariable(var);
                }
                if (details.getAction() == ExchangeActionType.RESPOND) {
                    interaction.setReplyToLabel(getLabel(details));
                }
                if (details.getSendCauseException() != null && details.getSendCauseException().trim().length() > 0) {
                    Raise raise = new Raise();
                    raise.getRoles().add(new Role(toRole.getName()));
                    TypeReference tref = new TypeReference();
                    tref.setLocalpart(XMLUtils.getLocalname(details.getSendCauseException()));
                    raise.setType(tref);
                    cb.getContents().add(raise);
                }
                if (details.getReceiveCauseException() != null && details.getReceiveCauseException().trim().length() > 0) {
                    Raise raise = new Raise();
                    raise.getRoles().add(new Role(fromRole.getName()));
                    TypeReference tref = new TypeReference();
                    tref.setLocalpart(XMLUtils.getLocalname(details.getReceiveCauseException()));
                    raise.setType(tref);
                    cb.getContents().add(raise);
                }
            }
            for (int i = 0; i < details.getSendRecordDetails().size(); i++) {
                RecordDetails rd = details.getSendRecordDetails().get(i);
                if (rd.getWhen() == WhenType.AFTER) {
                    String fromRoleName = (details.getAction() == ExchangeActionType.REQUEST ? fromRole.getName() : toRole.getName());
                    Assignment assign = new Assignment();
                    assign.getSource().setComponentURI(CDLTypeUtil.getURIFragment(rd));
                    Role role = new Role(fromRoleName);
                    assign.getRoles().add(role);
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(rd.getTargetVariable().getName());
                    assign.setVariable(var);
                    org.scribble.expression.xpath.model.XPathExpression expr = new org.scribble.expression.xpath.model.XPathExpression();
                    if (rd.getSourceExpression() != null) {
                        expr.setQuery(rd.getSourceExpression());
                    } else if (rd.getSourceVariable() != null) {
                        expr.setQuery("cdl:getVariable('" + rd.getSourceVariable().getName() + "','','')");
                    }
                    assign.setExpression(expr);
                    cb.getContents().add(assign);
                    if (rd.getCauseException() != null && rd.getCauseException().trim().length() > 0) {
                        Raise raise = new Raise();
                        raise.getRoles().add(new Role(fromRoleName));
                        TypeReference tref = new TypeReference();
                        tref.setLocalpart(XMLUtils.getLocalname(rd.getCauseException()));
                        raise.setType(tref);
                        cb.getContents().add(raise);
                    }
                }
            }
            for (int i = 0; i < details.getReceiveRecordDetails().size(); i++) {
                RecordDetails rd = details.getReceiveRecordDetails().get(i);
                if (rd.getWhen() == WhenType.AFTER) {
                    String toRoleName = (details.getAction() == ExchangeActionType.REQUEST ? toRole.getName() : fromRole.getName());
                    Assignment assign = new Assignment();
                    assign.getSource().setComponentURI(CDLTypeUtil.getURIFragment(rd));
                    Role role = new Role(toRoleName);
                    assign.getRoles().add(role);
                    org.scribble.conversation.model.Variable var = new org.scribble.conversation.model.Variable();
                    var.setName(rd.getTargetVariable().getName());
                    assign.setVariable(var);
                    if (rd.getTargetVariable().getType() instanceof ChannelType) {
                        Object state = context.getState(rd.getTargetVariable().getName());
                        if (state == null) {
                            Channel newch = new Channel();
                            newch.setName(rd.getTargetVariable().getName());
                            context.setState(newch.getName(), newch);
                        }
                    }
                    org.scribble.expression.xpath.model.XPathExpression expr = new org.scribble.expression.xpath.model.XPathExpression();
                    if (rd.getSourceExpression() != null) {
                        expr.setQuery(rd.getSourceExpression());
                    } else if (rd.getSourceVariable() != null) {
                        expr.setQuery("cdl:getVariable('" + rd.getSourceVariable().getName() + "','','')");
                    }
                    assign.setExpression(expr);
                    cb.getContents().add(assign);
                    if (rd.getCauseException() != null && rd.getCauseException().trim().length() > 0) {
                        Raise raise = new Raise();
                        raise.getRoles().add(new Role(toRoleName));
                        TypeReference tref = new TypeReference();
                        tref.setLocalpart(XMLUtils.getLocalname(rd.getCauseException()));
                        raise.setType(tref);
                        cb.getContents().add(raise);
                    }
                }
            }
            cbs.add(cb);
        }
        if (cbs.size() > 2) {
            block.getContents().addAll(cbs.remove(0).getContents());
            If choice = new If();
            choice.getRoles().add(new Role(toRole.getName()));
            for (int i = 0; i < cbs.size(); i++) {
                ConditionalBlock cb = cbs.get(i);
                choice.getConditionalBlocks().add(cb);
            }
            block.getContents().add(choice);
        } else {
            for (int i = 0; i < cbs.size(); i++) {
                ConditionalBlock cb = cbs.get(i);
                block.getContents().addAll(cb.getContents());
            }
        }
        return (ret);
    }
