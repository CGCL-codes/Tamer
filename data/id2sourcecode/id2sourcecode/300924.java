    public static ModelHogComponent[] getComponents() {
        Vector<String> v = new Vector<String>();
        v.add("ModelHogHelp");
        v.add("ModelHogIACV");
        v.add("ModelHogSpark");
        v.add("ModelHogFuel");
        ModelHogComponent[] components = new ModelHogComponent[v.size()];
        Iterator<String> itr = v.iterator();
        int i = 0;
        while (itr.hasNext()) {
            try {
                Class<?> cls = Class.forName("hog." + (String) itr.next());
                Constructor<?>[] temp = cls.getConstructors();
                ModelHogComponent mhc = (ModelHogComponent) temp[0].newInstance();
                components[i] = mhc;
                i++;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        ModelHogComponent[] mhcArray = new ModelHogComponent[i];
        System.arraycopy(components, 0, mhcArray, 0, i);
        return mhcArray;
    }
