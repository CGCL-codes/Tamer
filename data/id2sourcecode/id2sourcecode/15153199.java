    public void EmailPart(Part part, String path) throws IOException, MessagingException {
        Object object = part.getContent();
        if (object instanceof String) {
            log.debug("Instance of a string");
            log.debug("Type of part: " + part.getContentType());
            log.debug("Disposition: " + part.getDisposition());
            log.debug("Description: " + part.getDescription());
            log.debug("FileName: " + part.getFileName());
            log.debug("Content :\n" + part.getContent());
            File tmpFile;
            String extension = "";
            if (part.getFileName() == null) {
                extension = part.getContentType().substring(part.getContentType().indexOf("/") + 1);
                if (extension.indexOf(";") != -1) {
                    extension = extension.substring(0, extension.indexOf(";"));
                }
                if (extension.equalsIgnoreCase("plain")) extension = "txt";
                tmpFile = new File(path + "/" + attachmentCount + "." + extension);
                if (first) {
                    body = path + "/" + attachmentCount + "." + extension;
                    first = false;
                }
            } else {
                if (part.getFileName().indexOf(".") == -1) {
                    extension = "txt";
                    tmpFile = new File(path + "/" + part.getFileName() + "." + extension);
                    if (first) {
                        body = path + "/" + part.getFileName() + "." + extension;
                        first = false;
                    }
                } else {
                    extension = part.getFileName().substring(part.getFileName().lastIndexOf(".") + 1);
                    if (extension.equalsIgnoreCase("plain")) extension = "txt";
                    tmpFile = new File(path + "/" + part.getFileName().substring(0, part.getFileName().lastIndexOf(".") + 1) + extension);
                    if (first) {
                        body = path + "/" + part.getFileName().substring(0, part.getFileName().lastIndexOf(".") + 1) + extension;
                        first = false;
                    }
                }
            }
            ReadWriteTextFile.setContents(new File(tmpFile.getAbsolutePath()), part.getContent().toString(), true);
            xml += attachmentprocessing.runScript(tmpFile.getAbsolutePath(), extension);
            attachmentCount++;
        } else if (object instanceof Multipart) {
            log.debug("-------------");
            log.debug("Instance of a multipart");
            log.debug("Type of part: " + part.getContentType());
            log.debug("Disposition: " + part.getDisposition());
            log.debug("Description: " + part.getDescription());
            log.debug("FileName: " + part.getFileName());
            log.debug("Content:\n" + part.getContent());
            log.debug("-------------");
            Multipart mp = (Multipart) object;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                EmailPart(mp.getBodyPart(i), path);
            }
        } else if (object instanceof InputStream) {
            log.debug("-------------");
            log.debug("Instance of a inputstream");
            log.debug("Type of part: " + part.getContentType());
            log.debug("Disposition: " + part.getDisposition());
            log.debug("Description: " + part.getDescription());
            log.debug("FileName: " + part.getFileName());
            log.debug("Content:\n" + part.getContent());
            log.debug("-------------");
            InputStream is = (InputStream) object;
            File tmpFile;
            String extension = "";
            if (part.getFileName() == null) {
                extension = part.getContentType().substring(part.getContentType().indexOf("/") + 1);
                if (extension.indexOf(";") != -1) {
                    extension = extension.substring(0, extension.indexOf(";"));
                }
                if (extension.equalsIgnoreCase("plain")) extension = "txt";
                tmpFile = new File(path + "/" + attachmentCount + "." + extension);
                if (first) {
                    body = path + "/" + attachmentCount + "." + extension;
                    first = false;
                }
            } else {
                if (part.getFileName().indexOf(".") == -1) {
                    extension = "txt";
                    tmpFile = new File(path + "/" + part.getFileName() + "." + extension);
                } else {
                    extension = part.getFileName().substring(part.getFileName().lastIndexOf(".") + 1);
                    if (extension.equalsIgnoreCase("plain")) extension = "txt";
                    tmpFile = new File(path + "/" + part.getFileName().substring(0, part.getFileName().lastIndexOf(".") + 1) + extension);
                    if (first) {
                        body = path + "/" + part.getFileName().substring(0, part.getFileName().lastIndexOf(".") + 1) + extension;
                        first = false;
                    }
                }
            }
            int count;
            byte data[] = new byte[1000000];
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFile), 1000000);
            while ((count = is.read(data, 0, 1000000)) != -1) out.write(data, 0, count);
            out.flush();
            out.close();
            xml += attachmentprocessing.runScript(tmpFile.getAbsolutePath(), extension);
            attachmentCount++;
        } else if (object instanceof Message) {
            log.debug("-------------");
            log.debug("Instance of a message");
            log.debug("Type of part: " + part.getContentType());
            log.debug("Disposition: " + part.getDisposition());
            log.debug("Description: " + part.getDescription());
            log.debug("FileName: " + part.getFileName());
            log.debug("Content:\n" + part.getContent());
            log.debug("-------------");
            Message message = (Message) object;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                message.writeTo(os);
                ReadWriteTextFile.setContents(new File(path + "/" + part.getFileName()), os.toString(), true);
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
            }
            EmailPart((Message) object, path);
        }
    }
