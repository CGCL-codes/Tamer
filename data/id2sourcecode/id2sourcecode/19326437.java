    private IELO createCurnit(String filename) {
        IELO curnit = new BasicELO();
        IContent content = curnit.getContent();
        URL url = CreateDefaultRooloLOROtmlCurnits.class.getResource(filename);
        try {
            FileInputStream fis;
            fis = new FileInputStream(new File(url.toURI()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            fis.close();
            content.setBytes(baos.toByteArray());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IMetadata metadata1 = curnit.getMetadata();
        IMetadataValueContainer container;
        container = metadata1.getMetadataValueContainer(ELOMetadataKeys.TITLE.getKey());
        container.setValue(filename);
        container = metadata1.getMetadataValueContainer(ELOMetadataKeys.TYPE.getKey());
        container.setValue("Curnit");
        container = metadata1.getMetadataValueContainer(ELOMetadataKeys.DESCRIPTION.getKey());
        container.setValue("This is a test curnit based on a curnit");
        container = metadata1.getMetadataValueContainer(ELOMetadataKeys.AUTHOR.getKey());
        container.setValue("tony p");
        container = metadata1.getMetadataValueContainer(ELOMetadataKeys.FAMILYTAG.getKey());
        container.setValue("TELS");
        container = metadata1.getMetadataValueContainer(ELOMetadataKeys.ISCURRENT.getKey());
        container.setValue("yes");
        return curnit;
    }
