    public void testAllReadWriteProperties() {
        try {
            for (int i = 0; i < classes.length; i++) {
                Object object = classes[i].newInstance();
                ClassIntrospector introspector = ClassIntrospector.getInstance(classes[i]);
                List writeables = Arrays.asList(introspector.getWriteablePropertyNames());
                List readables = Arrays.asList(introspector.getReadablePropertyNames());
                for (int j = 0; j < writeables.size(); j++) {
                    String writeable = (String) writeables.get(j);
                    if (readables.contains(writeable)) {
                        Class type = introspector.getGetterType(writeable);
                        Object sample = getSampleFor(type);
                        BeanIntrospector probe = new BeanIntrospector();
                        probe.setObject(object, writeable, sample);
                        assertEquals(sample, probe.getObject(object, writeable));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error. ", e);
        }
    }
