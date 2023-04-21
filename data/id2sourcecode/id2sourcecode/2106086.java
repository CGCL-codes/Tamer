    public void testOpenUpdateSaveCycle() throws IOException, CoreException {
        File inputFile = getSessionFile("new_empty_session.jmx");
        File outputFile = new File("test1.xml");
        JMoneyXmlFormat reader = new JMoneyXmlFormat();
        SessionManager manager = new SessionManager(JMoneyXmlFormat.ID_FILE_FORMAT, reader, inputFile);
        reader.readSessionQuietly(inputFile, manager, null);
        assertNotNull(manager);
        assertNotNull(manager.getSession());
        Session session = manager.getSession();
        BankAccount account1 = session.getAccountCollection().createNewElement(BankAccountInfo.getPropertySet());
        BankAccount account2 = session.getAccountCollection().createNewElement(BankAccountInfo.getPropertySet());
        account1.setName("My Checking Account");
        account2.setName("My Savings Account");
        Transaction trans = session.getTransactionCollection().createNewElement(TransactionInfo.getPropertySet());
        Entry entry1 = trans.getEntryCollection().createEntry();
        Entry entry2 = trans.getEntryCollection().createEntry();
        entry1.setAccount(account1);
        entry2.setAccount(account2);
        entry1.setAmount(1234);
        entry2.setAmount(-1234);
        entry1.setMemo("transfer from savings");
        entry2.setMemo("transfer to checking");
        try {
            reader.writeSessionQuietly(manager, outputFile, null);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        }
        SessionManager manager2 = new SessionManager(JMoneyXmlFormat.ID_FILE_FORMAT, reader, inputFile);
        reader.readSessionQuietly(inputFile, manager2, null);
        assertNotNull(manager2);
        assertNotNull(manager2.getSession());
        Session session2 = manager2.getSession();
        assertEquals(1, session2.getCommodityCollection().size());
        assertEquals(2, session2.getAccountCollection().size());
        assertEquals(1, session2.getTransactionCollection().size());
    }
