    protected byte[] engineSign() throws SignatureException {
        byte[] out = null;
        byte[] shaMBytes = md_.digest();
        byte[] plainSig = null;
        try {
            DigestInfo di = new DigestInfo(md5aid_, shaMBytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DEREncoder encoder = new DEREncoder(baos);
            di.encode(encoder);
            plainSig = baos.toByteArray();
            baos.close();
            out = cipher_.doFinal(plainSig);
            return out;
        } catch (ConstraintException ce) {
            System.out.println("internal error:");
            ce.printStackTrace();
        } catch (ASN1Exception ae) {
            System.out.println("internal error:");
            ae.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("internal error:");
            ioe.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("RSASignature: cipher.doFinal");
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            System.err.println("RSASignature: cipher.doFinal");
            bpe.printStackTrace();
        }
        return null;
    }
