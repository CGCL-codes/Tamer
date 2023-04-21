    public Resource getMD5HashResource() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.write(subject.getContent().length());
            dos.write(predicate.getContent().length());
            dos.write(object.getContent().length());
            dos.writeBoolean(object instanceof Literal);
            dos.writeChars(subject.getContent());
            dos.writeChars(predicate.getContent());
            dos.writeChars(object.getContent());
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(baos.toByteArray());
            StringBuffer sb = new StringBuffer("urn:statement:md5:");
            for (int i = 0; i < bytes.length; i++) {
                int loNibble = bytes[i] & 0xf;
                int hiNibble = (bytes[i] >> 4) & 0xf;
                sb.append(byteChars.charAt(hiNibble));
                sb.append(byteChars.charAt(loNibble));
            }
            return new Resource(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
