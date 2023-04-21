    public void preClose(HashMap exclusionSizes) throws IOException, DocumentException {
        if (preClosed) throw new DocumentException("Document already pre closed.");
        preClosed = true;
        AcroFields af = writer.getAcroFields();
        String name = getFieldName();
        boolean fieldExists = !(isInvisible() || isNewField());
        if (fieldExists) {
            af.removeField(name);
        }
        writer.setSigFlags(3);
        PdfFormField sigField = PdfFormField.createSignature(writer);
        sigField.setFieldName(name);
        PdfIndirectReference refSig = writer.getPdfIndirectReference();
        sigField.put(PdfName.V, refSig);
        sigField.setFlags(132);
        int pagen = getPage();
        PdfReader reader = writer.reader;
        if (!isInvisible()) {
            sigField.setWidget(getPageRect(), null);
            sigField.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, getAppearance());
        } else sigField.setWidget(new Rectangle(0, 0), null);
        sigField.setPage(pagen);
        writer.addAnnotation(sigField, pagen);
        exclusionLocations = new HashMap();
        if (cryptoDictionary == null) {
            if (PdfName.ADOBE_PPKLITE.equals(getFilter())) sigStandard = new PdfSigGenericPKCS.PPKLite(getProvider()); else if (PdfName.ADOBE_PPKMS.equals(getFilter())) sigStandard = new PdfSigGenericPKCS.PPKMS(getProvider()); else if (PdfName.VERISIGN_PPKVS.equals(getFilter())) sigStandard = new PdfSigGenericPKCS.VeriSign(getProvider()); else throw new IllegalArgumentException("Unknown filter: " + getFilter());
            sigStandard.setExternalDigest(externalDigest, externalRSAdata, digestEncryptionAlgorithm);
            if (getReason() != null) sigStandard.setReason(getReason());
            if (getLocation() != null) sigStandard.setLocation(getLocation());
            if (getContact() != null) sigStandard.setContact(getContact());
            sigStandard.put(PdfName.M, new PdfDate(getSignDate()));
            sigStandard.setSignInfo(getPrivKey(), getCertChain(), getCrlList());
            PdfString contents = (PdfString) sigStandard.get(PdfName.CONTENTS);
            PdfLiteral lit = new PdfLiteral(contents.toString().length() * 2 + 2);
            exclusionLocations.put(PdfName.CONTENTS, lit);
            sigStandard.put(PdfName.CONTENTS, lit);
            lit = new PdfLiteral(80);
            exclusionLocations.put(PdfName.BYTERANGE, lit);
            sigStandard.put(PdfName.BYTERANGE, lit);
            if (signatureEvent != null) signatureEvent.getSignatureDictionary(sigStandard);
            writer.addToBody(sigStandard, refSig, false);
        } else {
            PdfLiteral lit = new PdfLiteral(80);
            exclusionLocations.put(PdfName.BYTERANGE, lit);
            cryptoDictionary.put(PdfName.BYTERANGE, lit);
            for (Iterator it = exclusionSizes.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                PdfName key = (PdfName) entry.getKey();
                Integer v = (Integer) entry.getValue();
                lit = new PdfLiteral(v.intValue());
                exclusionLocations.put(key, lit);
                cryptoDictionary.put(key, lit);
            }
            if (signatureEvent != null) signatureEvent.getSignatureDictionary(cryptoDictionary);
            writer.addToBody(cryptoDictionary, refSig, false);
        }
        writer.close(stamper.getMoreInfo());
        range = new int[exclusionLocations.size() * 2];
        int byteRangePosition = ((PdfLiteral) exclusionLocations.get(PdfName.BYTERANGE)).getPosition();
        exclusionLocations.remove(PdfName.BYTERANGE);
        int idx = 1;
        for (Iterator it = exclusionLocations.values().iterator(); it.hasNext(); ) {
            PdfLiteral lit = (PdfLiteral) it.next();
            int n = lit.getPosition();
            range[idx++] = n;
            range[idx++] = lit.getPosLength() + n;
        }
        Arrays.sort(range, 1, range.length - 1);
        for (int k = 3; k < range.length - 2; k += 2) range[k] -= range[k - 1];
        if (tempFile == null) {
            bout = sigout.getBuffer();
            boutLen = sigout.size();
            range[range.length - 1] = boutLen - range[range.length - 2];
            ByteBuffer bf = new ByteBuffer();
            bf.append('[');
            for (int k = 0; k < range.length; ++k) bf.append(range[k]).append(' ');
            bf.append(']');
            System.arraycopy(bf.getBuffer(), 0, bout, byteRangePosition, bf.size());
        } else {
            try {
                raf = new RandomAccessFile(tempFile, "rw");
                int boutLen = (int) raf.length();
                range[range.length - 1] = boutLen - range[range.length - 2];
                ByteBuffer bf = new ByteBuffer();
                bf.append('[');
                for (int k = 0; k < range.length; ++k) bf.append(range[k]).append(' ');
                bf.append(']');
                raf.seek(byteRangePosition);
                raf.write(bf.getBuffer(), 0, bf.size());
            } catch (IOException e) {
                try {
                    raf.close();
                } catch (Exception ee) {
                }
                try {
                    tempFile.delete();
                } catch (Exception ee) {
                }
                throw e;
            }
        }
    }
