    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (ReflectionHelper.isMethod(method, "toString", String.class, new Class[0])) {
            StringBuffer sb = new StringBuffer();
            sb.append("[BeanProxy: ");
            if (m_subject != null) {
                sb.append((String) method.invoke(m_subject, args));
                sb.append(", ");
            }
            sb.append("properties=");
            sb.append(m_properties.values().toString());
            sb.append("]");
            return sb.toString();
        }
        if (m_subjectImplementedMethods.contains(method)) {
            Method subjectMethod = (Method) m_ifaceMethodToSubjectMap.get(method);
            return subjectMethod.invoke(m_subject, args);
        } else {
            Property p;
            p = (Property) m_readMethodToPropertyMap.get(method);
            if (p != null) {
                Object result = (p.value != null) ? p.value : s_defaultValueMap.get(p.type);
                return result;
            }
            p = (Property) m_writeMethodToPropertyMap.get(method);
            if (p != null) {
                p.value = args[0];
                return null;
            }
            System.err.println("\n\n**********************************************************************");
            System.err.println(" B U G - R E P O R T   I N F O R M A T I O N");
            System.err.println("**********************************************************************");
            debug();
            System.err.println("**********************************************************************\n\n");
            throw new BugException("BeanInvocationHandler initialisation buggy. Method '" + method + "' neither in m_subjectImplementedMethods set (" + m_subjectImplementedMethods + ") nor exists a mapping " + "in m_writeMethodToPropertyMap (" + m_writeMethodToPropertyMap + ") or " + "m_readMethodToPropertyMap (" + m_readMethodToPropertyMap + ").");
        }
    }
