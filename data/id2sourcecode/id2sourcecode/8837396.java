    private LibrarySet<String> findCodeDependencies(String libPath, LibrarySet<String> srcClazzes) {
        LibrarySet<String> libEntries = new LibrarySet<String>();
        if ((libPath != null) && (new File(libPath).exists())) {
            File[] classFiles = ClazzUtil.getClassFiles(libPath);
            for (Iterator<LibrarySet<String>.Entry<String>> srcIterator = srcClazzes.iterator(); srcIterator.hasNext(); ) {
                LibrarySet<String>.Entry<String> srcEntry = srcIterator.next();
                for (File fileToParse : classFiles) {
                    try {
                        LibrarySet<String>.Entry<String> libEntry = null;
                        if (new ClazzUtil.JarFileFilter().accept(fileToParse)) {
                            JarFile jarFile = new JarFile(fileToParse);
                            if (jarFile.getEntry(srcEntry.toString()) != null) {
                                libEntry = libEntries.add(fileToParse.getAbsolutePath());
                            }
                        } else if (fileToParse.equals(srcEntry.toString())) {
                            libEntry = libEntries.add(fileToParse.getAbsolutePath());
                        }
                        if (libEntry != null) {
                            libEntry.addClassReference(srcEntry);
                        }
                    } catch (IOException e) {
                        writer.println("Error: Unable to read " + fileToParse + ", skipping the entry.");
                    }
                }
            }
        }
        return libEntries;
    }
