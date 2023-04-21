        public PropertyDescriptor(String propName, String readableName, String description, AccessRule readRule, AccessRule writeRule) {
            super();
            this.propName = propName;
            this.readableName = readableName;
            this.description = description;
            this.readRule = readRule;
            this.writeRule = writeRule;
        }
