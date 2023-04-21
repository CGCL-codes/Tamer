    static String update_sheet_back(Character character) throws IOException {
        String charactersheet = "";
        String characterSheetBack = System.getProperty("user.dir");
        characterSheetBack = characterSheetBack.concat("/dataFiles/html/back.html");
        try {
            FileInputStream fis = new FileInputStream(characterSheetBack);
            FileChannel fc = fis.getChannel();
            MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            byte[] barray = new byte[(int) (fc.size())];
            mbf.get(barray);
            charactersheet = new String(barray);
        } finally {
        }
        charactersheet = charactersheet.replaceAll("_SKILLS_", table(character.skills));
        charactersheet = charactersheet.replaceAll("_TALENTS_", table(character.talents));
        charactersheet = charactersheet.replaceAll("_TRAPPINGS_", table(character.trappings));
        return charactersheet;
    }
