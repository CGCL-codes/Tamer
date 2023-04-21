    public void copyDocsApps() {
        System.out.println("Attempting to pull files using copyDocsApps()");
        try {
            CodeSource src = GWrap_GUI_ClickMe.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                docsDirectory = new File(saveDirectory, "Documentation");
                docsDirectory.mkdir();
                appsDirectory = new File(saveDirectory, "Apps");
                appsDirectory.mkdir();
                URL jar = src.getLocation();
                System.out.println("src jar " + jar.getFile());
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                BufferedInputStream bis = new BufferedInputStream(zip);
                ZipEntry ze = null;
                FileOutputStream fos = null;
                DataOutputStream dos = null;
                int count;
                byte data[] = new byte[2048];
                while ((ze = zip.getNextEntry()) != null) {
                    String entryName = ze.getName();
                    if (entryName.startsWith("Documentation/")) {
                        String trimmedName = entryName.replace("Documentation/", "");
                        if (trimmedName.length() == 0) continue;
                        fos = new FileOutputStream(new File(docsDirectory, trimmedName));
                    } else if (entryName.startsWith("Apps/")) {
                        String trimmedName = entryName.replace("Apps/", "");
                        if (trimmedName.length() == 0) continue;
                        fos = new FileOutputStream(new File(appsDirectory, trimmedName));
                    } else fos = null;
                    if (fos != null) {
                        dos = new DataOutputStream(new BufferedOutputStream(fos));
                        while ((count = bis.read(data, 0, 2048)) != -1) dos.write(data, 0, count);
                        dos.close();
                        fos.close();
                    }
                }
                bis.close();
                zip.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
