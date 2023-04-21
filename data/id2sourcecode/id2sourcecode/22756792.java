    public static ArchiveFiduciaryMediaSet[] newAFMSet(ArchiveFiduciaryMediaRequest afmReq, ArchiveFiduciaryMediaIssuancePolicy issuancePolicy, GeneralNames afmSeriesId, DERInteger serialNumber, X509CertificateObject caCert, KeyPair caKeyPair, KeyPair seriesKP) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance(LtansUtils.DIGEST_ALGORITHM);
        Random rnd = new Random();
        ArrayList digestList = new ArrayList<byte[]>();
        ArrayList afmuList = new ArrayList<ArchiveFiduciaryMediaUnit>();
        long currenttime = System.currentTimeMillis();
        BigInteger duration = issuancePolicy.getDuration();
        Time sTime = new Time(new Date(currenttime));
        Time eTime = new Time(new Date(currenttime + (duration.intValue() * 1000)));
        int certNum = issuancePolicy.getCertificateNumber().intValue();
        ArchiveFiduciaryMediaUnitTemplate[] afmUnitTemplateArray = new ArchiveFiduciaryMediaUnitTemplate[certNum];
        for (int x = 0; x < certNum; x++) {
            BigInteger issuerBigInt = new BigInteger(64, rnd);
            BigInteger subjectBigInt = new BigInteger(64, rnd);
            ArchiveFiduciaryMediaUnitTemplate afmUnitTemplate = new ArchiveFiduciaryMediaUnitTemplate(new DERInteger(x + 1), new X509Name(caCert.getIssuerDN().toString()), afmSeriesId, sTime, eTime, new DERBitString(issuerBigInt.toByteArray()), new DERBitString(subjectBigInt.toByteArray()), null, seriesKP.getPublic());
            afmUnitTemplateArray[x] = afmUnitTemplate;
            byte[] encAfmIssuerBytes = afmUnitTemplate.getIssuerDataBytes();
            byte[] digestBytes = sha256.digest(encAfmIssuerBytes);
            digestList.add(digestBytes);
        }
        Signature treeSig = Signature.getInstance(LtansUtils.SIGNATURE_ALGORITHM);
        treeSig.initSign(seriesKP.getPrivate());
        MerkleTree mt = MerkleTree.buildTree(digestList);
        int treeHeight = mt.getTreeHeight();
        byte[] rootDigest = mt.getRootDigest();
        treeSig.update(rootDigest);
        byte[] digestSig = treeSig.sign();
        int afmuNum = afmuList.size();
        DERBitString issuerTreeSigValue = new DERBitString(digestSig);
        int q = 0;
        while (Math.pow(2, q) < digestList.size()) {
            q++;
        }
        AlgorithmIdentifier issuerTreeSigAlg = new AlgorithmIdentifier(new DERObjectIdentifier(AFMObjectIdentifiers.sha256_rsa_tree_signature_oid + "." + q + ""));
        ArchiveFiduciaryMediaUnit[] distSet = new ArchiveFiduciaryMediaUnit[certNum];
        for (int x = 0; x < certNum; x++) {
            distSet[x] = new ArchiveFiduciaryMediaUnit(afmUnitTemplateArray[x], issuerTreeSigAlg, issuerTreeSigValue, seriesKP);
        }
        ASN1EncodableVector vect = new ASN1EncodableVector();
        vect.add(new DERInteger(0));
        vect.add(serialNumber);
        vect.add(new X509Name(caCert.getIssuerDN().toString()));
        ASN1EncodableVector tmpSeq = new ASN1EncodableVector();
        tmpSeq.add(sTime);
        tmpSeq.add(eTime);
        vect.add(new DERSequence(tmpSeq));
        vect.add(afmSeriesId);
        vect.add(new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption), caKeyPair.getPublic().getEncoded()));
        ASN1EncodableVector treeSigSeqV = new ASN1EncodableVector();
        ASN1EncodableVector digestListV = new ASN1EncodableVector();
        for (int w = 0; w < digestList.size(); w++) {
            digestListV.add(new DERBitString((byte[]) digestList.get(w)));
        }
        treeSigSeqV.add(new DERInteger(0));
        treeSigSeqV.add(new DERSequence(digestListV));
        treeSigSeqV.add(issuerTreeSigAlg);
        treeSigSeqV.add(issuerTreeSigValue);
        ASN1EncodableVector vOne = new ASN1EncodableVector();
        vOne.add(new DERObjectIdentifier(AFMObjectIdentifiers.id_afm_series_descriptor));
        vOne.add(new DERBoolean(true));
        DEROctetString treeSigStr = new DEROctetString(LtansUtils.getDERObjectBytes(new DERSequence(treeSigSeqV)));
        vOne.add(treeSigStr);
        ASN1EncodableVector vtwo = new ASN1EncodableVector();
        vtwo.add(new DERSequence(vOne));
        X509Extensions exts = X509Extensions.getInstance(new DERSequence(vtwo));
        vect.add(exts);
        TBSCertificateStructure tbsCert = new TBSCertificateStructure(new DERSequence(vect));
        byte[] tbsCertBytes = LtansUtils.getDERObjectBytes(tbsCert);
        Signature certSig = Signature.getInstance(LtansUtils.SIGNATURE_ALGORITHM);
        certSig.initSign(seriesKP.getPrivate());
        certSig.update(tbsCertBytes);
        byte[] tbsSigBytes = certSig.sign();
        DERBitString derBitStr = new DERBitString(tbsSigBytes);
        ASN1EncodableVector vTBSVect = new ASN1EncodableVector();
        vTBSVect.add(tbsCert);
        vTBSVect.add(new AlgorithmIdentifier(LtansUtils.defaultSignatureOID));
        vTBSVect.add(derBitStr);
        X509CertificateStructure x509CertStr = new X509CertificateStructure(new DERSequence(vTBSVect));
        ArchiveFiduciaryMediaSet releasedAfmSet = new ArchiveFiduciaryMediaSet(distSet, new DERSet(x509CertStr), seriesKP);
        ArchiveFiduciaryMediaSet[] afmSet = new ArchiveFiduciaryMediaSet[1];
        afmSet[0] = releasedAfmSet;
        return afmSet;
    }
