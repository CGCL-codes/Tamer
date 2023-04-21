    public Class<?> getReturnType() throws ParseException {
        if (">".equals(getName()) || ">=".equals(getName()) || "<".equals(getName()) || "<=".equals(getName()) || "==".equals(getName()) || "!=".equals(getName()) || "&&".equals(getName()) || "||".equals(getName())) {
            return boolean.class;
        }
        Class<?> leftType = leftParameter.getReturnType();
        if (StringUtils.isFunction(getName())) {
            String name = getName().substring(1);
            if ("to".equals(name) && rightParameter instanceof Constant && rightParameter.getReturnType() == String.class) {
                String rightCode = rightParameter.getCode();
                if (rightCode.length() > 2 && rightCode.startsWith("\"") && rightCode.endsWith("\"")) {
                    return ClassUtils.forName(getPackages(), rightCode.substring(1, rightCode.length() - 1));
                }
            } else if ("class".equals(name)) {
                return Class.class;
            }
            Class<?>[] rightTypes = rightParameter.getReturnTypes();
            Collection<Class<?>> functions = getFunctions();
            if (functions != null && functions.size() > 0) {
                Class<?>[] allTypes;
                if (rightTypes == null || rightTypes.length == 0) {
                    if (leftType == null) {
                        allTypes = new Class<?>[0];
                    } else {
                        allTypes = new Class<?>[] { leftType };
                    }
                } else {
                    allTypes = new Class<?>[rightTypes.length + 1];
                    allTypes[0] = leftType;
                    System.arraycopy(rightTypes, 0, allTypes, 1, rightTypes.length);
                }
                for (Class<?> function : functions) {
                    try {
                        Method method = ClassUtils.searchMethod(function, name, allTypes);
                        if (Object.class.equals(method.getDeclaringClass())) {
                            break;
                        }
                        return method.getReturnType();
                    } catch (NoSuchMethodException e) {
                    }
                }
            }
            return getReturnType(leftType, getName().substring(1), rightTypes);
        } else if (getName().equals("..")) {
            if (leftType == int.class || leftType == Integer.class || leftType == short.class || leftType == Short.class || leftType == long.class || leftType == Long.class) {
                return IntegerSequence.class;
            } else if (leftType == char.class || leftType == Character.class) {
                return CharacterSequence.class;
            } else if (leftType == String.class) {
                return StringSequence.class;
            } else {
                throw new ParseException("The operator \"..\" unsupported parameter type " + leftType, getOffset());
            }
        } else if ("[".equals(getName())) {
            Map<String, Class<?>> types = getParameterTypes();
            if (Map.class.isAssignableFrom(leftType)) {
                if (leftParameter instanceof Variable) {
                    String var = ((Variable) leftParameter).getName();
                    Class<?> t = types.get(var + ":1");
                    if (t != null) {
                        return t;
                    }
                }
                return Object.class;
            }
            Class<?> rightType = rightParameter.getReturnType();
            if (List.class.isAssignableFrom(leftType)) {
                if (IntegerSequence.class.equals(rightType) || int[].class == rightType) {
                    return List.class;
                } else if (int.class.equals(rightType)) {
                    if (leftParameter instanceof Variable) {
                        String var = ((Variable) leftParameter).getName();
                        Class<?> t = types.get(var + ":0");
                        if (t != null) {
                            return t;
                        }
                    }
                    return Object.class;
                } else {
                    throw new ParseException("The \"[]\" index type: " + rightType.getName() + " must be int!", getOffset());
                }
            } else if (leftType.isArray()) {
                if (IntegerSequence.class.equals(rightType) || int[].class == rightType) {
                    return leftType;
                } else if (int.class.equals(rightType)) {
                    return leftType.getComponentType();
                } else {
                    throw new ParseException("The \"[]\" index type: " + rightType.getName() + " must be int!", getOffset());
                }
            }
            throw new ParseException("Unsuptorted \"[]\" for non-array type: " + leftType.getName(), getOffset());
        } else if ("?".equals(getName())) {
            return rightParameter.getReturnType();
        } else if (":".equals(getName()) && !(leftParameter instanceof BinaryOperator && "?".equals(((BinaryOperator) leftParameter).getName()))) {
            return Map.Entry.class;
        } else {
            return leftType;
        }
    }
