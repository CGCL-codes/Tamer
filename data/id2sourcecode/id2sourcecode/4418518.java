    public void test_18_23_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            JustReadObject justReadObject = new JustReadObject();
            objToSave = justReadObject;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue("Called readObject on an object without a writeObject", !((JustReadObject) objLoaded).calledReadObject);
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }
