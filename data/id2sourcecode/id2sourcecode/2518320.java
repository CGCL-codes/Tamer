    protected void processPage() throws HamboException {
        try {
            long msgnr = getLongParameter("msg", 0);
            String partid = getParameter("part");
            Part part = Messaging.getMailStorage(user_id).getMessage(msgnr);
            if (partid != null && !partid.trim().equals("")) {
                part = locatePart((Multipart) part.getContent(), PartId.valueOf(partid));
                response.setContentType(part.getContentType());
                OutputStream out = response.getOutputStream();
                InputStream source = part.getInputStream();
                int b;
                while ((b = source.read()) != -1) out.write(b);
            } else {
                response.setContentType("message/rfc822");
                part.writeTo(response.getOutputStream());
            }
        } catch (IOException err) {
            throw new HamboException("Failed to view attachment", err);
        } catch (MessagingException err) {
            throw new HamboException("Failed to view attachment", err);
        }
    }
