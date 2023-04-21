    public byte[] getDigest(OMElement element, String digestAlgorithm) throws OMException {
        byte[] digest = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(1);
            dos.write(getExpandedName(element).getBytes("UnicodeBigUnmarked"));
            dos.write((byte) 0);
            dos.write((byte) 0);
            Collection attrs = getAttributesWithoutNS(element);
            dos.writeInt(attrs.size());
            Iterator itr = attrs.iterator();
            while (itr.hasNext()) dos.write(getDigest((OMAttribute) itr.next(), digestAlgorithm));
            OMNode node = element.getFirstOMChild();
            int length = 0;
            itr = element.getChildElements();
            while (itr.hasNext()) {
                length++;
                itr.next();
            }
            dos.writeInt(length);
            while (node != null) {
                dos.write(getDigest(node, digestAlgorithm));
                node = node.getNextOMSibling();
            }
            dos.close();
            md.update(baos.toByteArray());
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new OMException(e);
        } catch (IOException e) {
            throw new OMException(e);
        }
        return digest;
    }
