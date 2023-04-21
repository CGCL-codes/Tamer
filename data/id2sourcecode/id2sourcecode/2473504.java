    public boolean sendImage(Molecule data) throws Exception {
        OutputStream os, os2 = null;
        InputStream is, is2 = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        StreamConnection con = null;
        GZIPOutputStream gzout = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        if (data != null) {
            System.out.println(data);
        } else {
            data = new NewXMLtoMolecule(new File("xml/VX.xml")).getMolecule();
        }
        try {
            con = (StreamConnection) Connector.open(serverConnectionString);
            os = con.openOutputStream();
            is = con.openInputStream();
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            bos = new ByteArrayOutputStream();
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(512, SecureRandom.getInstance("SHA1PRNG"));
            AlgorithmParameters params = paramGen.generateParameters();
            DHParameterSpec dhSpec = (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
            keyGen.initialize(dhSpec);
            KeyPair keypair = keyGen.generateKeyPair();
            System.out.println("KeyPair generated.");
            PrivateKey privateKey = keypair.getPrivate();
            PublicKey publicKey = keypair.getPublic();
            System.out.println(publicKey.getFormat());
            PublicKey rempub = null;
            byte[] ourpublicKeyBytes = publicKey.getEncoded();
            byte[] theirpublicKeyBytes = null;
            System.out.println("Our: " + toHexString(ourpublicKeyBytes));
            oos.writeObject(publicKey);
            Thread.sleep(1000);
            System.out.println("Our public was sent.");
            theirpublicKeyBytes = ((PublicKey) ois.readObject()).getEncoded();
            System.out.println("The: " + toHexString(theirpublicKeyBytes));
            System.out.println("Remote public key is read.");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(theirpublicKeyBytes);
            KeyFactory keyFact = KeyFactory.getInstance("DH");
            rempub = keyFact.generatePublic(x509KeySpec);
            DHParameterSpec paramspec = ((DHPublicKey) rempub).getParams();
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(rempub, true);
            String algorithm = "TripleDES";
            SecretKey secretKey = ka.generateSecret(algorithm);
            System.out.println(toHexString(secretKey.getEncoded()));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getEncoded(), algorithm);
            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.ENCRYPT_MODE, skeySpec);
            oos.flush();
            oos.close();
            os.flush();
            os.close();
            os2 = con.openOutputStream();
            oos = new ObjectOutputStream(os2);
            long ttim = System.currentTimeMillis();
            NewMoleculetoXML mxml = new NewMoleculetoXML(data);
            org.jdom.Document doc = mxml.getDocument();
            XMLOutputter out = new XMLOutputter();
            SealedObject so = new SealedObject(doc, c);
            oos.writeObject(so);
            oos.flush();
            System.out.println("Time taken:" + (System.currentTimeMillis() - ttim));
            oos.flush();
            oos.close();
            os2.flush();
            os2.close();
            con.close();
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        } catch (java.security.InvalidKeyException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        System.out.println("The image was sent");
        return true;
    }
