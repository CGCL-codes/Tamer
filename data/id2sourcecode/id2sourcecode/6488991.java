    public MessageListDTO receiveMessages(String filterBySender, String filterBySubject) {
        MessageListDTO ml = new MessageListDTO();
        SearchTerm st = new FromStringTerm(filterBySender);
        try {
            this.msgs = folder.search(st);
        } catch (MessagingException e) {
            e.printStackTrace();
            ml = new MessageListDTO();
            ml.setError(63);
            return ml;
        }
        try {
            for (Message m : msgs) {
                Address[] adrs = m.getFrom();
                String[] from = new String[adrs.length];
                int fromcounter = 0;
                for (Address a : adrs) {
                    from[fromcounter] = a.toString();
                    fromcounter++;
                }
                String subject = m.getSubject();
                String text = "";
                String contentType = m.getContentType();
                List<File> files = new ArrayList<File>();
                if (contentType.contains("text/plain")) {
                    text = (String) m.getContent();
                } else if (contentType.contains("multipart")) {
                    List<Part> parts = new ArrayList<Part>();
                    Multipart mp = (Multipart) m.getContent();
                    getParts(parts, mp);
                    for (Part part : parts) {
                        String disposition = part.getDisposition();
                        if (disposition != null) {
                            if ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))) {
                                String filename = part.getFileName();
                                File file = new File(filename);
                                for (int j = 0; file.exists(); j++) {
                                    file = new File(j + filename);
                                }
                                FileOutputStream out = new FileOutputStream(file);
                                BufferedOutputStream bos = new BufferedOutputStream(out);
                                BufferedInputStream bis = new BufferedInputStream(part.getInputStream());
                                byte[] data = new byte[1024];
                                int read;
                                while ((read = bis.read(data)) >= 0) {
                                    bos.write(data, 0, read);
                                }
                                bis.close();
                                bos.close();
                                files.add(file);
                            }
                        } else {
                            if (part.getContentType().contains("text/plain")) {
                                text = (String) part.getContent();
                            } else {
                                continue;
                            }
                        }
                    }
                } else {
                    continue;
                }
                ml.addMessageDTO(new MessageDTO(from, "", subject, files, text));
                ml.setError(0);
            }
        } catch (IOException e) {
            ml.setError(61);
            return ml;
        } catch (MessagingException e) {
            ml.setError(62);
            return ml;
        } catch (Exception e) {
            ml.setError(63);
            return ml;
        }
        return ml;
    }
