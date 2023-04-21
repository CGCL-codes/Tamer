    public void testSave() throws Exception {
        PalmDB database = new PalmDBImpl();
        Header header = new Header();
        Timestamp timestamp = Timestamp.valueOf("2006-12-01 08:10:25.000");
        header.setCreationDate(new Date(timestamp));
        header.setModificationDate(new Date(timestamp));
        header.setName("ToDoDB");
        header.setAttributes(Header.ATTRIBUTE_BACKUP_DATABASE);
        header.setCreator("todo");
        header.setType("DATA");
        header.setLastBackupDate(new Date(new UInt32(0)));
        header.setModificationNumber(new UInt32(0));
        header.setNumberOfRecords(new UInt16(1));
        database.setHeader(header);
        ApplicationInfo info = new ApplicationInfo();
        info.setCategoryLabels(Arrays.asList("Unfiled", "Test"));
        info.setRenamedCategories(new UInt16(1));
        ArrayList<UInt8> categoryIds = new ArrayList<UInt8>();
        for (int i = 0; i != info.getCategoryLabels().size(); i++) {
            categoryIds.add(new UInt8(i));
        }
        info.setCategoryUniqueIds(categoryIds);
        info.setLastUniqueId(new UInt8(info.getCategoryUniqueIds().size() - 1));
        database.setApplicationInfo(info);
        RecordHeader recordHeader = new RecordHeader();
        recordHeader.setAttributes(RecordHeader.RECORD_DIRTY.or(new UInt8(0x01)));
        recordHeader.setUniqueId(new UInt32(0));
        byte[] todoRecord = { (byte) 0xff, (byte) 0xff, 0x01, 0x47, 0x65, 0x74, 0x20, 0x61, 0x20, 0x6c, 0x69, 0x66, 0x65, 0x2e, 0x00, 0x4e, 0x6f, 0x74, 0x65, 0x20, 0x74, 0x6f, 0x20, 0x73, 0x65, 0x6c, 0x66, 0x2e, 0x00 };
        Record record = new RecordImpl(todoRecord);
        record.setHeader(recordHeader);
        database.setRecords(Arrays.asList(record));
        PalmWriter writer = new PalmWriter();
        writer.save(new FileOutputStream(FILE), database);
        MessageDigest digest = MessageDigest.getInstance("MD5");
        FileInputStream stream = new FileInputStream(new File(FILE));
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = stream.read(buffer)) != -1) {
            digest.update(buffer, 0, read);
        }
        String hash = Utilities.byteArrayToHexString(digest.digest());
        assertEquals(MD5SUM, hash);
    }
