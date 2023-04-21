    public void sign(XMLSignContext signContext) throws MarshalException, XMLSignatureException {
        if (signContext == null) {
            throw new NullPointerException("signContext cannot be null");
        }
        DOMSignContext context = (DOMSignContext) signContext;
        if (context != null) {
            marshal(context.getParent(), context.getNextSibling(), DOMUtils.getSignaturePrefix(context), context);
        }
        List allReferences = new ArrayList();
        signatureIdMap = new HashMap();
        signatureIdMap.put(id, this);
        signatureIdMap.put(si.getId(), si);
        List refs = si.getReferences();
        for (int i = 0, size = refs.size(); i < size; i++) {
            Reference ref = (Reference) refs.get(i);
            signatureIdMap.put(ref.getId(), ref);
        }
        for (int i = 0, size = objects.size(); i < size; i++) {
            XMLObject obj = (XMLObject) objects.get(i);
            signatureIdMap.put(obj.getId(), obj);
            List content = obj.getContent();
            for (int j = 0, csize = content.size(); j < csize; j++) {
                XMLStructure xs = (XMLStructure) content.get(j);
                if (xs instanceof Manifest) {
                    Manifest man = (Manifest) xs;
                    signatureIdMap.put(man.getId(), man);
                    List manRefs = man.getReferences();
                    for (int k = 0, msize = manRefs.size(); k < msize; k++) {
                        Reference ref = (Reference) manRefs.get(k);
                        allReferences.add(ref);
                        signatureIdMap.put(ref.getId(), ref);
                    }
                }
            }
        }
        allReferences.addAll(si.getReferences());
        for (int i = 0, size = allReferences.size(); i < size; i++) {
            DOMReference ref = (DOMReference) allReferences.get(i);
            digestReference(ref, signContext);
        }
        for (int i = 0, size = allReferences.size(); i < size; i++) {
            DOMReference ref = (DOMReference) allReferences.get(i);
            if (ref.isDigested()) {
                continue;
            }
            ref.digest(signContext);
        }
        Key signingKey = null;
        KeySelectorResult ksr = null;
        try {
            ksr = signContext.getKeySelector().select(ki, KeySelector.Purpose.SIGN, si.getSignatureMethod(), signContext);
            signingKey = ksr.getKey();
            if (signingKey == null) {
                throw new XMLSignatureException("the keySelector did not " + "find a signing key");
            }
        } catch (KeySelectorException kse) {
            throw new XMLSignatureException("cannot find signing key", kse);
        }
        byte[] val = null;
        try {
            val = ((DOMSignatureMethod) si.getSignatureMethod()).sign(signingKey, (DOMSignedInfo) si, signContext);
        } catch (InvalidKeyException ike) {
            throw new XMLSignatureException(ike);
        }
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "SignatureValue = " + val);
        }
        ((DOMSignatureValue) sv).setValue(val);
        this.localSigElem = sigElem;
        this.ksr = ksr;
    }
