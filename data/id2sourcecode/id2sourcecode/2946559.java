    public PermissionCollection getPermissions(X509Certificate cert) {
        if (cert == null) return null;
        String perm = null;
        String fileName = generateFilename(cert.getSerialNumber().toByteArray());
        if (fileName == null) return null;
        logger.info("Looking for cert security file:" + fileName);
        File f = getFile(fileName);
        if (f == null || f.exists() == false) {
            return null;
        }
        PermissionCollection perms = null;
        try {
            FileInputStream in = new FileInputStream(f);
            Document doc = docBuilder.parse(in);
            NodeList nodes = doc.getElementsByTagName("certificate");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element certE = (Element) nodes.item(i);
                String name = certE.getAttribute("name");
                String serialNum = new String(new BASE64Decoder().decodeBuffer(certE.getAttribute("serial-num")));
                if (!name.equals(cert.getSubjectX500Principal().getName())) continue;
                if (!serialNum.equals(new String(cert.getSerialNumber().toByteArray()))) continue;
                NodeList permElements = certE.getElementsByTagName("permission");
                perms = createPermissionsFromElements(permElements);
                if (perms != null) break;
            }
            in.close();
        } catch (Exception exp) {
            BootSecurityManager.securityLogger.log(Level.SEVERE, "Invalid stored permission for " + cert.getSubjectDN().toString(), exp);
        }
        return perms;
    }
