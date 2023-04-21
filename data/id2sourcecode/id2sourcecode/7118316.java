    public void readArray(JammEnum type) throws IllegalArgumentException {
        try {
            FileReader fr;
            BufferedReader br;
            String line;
            switch(type) {
                case READ_ACCOUNTS:
                    fr = new FileReader(accounts_file);
                    br = new BufferedReader(fr);
                    while ((line = br.readLine()) != null) {
                        list_accounts.addElement(line);
                    }
                    logger.writeLog(JammEnum.INFO, "Finished Accounts Array reading");
                    break;
                case READ_COLUMNSIZES:
                    list_columnSizes.clear();
                    fr = new FileReader(colSizes_file);
                    br = new BufferedReader(fr);
                    while (((line = br.readLine()) != null)) {
                        list_columnSizes.add(Integer.parseInt(line));
                    }
                    break;
                default:
                    logger.writeLog(JammEnum.ERROR, "IOException during Array read");
                    throw new IllegalArgumentException();
            }
            fr.close();
        } catch (IOException e) {
            logger.writeLog(JammEnum.ERROR, "File not found on HDD");
            e.printStackTrace();
        }
    }
