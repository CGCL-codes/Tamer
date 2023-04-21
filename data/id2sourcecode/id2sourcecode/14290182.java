    public static void saveCharacter(MemberPerson person) {
        Date d = new Date();
        String dT = new SimpleDateFormat("yyyyddMM-HH.mm.ss.SSS").format(d);
        String slot = charsDir + "/" + dT + "_" + person.foreName + "/";
        File f = new File(slot);
        f.mkdirs();
        File saveGame = new File(slot + "character.zip");
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(saveGame));
            zipOutputStream.putNextEntry(new ZipEntry("character.xml"));
            person.getXml(zipOutputStream);
            zipOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
