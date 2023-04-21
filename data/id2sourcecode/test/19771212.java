    private void writeOutputField(Field field, Output outputAnnotation, Class<?> pageClass, Object pageObject, PrintWriter writer) {
        writer.println("<tr>");
        {
            String outputLabel = outputAnnotation.value();
            if (false == "".equals(outputLabel)) {
                new HtmlElement("th").addAttribute("align", "left").addAttribute("style", "background-color: #e0e0e0").setBody(outputLabel + ":").write(writer);
                writer.println("<td>");
            } else {
                writer.println("<td colspan=\"2\">");
            }
            String fieldName = field.getName();
            try {
                field.setAccessible(true);
                Object outputValue = field.get(pageObject);
                Render renderAnnotation = field.getAnnotation(Render.class);
                if (null != renderAnnotation) {
                    Class<? extends Renderer<?>> rendererClass = renderAnnotation.value();
                    Renderer renderer = rendererClass.newInstance();
                    renderer.renderOutput(fieldName, outputValue, writer);
                } else if (outputAnnotation.verbatim()) {
                    new HtmlElement("pre").setBody(outputValue.toString()).write(writer);
                    new HtmlElement("input").addAttribute("type", "hidden").addAttribute("name", fieldName).addAttribute("value", outputValue.toString()).write(writer);
                } else if (List.class.equals(field.getType())) {
                    List<?> outputList = (List<?>) outputValue;
                    outputTable(fieldName, outputList, pageClass, writer);
                } else {
                    Class<?> outputClass;
                    if (null != outputValue) {
                        outputClass = outputValue.getClass();
                    } else {
                        outputClass = field.getType();
                    }
                    if (hasOutputFields(outputClass)) {
                        writeOutputRecordField(field, outputValue, outputClass, writer);
                    } else {
                        if (null == outputValue) {
                            outputValue = "";
                        }
                        writer.println(escape(outputValue));
                        new HtmlElement("input").addAttribute("type", "hidden").addAttribute("name", fieldName).addAttribute("value", outputValue.toString()).write(writer);
                    }
                }
            } catch (Exception e) {
                LOG.debug("cannot read field " + fieldName + ": " + e.getMessage());
                writer.println("cannot read field: " + fieldName);
            }
            writer.println("</td>");
        }
        writer.println("</tr>");
    }
