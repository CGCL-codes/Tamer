    public void initialize(Serializer serializer, Class orig) {
        if (orig == Date.class) System.out.print("");
        if (orig.isArray()) {
            this.isArray = true;
            orig = orig.getComponentType();
        } else if (orig == Class.class) {
            isClass = true;
        } else if (Collection.class.isAssignableFrom(orig)) {
            this.isCollection = true;
        } else if (Map.class.isAssignableFrom(orig)) {
            this.isMap = true;
        } else {
            try {
                if (orig.getDeclaredMethod("readObject", java.io.ObjectInputStream.class) != null && orig.getDeclaredMethod("writeObject", java.io.ObjectOutputStream.class) != null) {
                    this.hasSpecializedSerialization = true;
                }
            } catch (Exception e) {
            }
            try {
                if (orig.getDeclaredMethod("writeReplace") != null) {
                    this.hasWriteReplace = true;
                }
            } catch (Exception e) {
            }
            try {
                if (orig.getDeclaredMethod("readResolve") != null) {
                    this.hasReadResolve = true;
                }
            } catch (Exception e) {
            }
        }
        this.name = orig.getName();
        if (!isArray && !isCollection && !isMap && !hasSpecializedSerialization && !isClass && orig.getSuperclass() != null) this.superClass = serializer.getGenericClass(orig.getSuperclass());
        this.isPrimitive = orig.isPrimitive() || orig.equals(String.class);
        this.isEnum = orig.isEnum();
        List<GenericField> fields = new LinkedList<GenericField>();
        int offset = superClass != null ? superClass.getFieldCount() : 0;
        if (!isPrimitive && !isMap && !isCollection && !isClass && !isArray && !hasSpecializedSerialization) {
            int n = 0;
            for (Field f : orig.getDeclaredFields()) {
                if ((f.getModifiers() & (Modifier.TRANSIENT | Modifier.STATIC)) > 0) continue;
                f.setAccessible(true);
                fields.add(new GenericField(serializer, f, n + offset));
                n++;
            }
        }
        this.fields = fields.toArray(new GenericField[0]);
        this.declaredFieldCount = isPrimitive ? 0 : this.fields.length;
        this.fieldCount = this.declaredFieldCount + (superClass != null ? superClass.getFieldCount() : 0);
    }
