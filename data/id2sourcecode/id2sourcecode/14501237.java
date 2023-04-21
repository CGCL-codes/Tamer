    @Override
    public QualifyingProperty verify(CompleteRevocationRefsData propData, QualifyingPropertyVerificationContext ctx) throws InvalidPropertyException {
        Collection<X509CRL> crls = ctx.getCertChainData().getCrls();
        Collection<CRLRef> crlRefs = new ArrayList<CRLRef>(propData.getCrlRefs());
        if (crls.isEmpty()) throw new CompleteRevocRefsCRLsNotAvailableException();
        for (X509CRL crl : crls) {
            CRLRef match = null;
            for (CRLRef crlRef : crlRefs) {
                if (!crl.getIssuerX500Principal().equals(new X500Principal(crlRef.issuerDN)) || !crl.getThisUpdate().equals(crlRef.issueTime.getTime())) continue;
                try {
                    if (crlRef.serialNumber != null) {
                        BigInteger crlNum = CrlExtensionsUtils.getCrlNumber(crl);
                        if (crlNum != null && !crlRef.serialNumber.equals(crlNum)) continue;
                    }
                    MessageDigest md = this.digestEngineProvider.getEngine(crlRef.digestAlgUri);
                    if (Arrays.equals(md.digest(crl.getEncoded()), crlRef.digestValue)) {
                        match = crlRef;
                        break;
                    }
                } catch (IOException ex) {
                    throw new CompleteRevocRefsReferenceException(crl, ex.getMessage());
                } catch (CRLException ex) {
                    throw new CompleteRevocRefsReferenceException(crl, ex.getMessage());
                } catch (UnsupportedAlgorithmException ex) {
                    throw new CompleteRevocRefsReferenceException(crl, ex.getMessage());
                }
            }
            if (null == match) throw new CompleteRevocRefsReferenceException(crl, "no matching reference");
            crlRefs.remove(match);
        }
        return new CompleteRevocationRefsProperty(crls);
    }
