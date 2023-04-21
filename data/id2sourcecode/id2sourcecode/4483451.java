    public static void updateJbossStandard(String targetServerDir, String setupDir, String sourceDeployment) throws Exception {
        String jbossStandardXML = targetServerDir + "/server/default/conf/standardjboss.xml";
        String jbossStandardCopy = setupDir + "/standardjboss.xml.http";
        String jbossStandardOriginal = setupDir + "/standardjboss.xml.orig";
        FileSystemUtil.createDirectory(setupDir);
        FileUtils.copyFile(new File(jbossStandardXML), new File(jbossStandardCopy));
        FileUtils.copyFile(new File(jbossStandardXML), new File(jbossStandardOriginal));
        String insert = FileSystemUtil.getTextContents(new File(sourceDeployment + "/configure/standardjboss_httpinvoker.txt"));
        String jbossStandardContents = FileSystemUtil.getTextContents(new File(jbossStandardCopy));
        jbossStandardContents = jbossStandardContents.replace("<invoker-proxy-bindings>", "<invoker-proxy-bindings>\n" + insert + "\n");
        int statelessSessionBeanStartsAt = jbossStandardContents.indexOf("<container-name>Standard Stateless SessionBean</container-name>");
        if (statelessSessionBeanStartsAt > 0) {
            String replaceText = "<invoker-proxy-binding-name>";
            String replaceTextEnd = "</invoker-proxy-binding-name>";
            int replaceStart = jbossStandardContents.indexOf(replaceText, statelessSessionBeanStartsAt);
            int replaceEnd = jbossStandardContents.indexOf(replaceTextEnd, statelessSessionBeanStartsAt) + replaceTextEnd.length();
            jbossStandardContents = jbossStandardContents.substring(0, replaceStart) + "<invoker-proxy-binding-name>stateless-http-invoker</invoker-proxy-binding-name>" + jbossStandardContents.substring(replaceEnd);
        } else {
            throw new Exception("cant find Standard Stateless SessionBean");
        }
        FileSystemUtil.createFile(jbossStandardCopy, jbossStandardContents);
        FileSystemUtil.deleteFile(setupDir + "/standardjboss_httpinvoker.txt");
    }
