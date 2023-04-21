    private static ByteArrayOutputStream replaceEntityMappings(ByteArrayOutputStream content, File entityMappingXML) throws IOException {
        JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(content.toByteArray()));
        ByteArrayOutputStream retval = new ByteArrayOutputStream();
        JarOutputStream tempJar = new JarOutputStream(retval);
        HashSet<String> insertedNames = new HashSet<String>();
        JarEntry jarEntry = jarInputStream.getNextJarEntry();
        while (jarEntry != null) {
            if (!insertedNames.contains(jarEntry.getName())) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = jarInputStream;
                if (jarEntry.getName().equals("META-INF/entity-mappings.xml")) {
                    jarEntry = new JarEntry("META-INF/entity-mappings.xml");
                    is = new FileInputStream(entityMappingXML);
                }
                byte[] data = new byte[30000];
                int numberread;
                while ((numberread = is.read(data)) != -1) {
                    baos.write(data, 0, numberread);
                }
                tempJar.putNextEntry(jarEntry);
                insertedNames.add(jarEntry.getName());
                tempJar.write(baos.toByteArray());
            }
            jarEntry = jarInputStream.getNextJarEntry();
        }
        tempJar.close();
        return retval;
    }
