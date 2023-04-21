    public void addAttachment(TolvenMessageWithAttachments tm, File attachment) throws IOException {
        FileInputStream fi = new FileInputStream(attachment);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buf[] = new byte[100];
        while (true) {
            int count = fi.read(buf);
            if (count < 0) break;
            baos.write(buf, 0, count);
        }
        fi.close();
        TolvenMessageAttachment tma = new TolvenMessageAttachment();
        tma.setPayload(baos.toByteArray());
        tma.setDescription("Attachment");
        if (attachment.getName().endsWith(".gif")) {
            tma.setMediaType("image/gif");
        }
        if (attachment.getName().endsWith(".jpg") || attachment.getName().endsWith(".jpeg")) {
            tma.setMediaType("image/jpeg");
        }
        if (attachment.getName().endsWith(".png") || attachment.getName().endsWith(".png")) {
            tma.setMediaType("image/png");
        }
        if (attachment.getName().endsWith(".tif") || attachment.getName().endsWith(".tiff")) {
            tma.setMediaType("image/tiff");
        }
        tm.getAttachments().add(tma);
    }
