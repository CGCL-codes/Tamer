    private Object buildAttr(String cls, String tag, double min, double max) {
        cls = cls.replace('.', '_');
        try {
            Object[] args = new Object[3];
            args[0] = tag;
            args[1] = new Double(min);
            args[2] = new Double(max);
            Class[] params = new Class[3];
            params[0] = args[0].getClass();
            params[1] = Double.TYPE;
            params[2] = Double.TYPE;
            Method wrtAttr = this.getClass().getMethod("buildAttr_" + cls, params);
            return wrtAttr.invoke(this, args);
        } catch (NoSuchMethodException e) {
            System.out.println("SaveableObject::buildAttr - Unrecognised class when reading genotype: " + cls + ". Please write the custom methods writeAttr_" + cls + " and readAttr_" + cls + " as public methods in PhysicalObject or a subclass, with the correct parameter types.  See e.g. writeAttr_java_awt_Polygon and readAttr_java_awt_Polygon for examples.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("SaveableObject::buildAttr - Reading genotype line " + gtpPos + ", method buildAttr_" + cls + " threw an exception.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
