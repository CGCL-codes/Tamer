    private void digestReference(DOMReference ref, XMLSignContext signContext) throws XMLSignatureException {
        if (ref.isDigested()) {
            return;
        }
        String uri = ref.getURI();
        if (Utils.sameDocumentURI(uri)) {
            String id = Utils.parseIdFromSameDocumentURI(uri);
            if (id != null && signatureIdMap.containsKey(id)) {
                Object obj = signatureIdMap.get(id);
                if (obj instanceof DOMReference) {
                    digestReference((DOMReference) obj, signContext);
                } else if (obj instanceof Manifest) {
                    Manifest man = (Manifest) obj;
                    List manRefs = man.getReferences();
                    for (int i = 0, size = manRefs.size(); i < size; i++) {
                        digestReference((DOMReference) manRefs.get(i), signContext);
                    }
                }
            }
            if (uri.length() == 0) {
                List transforms = ref.getTransforms();
                for (int i = 0, size = transforms.size(); i < size; i++) {
                    Transform transform = (Transform) transforms.get(i);
                    String transformAlg = transform.getAlgorithm();
                    if (transformAlg.equals(Transform.XPATH) || transformAlg.equals(Transform.XPATH2)) {
                        return;
                    }
                }
            }
        }
        ref.digest(signContext);
    }
