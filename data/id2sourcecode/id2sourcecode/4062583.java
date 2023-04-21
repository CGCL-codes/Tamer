    private BeanProperty(Object bean, String name, Class type, Method read, Method write, Field field) {
        this.name = name;
        this.type = type;
        writeMethod = write;
        readMethod = read;
        this.field = field;
        if (isAnnotationPresent(OneToMany.class) || Collection.class.isAssignableFrom(type)) {
            collection = true;
        }
    }
