    private static HashMap evaluateTemplate(String template_name, HashMap container, ComplexTypeConverter typeConverter, ParamHashMapBuilder builder) throws TypeConversionException, InstantiationException, IllegalAccessException, DigesterException {
        log.debug("evaluating template : " + template_name);
        TemplateType templateParamInst = (TemplateType) builder.getDigester().getTemplatesHashMap().get(template_name);
        VelocityStandardEngine vengine = new VelocityStandardEngine();
        StringWriter sw = new StringWriter();
        VelocityContext vcontext = (VelocityContext) container.get("$%$VELOCITY$%$CONTEXT::" + template_name);
        if (templateParamInst.getFromFile() != null && templateParamInst.getFromFile().trim().length() > 0) {
            FileFinder ff = new FileFinder();
            InputStream is = null;
            try {
                is = ff.getInputStream((templateParamInst.getFromFile()));
            } catch (FileNotFoundException e) {
                throw new TypeConversionException(e);
            }
            InputStreamReader isreader = new InputStreamReader(is);
            StringWriter writer = new StringWriter();
            int c;
            try {
                while ((c = isreader.read()) != -1) writer.write(c);
            } catch (IOException e) {
                throw new TypeConversionException(e);
            }
            templateParamInst.setContent(writer.getBuffer().toString());
        }
        String template_content = templateParamInst.getContent();
        try {
            vengine.applyTemplate(vcontext, sw, template_content);
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fava = sw.toString();
        templateParamInst.setContent(sw.toString());
        if (templateParamInst.getJice() != null) {
            log.debug("template has a jice element, refitting to template values");
            Document doc = null;
            try {
                doc = JDOMDocumentBuilder.build(sw.toString());
            } catch (JDOMException e) {
                throw new TypeConversionException(e);
            } catch (IOException e) {
                throw new TypeConversionException(e);
            }
            templateParamInst.setJice(doc.getRootElement());
        }
        Object value = typeConverter._convertType(templateParamInst);
        container.put(template_name, value);
        return container;
    }
