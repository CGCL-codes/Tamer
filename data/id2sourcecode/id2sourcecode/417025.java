    private void displayPart(MailUserData mud, int msgNum, int partNum, ServletOutputStream out, HttpServletResponse res) throws IOException {
        Part part = null;
        try {
            Message msg = mud.getFolder().getMessage(msgNum);
            Multipart mp = (Multipart) msg.getContent();
            part = mp.getBodyPart(partNum);
            String sct = part.getContentType();
            if (sct == null) {
                out.println("invalid part");
                return;
            }
            ContentType ct = new ContentType(sct);
            res.setContentType(ct.getBaseType());
            InputStream is = part.getInputStream();
            int i;
            while ((i = is.read()) != -1) out.write(i);
            out.flush();
            out.close();
        } catch (MessagingException mex) {
            out.println(mex.toString());
        }
    }
