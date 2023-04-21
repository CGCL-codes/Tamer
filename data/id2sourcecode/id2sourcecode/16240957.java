    public static void extractZip(String zip) {
        try {
            if ("windows".equalsIgnoreCase(Compilador.getSo())) {
                String dirName = System.getProperty("java.io.tmpdir");
                tempFolder = new File(dirName + "javac");
            } else if ("linux".equalsIgnoreCase(Compilador.getSo())) {
                String dirName = System.getProperty("user.home");
                tempFolder = new File(dirName + Compilador.getFileSeparator() + "javac");
            }
            if (!tempFolder.exists()) {
                tempFolder.mkdir();
            } else {
                UnZip.deleteFile(UnZip.tempFolder);
            }
            Compilador.setDirJavac(tempFolder.getCanonicalPath() + Compilador.getFileSeparator() + "bin" + Compilador.getFileSeparator());
            tempFolder.deleteOnExit();
            ZipFile archive = new ZipFile(zip);
            Enumeration e = archive.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File file = new File(tempFolder, entry.getName());
                if (entry.isDirectory() && !file.exists()) {
                    file.mkdirs();
                } else {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    InputStream in = archive.getInputStream(entry);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] buffer = new byte[8192];
                    int read;
                    while (-1 != (read = in.read(buffer))) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    out.close();
                }
            }
            archive.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PseutemView.mainPanel, "Error al extraer los archivos necesarios para el funcionamiento de la aplicación:\n\n" + ex, "Error Zip.", JOptionPane.ERROR_MESSAGE);
        }
    }
