    private static void exportEntity(ZipOutputStream zipstream, Entity entity) throws Exception {
        ZipEntry entry = new ZipEntry(entity.getClass().getName() + "." + entity.id.getAsString());
        zipstream.putNextEntry(entry);
        BindableProperties bindable = new BindableProperties();
        entity.addFieldToBindable(bindable);
        bindable.save();
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        bindable.getProperties().store(byteout, "");
        Utils.readWrite(new ByteArrayInputStream(byteout.toByteArray()), zipstream, false);
    }
