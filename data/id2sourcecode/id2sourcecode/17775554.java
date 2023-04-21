    public void writeContextToXml(InteractionContext context, File file) {
        if (context.getInteractionHistory().isEmpty()) return;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String handleIdentifier = context.getHandleIdentifier();
            String encoded = URLEncoder.encode(handleIdentifier, InteractionContextManager.CONTEXT_FILENAME_ENCODING);
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(file));
            ZipEntry zipEntry = new ZipEntry(encoded + InteractionContextManager.CONTEXT_FILE_EXTENSION_OLD);
            outputStream.putNextEntry(zipEntry);
            outputStream.setMethod(ZipOutputStream.DEFLATED);
            writer.setOutputStream(outputStream);
            writer.writeContextToStream(context);
            outputStream.closeEntry();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            StatusHandler.fail(e, "Could not write: " + file.getAbsolutePath(), true);
        }
    }
