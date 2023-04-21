    @Override
    public String toString() {
        return getClass().getSimpleName() + " {name=" + getName() + ", readable=" + (readMethod != null) + ", writable=" + (writeMethod != null) + "}";
    }
