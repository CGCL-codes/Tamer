    public BeanProperties(Field field, Method readMethod, boolean readUsesRequest, Method writeMethod, boolean writeUsesRequest, String propertyName, BeanProperty annotation) {
        this.field = field;
        if (this.field != null) {
            this.field.setAccessible(true);
        }
        this.propertyName = propertyName;
        this.annotation = annotation;
        this.readMethod = readMethod;
        if (readMethod != null) {
            readMethod.setAccessible(true);
        }
        this.writeMethod = writeMethod;
        if (writeMethod != null) {
            writeMethod.setAccessible(true);
        }
        this.readUsesRequest = readUsesRequest;
        this.writeUsesRequest = writeUsesRequest;
    }
