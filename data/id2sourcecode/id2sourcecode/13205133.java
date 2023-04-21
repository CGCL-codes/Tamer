    @Override
    public final TimeStampTokenRes getTimeStampToken(byte[] tsDigestInput, String digestAlgUri) throws TimeStampTokenGenerationException {
        try {
            MessageDigest md = messageDigestProvider.getEngine(digestAlgUri);
            byte[] digest = md.digest(tsDigestInput);
            TimeStampRequest tsRequest = this.tsRequestGenerator.generate(identifierForDigest(digestAlgUri), digest, BigInteger.valueOf(System.currentTimeMillis()));
            InputStream responseStream = getResponse(tsRequest.getEncoded());
            TimeStampResponse tsResponse = new TimeStampResponse(responseStream);
            if (tsResponse.getStatus() != PKIStatus.GRANTED && tsResponse.getStatus() != PKIStatus.GRANTED_WITH_MODS) {
                throw new TimeStampTokenGenerationException("Time stamp token not granted. " + tsResponse.getStatusString());
            }
            tsResponse.validate(tsRequest);
            TimeStampToken tsToken = tsResponse.getTimeStampToken();
            return new TimeStampTokenRes(tsToken.getEncoded(), tsToken.getTimeStampInfo().getGenTime());
        } catch (UnsupportedAlgorithmException ex) {
            throw new TimeStampTokenGenerationException("Digest algorithm not supported", ex);
        } catch (TSPException ex) {
            throw new TimeStampTokenGenerationException("Invalid time stamp response", ex);
        } catch (IOException ex) {
            throw new TimeStampTokenGenerationException("Encoding error", ex);
        }
    }
