    protected void doElementTest(String tagName, Class interfaceName, Object[][] attributes) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Element qualifiedElement = createElement(tagName, attributes);
        assertTrue("node should be a " + interfaceName.getName() + " but is " + qualifiedElement.getClass().getName(), interfaceName.isAssignableFrom(qualifiedElement.getClass()));
        assertEquals("Tag name", tagName.toUpperCase(), qualifiedElement.getNodeName());
        for (int i = 0; i < attributes.length; i++) {
            String propertyName = (String) attributes[i][0];
            Object propertyValue = attributes[i][1];
            assertEquals(propertyName, propertyValue, getProperty(qualifiedElement, propertyName));
        }
        Element element = createElement(tagName);
        ((ElementImpl) element).addDomListener(this);
        for (int i = 0; i < attributes.length; i++) {
            final String propertyName = (String) attributes[i][0];
            final Object propertyValue = attributes[i][1];
            Object defaultValue = attributes[i].length == 2 ? null : attributes[i][2];
            if (defaultValue == null) {
                assertNull(propertyName + " should not be specified by default", getProperty(element, propertyName));
            } else {
                assertEquals("default " + propertyName, defaultValue, getProperty(element, propertyName));
            }
            Method writeMethod = AbstractHTMLElementTest.getWriteMethod(element, propertyName);
            if (attributes[i].length > 3 && attributes[i][3].equals("ro")) {
                assertNull(propertyName + " is not read-only", writeMethod);
            } else {
                assertNotNull("No modifier defined for " + propertyName);
                clearReceivedEvents();
                writeMethod.invoke(element, new Object[] { propertyValue });
                assertEquals("modified " + propertyName, propertyValue, getProperty(element, propertyName));
                expectPropertyChange(element, propertyName);
            }
        }
    }
