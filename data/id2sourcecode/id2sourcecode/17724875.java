    public byte[] getDigest(OMDocument document, String digestAlgorithm) throws OMException {
        byte[] digest = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(9);
            Collection childNodes = getValidElements(document);
            dos.writeInt(childNodes.size());
            Iterator itr = childNodes.iterator();
            while (itr.hasNext()) {
                OMNode node = (OMNode) itr.next();
                if (node.getType() == OMNode.PI_NODE) dos.write(getDigest((OMProcessingInstruction) node, digestAlgorithm)); else if (node.getType() == OMNode.ELEMENT_NODE) dos.write(getDigest((OMElement) node, digestAlgorithm));
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
