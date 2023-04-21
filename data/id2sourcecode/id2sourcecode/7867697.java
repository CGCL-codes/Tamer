    private void setTestPermission0(String perm, String arg1, String arg2, boolean changeSM) {
        SecurityManager sm = System.getSecurityManager();
        try {
            File fff2 = null;
            java.io.DataOutputStream dos2 = null;
            try {
                fff2 = new File(System.getProperty("user.home") + java.io.File.separator + "." + policyFile + "");
                if (fff2.exists()) {
                    File fff3 = new File(System.getProperty("user.home") + java.io.File.separator + "." + policyFile + ".copiedBySMT");
                    if (fff3.exists()) {
                        fff2.delete();
                        fff2 = new File(System.getProperty("user.home") + java.io.File.separator + "." + policyFile + "");
                    } else {
                        fff2.renameTo(fff3);
                    }
                }
                ;
                fff2.createNewFile();
                dos2 = new java.io.DataOutputStream(new java.io.FileOutputStream(fff2));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dos2.writeBytes("grant {\n");
                String tmpstr = "";
                if (arg1.length() != 0 || arg2.length() != 0) {
                    if (arg1.length() != 0 && arg2.length() != 0) {
                        tmpstr = " \"" + arg1 + "\", \"" + arg2 + "\"";
                    } else {
                        tmpstr = " \"" + arg1 + arg2 + "\"";
                    }
                }
                if (perm != null && perm.length() != 0) {
                    dos2.writeBytes("permission " + perm + tmpstr + ";\n");
                }
                if (changeSM) {
                    dos2.writeBytes("permission " + "java.lang.RuntimePermission \"createSecurityManager\";\n");
                    dos2.writeBytes("permission " + "java.lang.RuntimePermission \"setSecurityManager\";\n");
                    dos2.writeBytes("permission " + "java.util.PropertyPermission \"user.home\", \"read\";\n");
                    String tmp = System.getProperty("user.home") + java.io.File.separator + "." + policyFile + "";
                    tmp = tmp.replace(java.io.File.separatorChar, '=');
                    tmp = tmp.replaceAll("=", "==");
                    tmp = tmp.replace('=', java.io.File.separatorChar);
                    dos2.writeBytes("permission " + "java.io.FilePermission \"" + tmp + "\", \"read,delete,write\";\n");
                    tmp = System.getProperty("user.home") + java.io.File.separator + "." + policyFile + ".copiedBySMT";
                    tmp = tmp.replace(java.io.File.separatorChar, '=');
                    tmp = tmp.replaceAll("=", "==");
                    tmp = tmp.replace('=', java.io.File.separatorChar);
                    dos2.writeBytes("permission " + "java.io.FilePermission \"" + tmp + "\", \"read,delete\";\n");
                    dos2.writeBytes("permission " + "java.util.PropertyPermission \"java.security.policy\", \"write\";\n");
                    dos2.writeBytes("permission " + "java.security.SecurityPermission \"getPolicy\";\n");
                    dos2.writeBytes("permission " + "java.security.SecurityPermission \"setProperty.policy.url.1\";\n");
                }
                dos2.writeBytes("};\n");
                dos2.flush();
                dos2.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            java.security.Policy.getPolicy().refresh();
            java.security.Security.setProperty("policy.url.1", "file:${user.home}/." + policyFile + "");
            sm = new SecurityManager();
            System.setSecurityManager(sm);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
