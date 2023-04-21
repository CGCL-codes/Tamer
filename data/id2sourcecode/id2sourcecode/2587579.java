        public PropertieDescription(Class propertyType, Method writeMethod, Method readMethod, String name) {
            super();
            this.propertyType = propertyType;
            this.writeMethod = writeMethod;
            this.name = name;
            this.readMethod = readMethod;
        }
