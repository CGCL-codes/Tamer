    public InputComponentHandlerBase(InputComponent component, String readProperty, String writeProperty) {
        this.component = component;
        this.descriptor = ClassCache.getFor(component.getClass());
        this.readProperty = descriptor.getProperty(readProperty);
        this.writeProperty = descriptor.getProperty(writeProperty);
    }
