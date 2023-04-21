    @SuppressWarnings("unchecked")
    protected void writeObject(PrintWriter pw, String field, Object object, HashSet<Object> alreadyWritten, String beanPathPrefix) {
        String key = getBeanToNameMap().get(object);
        String close = "";
        String beanPath = beanPathPrefix;
        if (StringUtils.isNotBlank(field)) {
            pw.write("<tr><td align='right' valign='top'><b>");
            String closeAnchor = "";
            if (StringUtils.isNotBlank(beanPathPrefix)) {
                if (beanPathPrefix.endsWith(".")) {
                    beanPath += field;
                } else if (beanPathPrefix.endsWith("[")) {
                    beanPath += field + "]";
                }
                pw.println("<a href='../beans/" + beanPath + "'>");
                closeAnchor = "</a>";
            }
            pw.write(field);
            pw.write(closeAnchor);
            pw.write(":</b></td><td>");
            close = "</td></tr>";
        }
        if (object == null) {
            pw.write("<i>null</i><br/>");
            pw.write(close);
            return;
        }
        if (object instanceof String) {
            pw.write("\"" + object + "\"<br/>");
            pw.write(close);
            return;
        }
        if (BeanUtils.isSimpleValueType(object.getClass()) || object instanceof File) {
            pw.write(object + "<br/>");
            pw.write(close);
            return;
        }
        if (alreadyWritten.contains(object)) {
            pw.println("&uarr;<br/>");
            pw.write(close);
            return;
        }
        alreadyWritten.add(object);
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(field)) {
            pw.println("<a href='../beans/" + key + "'>" + key + "</a><br/>");
            pw.write(close);
            return;
        }
        pw.print("<fieldset style='display:inline;vertical-align:top'><legend>");
        pw.println(object.getClass().getName() + "</legend>");
        pw.println("<table>");
        BeanWrapperImpl bwrap = new BeanWrapperImpl(object);
        for (PropertyDescriptor pd : getPropertyDescriptors(bwrap)) {
            if (pd.getReadMethod() != null && !pd.isHidden()) {
                String propName = pd.getName();
                if (beanPath != null) {
                    beanPathPrefix = beanPath + ".";
                }
                writeObject(pw, propName, bwrap.getPropertyValue(propName), alreadyWritten, beanPathPrefix);
            }
        }
        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                if (beanPath != null) {
                    beanPathPrefix = beanPath + "[";
                }
                writeObject(pw, i + "", array[i], alreadyWritten, beanPathPrefix);
            }
        }
        if (object instanceof List) {
            List<?> list = (List<?>) object;
            for (int i = 0; i < list.size(); i++) {
                if (beanPath != null) {
                    beanPathPrefix = beanPath + "[";
                }
                writeObject(pw, i + "", list.get(i), alreadyWritten, beanPathPrefix);
            }
        } else if (object instanceof Iterable) {
            for (Object next : (Iterable) object) {
                writeObject(pw, "#", next, alreadyWritten, null);
            }
        }
        if (object instanceof Map) {
            for (Object next : ((Map) object).entrySet()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) next;
                if (beanPath != null) {
                    beanPathPrefix = beanPath + "[";
                }
                writeObject(pw, entry.getKey().toString(), entry.getValue(), alreadyWritten, beanPathPrefix);
            }
        }
        pw.println("</table>");
        pw.print("</fieldset><br/>");
        pw.write(close);
    }
