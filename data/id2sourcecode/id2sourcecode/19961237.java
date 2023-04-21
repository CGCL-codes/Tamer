        public void option(String s, Object o) {
            int i = s.indexOf("=");
            if (i > 0) {
                String ans = s.substring(0, i);
                int slen = s.length();
                String value = s.substring(i + 1, slen);
                try {
                    BeanInfo info = Introspector.getBeanInfo(o.getClass());
                    PropertyDescriptor[] props = info.getPropertyDescriptors();
                    String pname = new String(" ");
                    Class type = null;
                    Method writer = null, reader = null;
                    int x = -1, len = props.length;
                    while (!pname.equals(ans) && x < len) {
                        x++;
                        pname = props[x].getDisplayName();
                        type = props[x].getPropertyType();
                        reader = props[x].getReadMethod();
                        writer = props[x].getWriteMethod();
                    }
                    if (x == len) System.out.println("Did not find Option!");
                    if (type.equals(boolean.class)) {
                        writer.invoke(o, new Object[] { new Boolean(value) });
                        Object val = reader.invoke(o, new Object[] {});
                    } else if (type.equals(int.class)) {
                        writer.invoke(o, new Object[] { new Integer(value) });
                        Object val = reader.invoke(o, new Object[] {});
                    } else if (type.equals(double.class)) {
                        writer.invoke(o, new Object[] { new Double(value) });
                        Object val = reader.invoke(o, new Object[] {});
                    } else if (type.equals(String.class)) {
                        writer.invoke(o, new Object[] { new String(value) });
                        Object val = reader.invoke(o, new Object[] {});
                    }
                } catch (Exception e) {
                    System.out.println("Cannot find class");
                }
            } else System.out.println("Cannot compute option - no object defined");
        }
