    static FieldWrapper resolveBeanProperty(final Field field, final EnumSet<BeanRestriction> beanRestrictions) {
        if (beanRestrictions.containsAll(EnumSet.of(BeanRestriction.NO_GETTER, BeanRestriction.YES_GETTER)) || beanRestrictions.containsAll(EnumSet.of(BeanRestriction.NO_SETTER, BeanRestriction.YES_SETTER))) {
            throw new IllegalArgumentException("cannot both include and exclude a setter/getter requirement");
        }
        final String setterName = "set" + StringUtils.capitalize(field.getName());
        final String getterName;
        if (field.getType().equals(boolean.class)) {
            getterName = "is" + StringUtils.capitalize(field.getName());
        } else {
            getterName = "get" + StringUtils.capitalize(field.getName());
        }
        final Method writeMethod = JReflect.findSimpleCompatibleMethod(field.getDeclaringClass(), setterName, field.getType());
        final Method readMethod = JReflect.findSimpleCompatibleMethod(field.getDeclaringClass(), getterName);
        if (!((readMethod != null && beanRestrictions.contains(BeanRestriction.NO_GETTER)) || (!(readMethod != null) && beanRestrictions.contains(BeanRestriction.YES_GETTER)) || (writeMethod != null && beanRestrictions.contains(BeanRestriction.NO_SETTER)) || (!(writeMethod != null) && beanRestrictions.contains(BeanRestriction.YES_SETTER)))) {
            return new FieldWrapper(field, readMethod, writeMethod);
        } else {
            return null;
        }
    }
