    public CreateORMMappingFiles HibernateHBMFile(String tabName, String packName, List<MySQLShowColumns> list) throws Exception {
        CreateORMMappingFiles ORMMappingFiles = new CreateORMMappingFiles();
        logger.info("HibernateHBMFile .....................");
        StringBuffer HibernateHBMFile = new StringBuffer();
        HibernateHBMFile.append(hibernate_hbm_xml_content.split("<class name=")[0]);
        String s1 = "      <class name=\"" + packName + "." + tabName + "\" table=\"" + tabName + "\" dynamic-insert=\"true\" dynamic-update=\"true\" lazy=\"false\">";
        HibernateHBMFile.append(s1).append("\n      <meta attribute=\"implement-equals\">true</meta>\n<cache usage=\"read-write\" />\n");
        String pojoIdName = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals("PRI")) {
                pojoIdName = list.get(i).getField();
                String ID = "\n      <id name=\"" + pojoIdName + "\" column=\"" + pojoIdName + "\" type=\"java.lang.Integer\" unsaved-value=\"-1\">\n            <generator class=\"identity\" />\n      </id>\n";
                HibernateHBMFile.append(ID);
            } else {
                if (list.get(i).getType().equals("datetime")) {
                    String dd = "      <property name=\"" + list.get(i).getField() + "\" column=\"" + list.get(i).getField() + "\" type=\"java.util.Date\" />";
                    HibernateHBMFile.append(dd).append("\n 	");
                } else {
                    String dd = "      <property name=\"" + list.get(i).getField() + "\" column=\"" + list.get(i).getField() + "\"  />";
                    HibernateHBMFile.append(dd).append("\n	");
                }
            }
        }
        HibernateHBMFile.append("\n      </class>\n</hibernate-mapping>");
        ORMMappingFiles.setFileContext(HibernateHBMFile.toString());
        ORMMappingFiles.setFileName(tabName + ".hbm.xml");
        return ORMMappingFiles;
    }
