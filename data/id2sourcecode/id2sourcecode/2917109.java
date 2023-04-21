    public TypeSpec(Class<?> object) {
        this.object = object;
        this.constructors = this.object.getConstructors();
        if (!isGeneric(object)) specialMethods(this.methods, object);
        this.injectMosaic = new Injector.MosaicHandler();
        this.injectRegistry = new Injector.FactoryHandler();
        this.waveConstraint = new ArrayList<Mosaic.AppliesToFilter>();
        {
            AppliesTo ann = object.getAnnotation(AppliesTo.class);
            if (ann != null) for (Class<?> annotation : ann.value()) if (AppliesToFilter.class.isAssignableFrom(annotation)) try {
                this.waveConstraint.add((AppliesToFilter) annotation.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Can't instantiate class " + annotation + ".\nEither it is not public or not static.", e);
            } else if (Annotation.class.isAssignableFrom(annotation)) this.waveConstraint.add(new AnnotationWaveFilter(annotation)); else this.waveConstraint.add(new SubclassOfWaveFilter(annotation));
        }
    }
