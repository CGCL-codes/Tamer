    void test001() {
        boolean b;
        ContentHandlerImpl[] chArr;
        ContentHandlerImpl chTst;
        String[] arr;
        String caller = accessRestricted[0];
        String badStr = "_";
        declare("Verify the write and read of RegistryStore");
        ContentHandlerImpl ch = new ContentHandlerImpl(ZERO_STRINGS, ZERO_STRINGS, ZERO_STRINGS, ZERO_ACTIONNAMES, null, ZERO_STRINGS, "");
        assertNotNull("Verify handler created", ch);
        b = RegistryStore.register(ch);
        assertFalse("Verify empty handler is not registered", b);
        ActionNameMap[] actionnames = new ActionNameMap[2];
        actionnames[0] = new ActionNameMap(actions, enActionNames, "en_US");
        actionnames[1] = new ActionNameMap(actions, frActionNames, "fr_CH");
        ch = new ContentHandlerImpl(types, suffixes, actions, actionnames, ID, accessRestricted, "authority");
        ch.storageId = suiteId;
        ch.classname = classname;
        ch.appname = name;
        b = RegistryStore.register(ch);
        assertTrue("Verify right handler is registered", b);
        chArr = RegistryStore.findHandler(caller, RegistryStore.FIELD_TYPES, ch.getType(0));
        if (chArr != null && chArr.length == 1) {
            assertEquals("Verify handler ID after search by type", ch.ID, chArr[0].ID);
        } else {
            fail("Verify search handler by type");
        }
        chArr = RegistryStore.findHandler(caller, RegistryStore.FIELD_TYPES, badStr);
        assertTrue("Verify empty search results by type", chArr == null || chArr.length == 0);
        chArr = RegistryStore.findHandler(caller, RegistryStore.FIELD_ID, ch.ID + ch.ID.substring(0, 3));
        if (chArr != null && chArr.length == 1) {
            assertEquals("Verify handler ID after partial search", ch.ID, chArr[0].ID);
        } else {
            fail("Verify handler ID after search by cut ID");
        }
        chTst = RegistryStore.getHandler(caller, ch.ID, RegistryStore.SEARCH_EXACT);
        if (chTst != null) {
            assertEquals("Verify handler search by ID exact", ch.ID, chTst.ID);
        } else {
            fail("Verify handler search by ID exact");
        }
        arr = RegistryStore.getValues(caller, RegistryStore.FIELD_TYPES);
        assertTrue("Verify getValues by type", arr != null && arr.length >= 2);
        b = testId(ch.ID);
        assertFalse("Verify test equal ID", b);
        b = testId(ch.ID.substring(0, 3));
        assertFalse("Verify test prefixed ID", b);
        b = testId(ch.ID + "qqq");
        assertFalse("Verify test prefixing ID", b);
        b = testId("qqq" + ch.ID);
        assertTrue("Verify test good ID", b);
        ContentHandlerImpl ch2 = RegistryStore.getHandler(null, ch.ID, RegistryStore.SEARCH_EXACT);
        assertNotNull("Verify loadHandler by ID", ch2);
        if (ch2 != null) {
            ch2.appname = ch.appname;
            ch2.version = ch.version;
            assertEquals("Verify loadHandler by ID", ch, ch2);
        }
        b = RegistryStore.unregister(ch.ID);
        assertTrue("Verify right handler is unregistered " + "/CHECK: if JSR 211 database has the correct format!/", b);
    }
