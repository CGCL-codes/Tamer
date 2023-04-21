    public MimeMessageHelper populateEmailTemplate(EmailQueue emailQueue, MimeMessageHelper helper) throws EmailEngineException {
        Map<String, String> model = new HashMap<String, String>();
        Set<EmailField> emailFields = emailQueue.getEmailFields();
        for (EmailField field : emailFields) {
            model.put(field.getFieldName(), field.getFieldValue());
        }
        VelocityContext context = new VelocityContext(model);
        Template template = null;
        StringWriter contentSw = null;
        String templateType = emailQueue.getTemplateType();
        VelocityTemplate velocityTemplate = null;
        try {
            velocityTemplate = velocityTemplateManager.getTemplate(templateType);
        } catch (ObjectNotFoundException e1) {
            log.warn("Missing template : " + templateType);
        }
        try {
            log.debug("Loading template: " + templateType);
            template = engine.getTemplate(templateType);
            contentSw = new StringWriter();
            log.debug("Merging template.");
            template.merge(context, contentSw);
        } catch (ResourceNotFoundException rnfe) {
            log.warn("couldn't find the template: " + templateType, rnfe);
            throw new EmailEngineException("Unable to find specified template : " + templateType);
        } catch (ParseErrorException pee) {
            log.warn("Template and model don't match", pee);
            throw new EmailEngineException("Template and model don't match :" + pee.getMessage());
        } catch (MethodInvocationException mie) {
            log.warn("Method invocation error populating template.", mie);
            throw new EmailEngineException(mie.getMessage());
        } catch (Exception e) {
            log.warn("Generic error loading and populating email template", e);
            throw new EmailEngineException(e.getMessage());
        }
        try {
            helper.setTo(emailQueue.getTo());
            helper.setSubject(emailQueue.getSubject());
            try {
                if (model.containsKey("html") && model.get("html").equalsIgnoreCase("true")) {
                    Map<String, String> htmlModel = new HashMap<String, String>();
                    htmlModel.put("title", velocityTemplate.getTitle() != null ? velocityTemplate.getTitle() : "");
                    htmlModel.put("content", contentSw.toString());
                    if (velocityTemplate != null) {
                        String langCode = velocityTemplate.getTemplateType().substring(velocityTemplate.getTemplateType().length() - 2);
                        try {
                            htmlModel.put("promotion", generateHtmlForPromotions(langCode, velocityTemplate));
                        } catch (Exception e) {
                            log.error("Exception occured while generating promotion html for email", e);
                            htmlModel.put("promotion", "");
                        }
                    } else {
                        htmlModel.put("promotion", "");
                    }
                    htmlModel.put("footer", "");
                    VelocityContext htmlContext = new VelocityContext(htmlModel);
                    Template htmlTemplate = null;
                    StringWriter htmlSw = null;
                    try {
                        htmlTemplate = engine.getTemplate(Constants.BASE_EMAIL_HTML_TEMPLATE);
                        htmlSw = new StringWriter();
                        htmlTemplate.merge(htmlContext, htmlSw);
                        helper.setText(htmlSw.toString(), true);
                    } catch (ResourceNotFoundException rnfe) {
                        log.warn("couldn't find the template: " + Constants.BASE_EMAIL_HTML_TEMPLATE, rnfe);
                        throw new EmailEngineException("Unable to find specified template : " + Constants.BASE_EMAIL_HTML_TEMPLATE);
                    } catch (ParseErrorException pee) {
                        log.warn("Template and model don't match", pee);
                        throw new EmailEngineException("Template and model don't match :" + pee.getMessage());
                    } catch (MethodInvocationException mie) {
                        log.warn("Method invocation error populating template.", mie);
                        throw new EmailEngineException(mie.getMessage());
                    } catch (Exception e) {
                        log.warn("Generic error loading and populating email template", e);
                        throw new EmailEngineException(e.getMessage());
                    }
                } else {
                    String noHTMLString = contentSw.toString().replaceAll(config.get("regex1"), config.get("regex1token"));
                    noHTMLString = noHTMLString.replaceAll(config.get("regex2"), config.get("regex2token"));
                    noHTMLString = noHTMLString.replaceAll(config.get("regex3"), config.get("regex3token"));
                    noHTMLString = noHTMLString.replaceAll(config.get("regex4"), config.get("regex4token"));
                    noHTMLString = noHTMLString.replaceAll(config.get("regex5"), config.get("regex5token"));
                    int regexNumber = 5;
                    String regEX = null;
                    while (++regexNumber < 99 && (regEX = config.get("regex" + regexNumber)) != null) {
                        noHTMLString = noHTMLString.replaceAll(regEX, config.get("regex" + regexNumber + "token"));
                    }
                    noHTMLString = removeHtmlSpecialEntities(noHTMLString);
                    helper.setText(noHTMLString);
                    log.debug(noHTMLString);
                }
                if (model.containsKey("FORM")) helper.setBcc(EmailEngineConfiguration.getInstance().getConfigurationParameter(EmailEngineConfiguration.SMTP_EFORMS));
                if (model.containsKey("from")) {
                    helper.setFrom(model.get("from"));
                } else {
                    helper.setFrom(EmailEngineConfiguration.getInstance().getConfigurationParameter(EmailEngineConfiguration.EMAIL_SRC_ADR));
                }
            } catch (ConfigurationException e) {
                log.warn("Unable to retrieve configuration for email templater", e);
                throw new EmailEngineException("Unable to set From field due to ConfigurationException : " + e.getMessage());
            }
            log.debug("Begin processing attachments.");
            if (!emailQueue.getAttachments().isEmpty()) {
                Set<EmailAttachment> attachments = emailQueue.getAttachments();
                FileOutputStream fos = null;
                InputStream is;
                for (EmailAttachment att : attachments) {
                    log.debug("Processing attachment: " + att);
                    try {
                        is = att.getAttachmentContent().getBinaryStream();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        int read;
                        while ((read = is.read()) != (-1)) {
                            out.write(read);
                        }
                        is.close();
                        byte[] b = out.toByteArray();
                        File file = new File(att.getAttachmentName());
                        fos = new FileOutputStream(file);
                        fos.write(b);
                        fos.flush();
                        fos.close();
                        helper.addAttachment(att.getAttachmentName(), file);
                    } catch (SQLException e) {
                        log.error("Unable to retrieve binary stream for attachment " + att, e);
                        throw new EmailEngineException("Unable to retrieve binary stream for attachment");
                    } catch (IOException ioe) {
                        log.error("Unable to read/write the attachment file.", ioe);
                        throw new EmailEngineException("Unable to read/write the attachment file");
                    }
                }
            }
            helper.setSentDate(new Timestamp(new Date().getTime()));
        } catch (MessagingException e) {
            throw new EmailEngineException("Unable to populate email due to MessagingException : " + e.getMessage());
        }
        return helper;
    }
