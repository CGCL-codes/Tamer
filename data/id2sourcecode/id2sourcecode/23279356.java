        private PropertyDescriptor(String property, Class propertyClass, Class keyClass, Method[] readMethods, Method[] writeMethods) {
            this.property = property;
            this.propertyClass = propertyClass;
            this.keyClass = keyClass;
            this.readMethods = readMethods;
            this.writeMethods = writeMethods;
        }
