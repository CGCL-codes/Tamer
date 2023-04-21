    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies serialization.", method = "!Serialization", args = {  })
    public void test_18_128_writeObject() {
        Object objToSave = null;
        Object objLoaded;
        try {
            PropertyPermission test = new PropertyPermission("java.*", "read,write");
            PermissionCollection p = test.newPermissionCollection();
            p.add(new PropertyPermission("java.*", "read"));
            p.add(new PropertyPermission("java.*", "write"));
            objToSave = p;
            if (DEBUG) System.out.println("Obj = " + objToSave);
            objLoaded = dumpAndReload(objToSave);
            assertTrue(MSG_TEST_FAILED + objToSave, ((PermissionCollection) objLoaded).implies(test));
        } catch (IOException e) {
            fail("IOException serializing " + objToSave + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException reading Object type : " + e.getMessage());
        } catch (Error err) {
            System.out.println("Error when obj = " + objToSave);
            throw err;
        }
    }
