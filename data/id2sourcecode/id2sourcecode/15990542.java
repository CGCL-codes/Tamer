    @Override
    public void didStore(String key, Serializable value) {
        System.out.println("stored: " + value);
        try {
            Field valuesByKeyField = KVStore.class.getDeclaredField("valuesByKey");
            valuesByKeyField.setAccessible(true);
            Field valuesInList = KVStore.class.getDeclaredField("valuesByTimeStamp");
            valuesInList.setAccessible(true);
            ConcurrentHashMap<String, Serializable> valueMap = (ConcurrentHashMap<String, Serializable>) valuesByKeyField.get(null);
            String[] files = KVStore.getActivity().fileList();
            for (String fileName : files) {
                System.out.println("file name: " + fileName);
            }
            FileInputStream persistanceFileInputStream = null;
            byte[] fileBytes = null;
            long size;
            Object storedObject = valueMap.get("testString");
            if (storedObject != null) {
                assertEquals("Some String to Store", ((String) storedObject));
                ConcurrentLinkedQueue<Serializable> valueList = (ConcurrentLinkedQueue<Serializable>) valuesInList.get(null);
                assertFalse(valueList.size() == 0);
                assertFalse(valueMap.size() == 0);
                assertTrue(files.length > 0);
                assertNotNull(Arrays.binarySearch(files, "testString"));
                persistanceFileInputStream = KVStore.getActivity().openFileInput("testString");
                size = persistanceFileInputStream.getChannel().size();
                fileBytes = new byte[(int) size];
                persistanceFileInputStream.read(fileBytes);
                persistanceFileInputStream.close();
                String textString = new String(fileBytes);
                System.out.println("string in file: " + textString);
                assertEquals("\"Some String to Store\"", textString);
            }
            storedObject = valueMap.get("numberList");
            if (storedObject != null) {
                System.out.println("second test");
                assertEquals(ArrayList.class, storedObject.getClass());
                ArrayList storedList = (ArrayList) storedObject;
                assertNotNull(storedList);
                assertNotNull(storedList);
                assertEquals(Integer.class, storedList.get(0).getClass());
                assertEquals(Double.class, storedList.get(1).getClass());
                assertNotNull(Arrays.binarySearch(files, "numberList"));
                persistanceFileInputStream = KVStore.getActivity().openFileInput("numberList");
                size = persistanceFileInputStream.getChannel().size();
                fileBytes = new byte[(int) size];
                persistanceFileInputStream.read(fileBytes);
                persistanceFileInputStream.close();
                String textString = new String(fileBytes);
                System.out.println("nums in file: " + textString);
                assertEquals("[\"5\",\"-7.54\"]", textString);
            }
            storedObject = valueMap.get("personList");
            if (storedObject != null) {
                System.out.println("third test");
                assertEquals(ArrayList.class, storedObject.getClass());
                ArrayList storedList = (ArrayList) storedObject;
                assertNotNull(storedList);
                assertNotNull(storedList);
                assertEquals(Person.class, storedList.get(0).getClass());
                assertEquals(Person.class, storedList.get(1).getClass());
                assertEquals(Person.class, storedList.get(2).getClass());
                assertNotNull(Arrays.binarySearch(files, "personList"));
                persistanceFileInputStream = KVStore.getActivity().openFileInput("personList");
                size = persistanceFileInputStream.getChannel().size();
                fileBytes = new byte[(int) size];
                persistanceFileInputStream.read(fileBytes);
                persistanceFileInputStream.close();
                String textString = new String(fileBytes);
                System.out.println("people: " + textString);
                assertEquals("[{\"lastName\":\"jones\",\"firstName\":\"bob\",\"isFemale\":\"false{,,,,},\"age\":\"23\"},{\"lastName\":\"garcia\",\"firstName\":\"sally\",\"isFemale\":\"true{,,,,},\"age\":\"20\"},{\"lastName\":\"barney\",\"firstName\":\"lee\",\"isFemale\":\"false{,,,,},\"age\":\"49\"}]", textString);
            }
            if (files.length == 3) {
                ((MainActivity) KVStore.getActivity()).testGet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
