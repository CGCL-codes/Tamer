    protected void loadData() {
        records = new LinkedList<List<Object>>();
        usersRecords = new HashMap<Integer, List<List<Object>>>();
        if (users == null) users = new LinkedList<Integer>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path), separator, '\'');
            CSVWriter writer = new CSVWriter(new FileWriter(path + "out"), separator, '\'');
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<Object> record = new ArrayList<Object>();
                int userId = CommonUtils.objectToInteger(nextLine[userIndex]);
                if (loadOnlySpecifiedUsers && !users.contains(userId)) continue;
                if (!usersRecords.containsKey(userId)) {
                    usersRecords.put(userId, new LinkedList<List<Object>>());
                }
                for (int i = 0; i < nextLine.length; i++) {
                    String s = nextLine[i];
                    if (i == userIndex || i == objectIndex) record.add(CommonUtils.objectToInteger(s)); else if (attributes[i].getType() == Attribute.NOMINAL) record.add(s); else if (attributes[i].getType() == Attribute.NUMERICAL) record.add(CommonUtils.objectToDouble(s)); else if (attributes[i].getType() == Attribute.LIST) {
                        String[] values = s.split(",");
                        record.add(Arrays.asList(values));
                    }
                }
                records.add(record);
                writer.writeNext(nextLine);
                usersRecords.get(userId).add(record);
            }
            reader.close();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sortUsersRatings();
    }
