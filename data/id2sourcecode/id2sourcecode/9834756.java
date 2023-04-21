    private IELO createCurnit(String uriName, URL otmlUrl) {
        IELO curnit = new BasicELO();
        IContent content = curnit.getContent();
        try {
            FileInputStream fis;
            fis = new FileInputStream(new File(otmlUrl.toURI()));
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
        IMetadata metadata = curnit.getMetadata();
        URI uri = null;
        try {
            uri = new URI(uriName);
            IMetadataValueContainer container = metadata.getMetadataValueContainer(createLongMetadataKey());
            container.setValue(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return curnit;
    }
