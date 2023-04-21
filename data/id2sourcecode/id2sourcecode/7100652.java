    @SuppressWarnings("unchecked")
    protected void writeNestedNames(PrintWriter pw, Object obj, String prefix, Set<Object> alreadyWritten) {
        if (obj == null || alreadyWritten.contains(obj) || obj.getClass().getName().startsWith("org.springframework.")) {
            return;
        }
        String close = "";
        if (getBeanToNameMap().containsKey(obj)) {
            String name = getBeanToNameMap().get(obj);
            pw.println("<li><a href='" + prefix + name + "'>" + name + "</a>");
            pw.println("<span style='color:#999'>" + obj.getClass().getName() + "</span><ul>");
            close = "</ul></li>";
        }
        if (!alreadyWritten.contains(obj)) {
            alreadyWritten.add(obj);
            try {
                BeanWrapperImpl bwrap = new BeanWrapperImpl(obj);
                for (PropertyDescriptor pd : getPropertyDescriptors(bwrap)) {
                    if (pd.getReadMethod() != null) {
                        String propName = pd.getName();
                        writeNestedNames(pw, bwrap.getPropertyValue(propName), prefix, alreadyWritten);
                    }
                }
                if (obj.getClass().isArray()) {
                    List<?> list = Arrays.asList(obj);
                    for (int i = 0; i < list.size(); i++) {
                        writeNestedNames(pw, list.get(i), prefix, alreadyWritten);
                    }
                }
                if (obj instanceof Iterable) {
                    for (Object next : (Iterable) obj) {
                        writeNestedNames(pw, next, prefix, alreadyWritten);
                    }
                }
            } catch (InvalidPropertyException ipe) {
                pw.println("<span style='color:red'>" + ipe.getMessage() + "</span>");
            }
        }
        pw.println(close);
    }
