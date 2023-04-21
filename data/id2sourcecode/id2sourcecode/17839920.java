    CodeSource performUrlConversion(CodeSource cs, boolean extractSignerCerts) {
        String path = null;
        CodeSource parsedCodeSource = null;
        URL locationURL = cs.getLocation();
        if (locationURL != null) {
            Permission urlAccessPermission = null;
            try {
                urlAccessPermission = locationURL.openConnection().getPermission();
            } catch (IOException e) {
            }
            if (urlAccessPermission != null && urlAccessPermission instanceof FilePermission) {
                path = ((FilePermission) urlAccessPermission).getName();
            } else if ((urlAccessPermission == null) && (locationURL.getProtocol().equals("file"))) {
                path = locationURL.getFile().replace("/", File.separator);
            } else {
            }
        }
        if (path == null) {
            if (extractSignerCerts) {
                parsedCodeSource = new CodeSource(cs.getLocation(), getSignerCertificates(cs));
            }
        } else {
            try {
                if (path.endsWith("*")) {
                    path = path.substring(0, path.length() - 1);
                    boolean removeTrailingFileSep = false;
                    if (path.endsWith(File.separator)) removeTrailingFileSep = true;
                    if (path.equals("")) {
                        path = System.getProperty("user.dir");
                    }
                    File f = new File(path);
                    path = f.getCanonicalPath();
                    StringBuffer sb = new StringBuffer(path);
                    if (!path.endsWith(File.separator) && removeTrailingFileSep) sb.append(File.separator);
                    sb.append('*');
                    path = sb.toString();
                } else if (path.endsWith(File.separator)) {
                    path = path.substring(0, path.length() - 1);
                    if (path.endsWith("!")) {
                        path = path.substring(0, path.length() - 1);
                    }
                } else {
                    path = new File(path).getCanonicalPath();
                }
                locationURL = fileToEncodedURL(new File(path));
                if (extractSignerCerts) {
                    parsedCodeSource = new CodeSource(locationURL, getSignerCertificates(cs));
                } else {
                    parsedCodeSource = new CodeSource(locationURL, cs.getCertificates());
                }
            } catch (IOException ioe) {
                if (extractSignerCerts) {
                    parsedCodeSource = new CodeSource(cs.getLocation(), getSignerCertificates(cs));
                }
            }
        }
        return parsedCodeSource;
    }
