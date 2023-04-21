    public void test_impliesLjava_security_Permission() throws Exception {
        URL classURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        assertNotNull("Could not get this class' location", classURL);
        File policyFile = Support_GetLocal.createTempFile(".policy");
        policyFile.deleteOnExit();
        URL signedBKS = getResourceURL("PermissionCollection/signedBKS.jar");
        String signedBKSPath = new File(signedBKS.getFile()).getCanonicalPath();
        signedBKSPath = signedBKSPath.replace('\\', '/');
        URL keystoreBKS = getResourceURL("PermissionCollection/keystore.bks");
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(policyFile);
            String linebreak = System.getProperty("line.separator");
            StringBuilder towrite = new StringBuilder();
            towrite.append("grant {");
            towrite.append(linebreak);
            towrite.append("    permission java.io.FilePermission \"");
            towrite.append(signedBKSPath);
            towrite.append("\", \"read\";");
            towrite.append(linebreak);
            towrite.append("    permission java.lang.RuntimePermission \"getProtectionDomain\";");
            towrite.append(linebreak);
            towrite.append("    permission java.security.SecurityPermission \"getPolicy\";");
            towrite.append(linebreak);
            towrite.append("};");
            towrite.append(linebreak);
            towrite.append(linebreak);
            towrite.append("grant codeBase \"");
            towrite.append(signedBKS.toExternalForm());
            towrite.append("\" signedBy \"eleanor\" {");
            towrite.append(linebreak);
            towrite.append("    permission java.io.FilePermission \"test1.txt\", \"write\";");
            towrite.append(linebreak);
            towrite.append("    permission mypackage.MyPermission \"essai\", signedBy \"eleanor,dylan\";");
            towrite.append(linebreak);
            towrite.append("};");
            towrite.append(linebreak);
            towrite.append(linebreak);
            towrite.append("grant codeBase \"");
            towrite.append(signedBKS.toExternalForm());
            towrite.append("\" signedBy \"eleanor\" {");
            towrite.append(linebreak);
            towrite.append("    permission java.io.FilePermission \"test2.txt\", \"write\";");
            towrite.append(linebreak);
            towrite.append("};");
            towrite.append(linebreak);
            towrite.append(linebreak);
            towrite.append("grant codeBase \"");
            towrite.append(classURL.toExternalForm());
            towrite.append("\" {");
            towrite.append(linebreak);
            towrite.append("    permission java.security.AllPermission;");
            towrite.append(linebreak);
            towrite.append("};");
            towrite.append(linebreak);
            towrite.append(linebreak);
            towrite.append("keystore \"");
            towrite.append(keystoreBKS.toExternalForm());
            towrite.append("\",\"BKS\";");
            towrite.append(linebreak);
            fileOut.write(towrite.toString().getBytes());
            fileOut.flush();
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
        File jarFile = null;
        FileOutputStream fout = null;
        InputStream jis = null;
        try {
            jis = Support_Resources.getResourceStream("PermissionCollection/mypermissionBKS.jar");
            jarFile = Support_GetLocal.createTempFile(".jar");
            jarFile.deleteOnExit();
            fout = new FileOutputStream(jarFile);
            int c = jis.read();
            while (c != -1) {
                fout.write(c);
                c = jis.read();
            }
            fout.flush();
        } finally {
            if (fout != null) {
                fout.close();
            }
            if (jis != null) {
                jis.close();
            }
        }
        String classPath = new File(classURL.getFile()).getPath();
        String[] classPathArray = new String[2];
        classPathArray[0] = classPath;
        classPathArray[1] = jarFile.getPath();
        String[] args = { "-Djava.security.policy=" + policyFile.toURL(), "tests.support.Support_PermissionCollection", signedBKS.toExternalForm() };
        String result = Support_Exec.execJava(args, classPathArray, true);
        StringTokenizer resultTokenizer = new StringTokenizer(result, ",");
        assertEquals("Permission should be granted for test1.txt", "true", resultTokenizer.nextToken());
        assertEquals("Signed Permission should be granted for my permission", "true", resultTokenizer.nextToken());
        assertEquals("signed Permission should be granted for text2.txt", "true", resultTokenizer.nextToken());
        assertEquals("Permission should not be granted for text3.txt", "false", resultTokenizer.nextToken());
    }
