    public Map<String, File> extractJarArchive(JLabel label) {
        tmpDir.mkdir();
        Map<String, File> files = new TreeMap<String, File>();
        File f = new File("");
        JarFile jarf;
        try {
            jarf = new JarFile(jar);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not open JAR file!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            Enumeration<JarEntry> enum1 = jarf.entries();
            while (enum1.hasMoreElements()) {
                JarEntry file = (JarEntry) enum1.nextElement();
                f = new File((tmpDir.getAbsolutePath() + File.separator + file.getName()).replaceAll("\\\\", "/"));
                label.setText("Extracting from JAR: " + file.getName());
                if (file.getName().indexOf("META-INF") != -1) continue;
                String[] sarr = file.getName().replaceAll("\\\\", "/").split("/");
                if (sarr.length == 1 && file.getName().contains("/")) continue;
                if (sarr.length > 1) {
                    String dirStr = new String();
                    for (int i = 0; i < sarr.length - 1; i++) {
                        dirStr += sarr[i];
                        if (i < sarr.length - 2) dirStr += "\\";
                    }
                    File dir = new File(tmpDir.getAbsolutePath() + File.separator + dirStr);
                    dir.mkdirs();
                }
                files.put((file.getName().replaceAll("\\\\", "/")), f);
                InputStream is = jarf.getInputStream(file);
                FileOutputStream fos = new java.io.FileOutputStream(f);
                while (is.available() > 0) {
                    fos.write(is.read());
                }
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not extract " + f.getAbsolutePath() + " from JAR file!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return files;
    }
