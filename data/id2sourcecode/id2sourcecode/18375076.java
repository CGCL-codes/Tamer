            public Object newInstance(Map properties) {
                Object current = getCurrent();
                if (current instanceof DefaultTableModel) {
                    DefaultTableModel model = (DefaultTableModel) current;
                    Object header = properties.remove("header");
                    if (header == null) {
                        header = "";
                    }
                    Closure readClosure = (Closure) properties.remove("read");
                    if (readClosure == null) {
                        throw new IllegalArgumentException("Must specify 'read' Closure property for a closureColumn");
                    }
                    Closure writeClosure = (Closure) properties.remove("write");
                    Class type = (Class) properties.remove("type");
                    if (type == null) {
                        type = Object.class;
                    }
                    return model.addClosureColumn(header, readClosure, writeClosure, type);
                } else {
                    throw new RuntimeException("propertyColumn must be a child of a tableModel");
                }
            }
