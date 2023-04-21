    public static void addAttachmentFile(Element att, File attFile) throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        BufferedInputStream fileIs = new BufferedInputStream(new FileInputStream(attFile));
        int count = 0;
        byte[] buffer = new byte[8192];
        while ((count = fileIs.read(buffer)) != -1) {
            data.write(buffer, 0, count);
        }
        data.close();
        att.addElement("data").addText(new String(Base64.encodeBase64(data.toByteArray()), "UTF-8"));
    }
