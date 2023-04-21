package com.tinywebgears.tuatara.framework.action;

public abstract class AbstractSingleContextAction<T> extends AbstractContextAction<T> implements SingleContextActionIF<T> {

    private final Class<T> classType;

    public AbstractSingleContextAction(Class<T> classType, ContextActionProperties properties) {
        super(properties);
        this.classType = classType;
    }

    public Class<T> getContextClassType() {
        return classType;
    }
}
