            public boolean visitProperty(Property property) {
                Property targetProperty = targetDescriptor.getProperty(property.getName());
                targetProperty.writeProperty(target, null, property.readProperty(source));
                return true;
            }
