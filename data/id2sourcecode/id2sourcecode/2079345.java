        public Accessor(Object bean, Method readMethod, Method writeMethod, String label, PropertyType type, BeanProperty propertyAnnotation, String propertyName, EditComponent editComponent, List<PropertyValidator> validators) {
            this.bean = bean;
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
            this.label = label;
            this.type = type;
            this.propertyAnnotation = propertyAnnotation;
            this.propertyName = propertyName;
            this.editComponent = editComponent;
            this.validators = validators;
        }
