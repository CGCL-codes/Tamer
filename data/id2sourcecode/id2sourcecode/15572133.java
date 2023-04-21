    @Override
    public PropertyDataObject generatePropertyData(CompleteRevocationRefsProperty prop, PropertiesDataGenerationContext ctx) throws PropertyDataGenerationException {
        Collection<X509CRL> crls = prop.getCrls();
        Collection<CRLRef> crlRefs = new ArrayList<CRLRef>(crls.size());
        String digestAlgUri = this.algorithmsProvider.getDigestAlgorithmForReferenceProperties();
        try {
            MessageDigest messageDigest = this.messageDigestProvider.getEngine(digestAlgUri);
            for (X509CRL crl : crls) {
                GregorianCalendar crlTime = new GregorianCalendar();
                crlTime.setTime(crl.getThisUpdate());
                byte[] digest = messageDigest.digest(crl.getEncoded());
                BigInteger crlNum = CrlExtensionsUtils.getCrlNumber(crl);
                crlRefs.add(new CRLRef(crl.getIssuerX500Principal().getName(), crlNum, digestAlgUri, digest, crlTime));
            }
            return new CompleteRevocationRefsData(crlRefs);
        } catch (CRLException ex) {
            throw new PropertyDataGenerationException(prop, "cannot get encoded CRL", ex);
        } catch (IOException ex) {
            throw new PropertyDataGenerationException(prop, "cannot parse CRL number extension", ex);
        } catch (UnsupportedAlgorithmException ex) {
            throw new PropertyDataGenerationException(prop, ex.getMessage(), ex);
        }
    }
