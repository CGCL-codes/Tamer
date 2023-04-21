    private void outputTable(String tableName, List<?> list, Class<?> pageClass, PrintWriter writer) {
        if (null == list) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        writer.println("<table style=\"border: 1 px;\">");
        Class<?> listEntryClass = list.get(0).getClass();
        writer.println("<input type=\"hidden\" name=\"" + tableName + ".type\" value=\"" + listEntryClass.getName() + "\"/>");
        if (hasFields(listEntryClass)) {
            writer.println("<tr>");
            {
                Field[] fields = listEntryClass.getDeclaredFields();
                for (Field field : fields) {
                    Output outputAnnotation = field.getAnnotation(Output.class);
                    String label;
                    if (null != outputAnnotation) {
                        label = outputAnnotation.value();
                    } else {
                        label = field.getName();
                    }
                    new HtmlElement("th").addAttribute("style", "background-color: #a0a0a0;").setBody(label).write(writer);
                }
            }
            writer.println("</tr>");
        }
        int size = list.size();
        for (int idx = 0; idx < size; idx++) {
            Object item = list.get(idx);
            writer.println("<tr>");
            {
                {
                    if (hasFields(listEntryClass)) {
                        writer.println("<input type=\"hidden\" name=\"" + tableName + "." + idx + "\" value=\"" + listEntryClass.getName() + "\" />");
                        Field[] fields = listEntryClass.getDeclaredFields();
                        for (Field field : fields) {
                            Object fieldValue;
                            try {
                                field.setAccessible(true);
                                fieldValue = field.get(item);
                            } catch (Exception e) {
                                writer.println("Could not read field: " + field.getName());
                                continue;
                            }
                            HtmlElement tdElement = new HtmlElement("td").setBody(fieldValue.toString());
                            if (0 != idx % 2) {
                                tdElement.addAttribute("style", "background-color: #e0e0e0;");
                            }
                            tdElement.write(writer);
                            new HtmlElement("input").addAttribute("type", "hidden").addAttribute("name", tableName + "." + idx + "." + field.getName()).addAttribute("value", fieldValue.toString()).write(writer);
                        }
                    } else {
                        new HtmlElement("td").setBody(item.toString()).write(writer);
                        new HtmlElement("input").addAttribute("type", "hidden").addAttribute("name", tableName + "." + idx).addAttribute("value", item.toString()).write(writer);
                    }
                }
            }
            Method[] methods = pageClass.getDeclaredMethods();
            for (Method method : methods) {
                Action actionAnnotation = method.getAnnotation(Action.class);
                if (null == actionAnnotation) {
                    continue;
                }
                if (1 != method.getParameterTypes().length) {
                    continue;
                }
                Class<?> methodParamType = method.getParameterTypes()[0];
                if (false == methodParamType.equals(listEntryClass)) {
                    continue;
                }
                String actionName = actionAnnotation.value();
                if ("".equals(actionName)) {
                    actionName = method.getName();
                }
                writer.println("<td>");
                {
                    new HtmlElement("input").addAttribute("type", "button").addAttribute("value", actionName).addAttribute("name", tableName + "." + method.getName() + "." + idx).addAttribute("onclick", "doAction('" + method.getName() + "(" + tableName + "." + idx + ")" + "')").write(writer);
                }
                writer.println("</td>");
            }
            writer.println("</tr>");
        }
        writer.println("</table>");
    }
