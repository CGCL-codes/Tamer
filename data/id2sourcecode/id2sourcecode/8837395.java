    private void parseSource(String path) {
        if ((path != null) && (new File(path).exists())) {
            File[] classFiles = ClazzUtil.getClassFiles(path);
            for (File fileToParse : classFiles) {
                try {
                    if (new ClazzUtil.JarFileFilter().accept(fileToParse)) {
                        JarFile jarFile = new JarFile(fileToParse);
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            if (new ClazzUtil.ClassFileFilter().accept(jarEntry.getName())) {
                                InputStream is = jarFile.getInputStream(jarEntry);
                                totalEntries.addAll(poolReader.getClassesInUse(is));
                            }
                        }
                    } else {
                        FileInputStream fis = new FileInputStream(fileToParse);
                        totalEntries.addAll(poolReader.getClassesInUse(fis));
                    }
                } catch (IOException e) {
                    writer.println("Error: Unable to read " + fileToParse + ", skipping the entry.");
                }
            }
        } else {
            writer.println("Skipping invalid entry: " + path);
        }
    }
