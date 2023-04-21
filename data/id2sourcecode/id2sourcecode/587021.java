    private String getPolicyText(String webappFolder, String installDir) {
        String text = "// ========== JOTWIKI Webapp Permissions ========================\n";
        text += "grant codeBase \"file:${catalina.home}/bin/tomcat-juli.jar\" {\n";
        text += "// Tomcat-Juli as  'bug', always trying to read that logging.properties, and if no permission -> crash webapp\n";
        text += "permission java.io.FilePermission \"" + webappFolder + "jotwiki/WEB-INF/classes/logging.properties\", \"read\";\n";
        text += "};\n";
        text += "// Actual jotwiki perms.\n";
        text += "grant codeBase \"file:" + webappFolder + "jotwiki/WEB-INF/-\"{\n";
        text += "//Perm to read JOTWIKI_HOME property\n";
        text += "permission java.util.PropertyPermission \"JOTWIKI_HOME\", \"read\";\n";
        text += "//Perm to read jotwiki webapp conf/property files.\n";
        text += "permission java.io.FilePermission \"" + webappFolder + "jotwiki/jotconf/*\", \"read\";\n";
        text += "//Full permissions on jotwiki data folder\n";
        text += "permission java.io.FilePermission \"" + installDir + "-\", \"read,write,delete,execute\";\n";
        text += "};\n";
        text += "// ========= End JOTWIKI ===============================\n";
        return text;
    }
