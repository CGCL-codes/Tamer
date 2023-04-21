    private void collectDrivingLicenseData() {
        DriverDemographicInfo ddi = new DriverDemographicInfo(driverDataPanel.getValue("family"), driverDataPanel.getValue("names"), driverDataPanel.getValue("birth"), driverDataPanel.getValue("issue"), driverDataPanel.getValue("expiry"), driverDataPanel.getValue("country"), driverDataPanel.getValue("authority"), driverDataPanel.getValue("number"));
        List<CategoryInfo> l = driverDataPanel.getCategories(false);
        dl.putFile(DrivingLicenseService.EF_DG1, new DG1File(ddi, l).getEncoded());
        l = driverDataPanel.getCategories(true);
        if (l.size() > 0) {
            dl.putFile(DrivingLicenseService.EF_DG11, new DG11File(l).getEncoded());
        }
        if (driverDataPanel.getValue("gender") != null) {
            try {
                int gender = Integer.parseInt(driverDataPanel.getValue("gender"));
                int height = Integer.parseInt(driverDataPanel.getValue("height"));
                int weight = Integer.parseInt(driverDataPanel.getValue("weight"));
                String eye = driverDataPanel.getValue("eye");
                String hair = driverDataPanel.getValue("hair");
                String birthplace = driverDataPanel.getValue("birthplace");
                String residenceplace = driverDataPanel.getValue("residenceplace");
                dl.putFile(DrivingLicenseService.EF_DG2, new DG2File(gender, height, weight, eye, hair, birthplace, residenceplace).getEncoded(), eapDG2.isEnabled() && eapDG2.isSelected());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        if (driverDataPanel.getValue("adminnumber") != null) {
            try {
                int documentDisc = Integer.parseInt(driverDataPanel.getValue("documentdisc"));
                int dataDisc = Integer.parseInt(driverDataPanel.getValue("datadisc"));
                String adminNumber = driverDataPanel.getValue("adminnumber");
                byte[] idNumber = Hex.hexStringToBytes(driverDataPanel.getValue("isoid"));
                dl.putFile(DrivingLicenseService.EF_DG3, new DG3File(adminNumber, documentDisc, dataDisc, idNumber).getEncoded(), eapDG3.isEnabled() && eapDG3.isSelected());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        Iterator<PicturePane> it = pictures.iterator();
        ArrayList<FacePortrait> faces = new ArrayList<FacePortrait>();
        while (it.hasNext()) {
            PicturePane p = it.next();
            if (p.getImage() == null) {
                continue;
            }
            FacePortrait f = new FacePortrait(p.getImage(), p.getMimeType(), p.getDate());
            faces.add(f);
        }
        if (faces.size() > 0) {
            dl.putFile(DrivingLicenseService.EF_DG4, new DG4File(faces).getEncoded());
        }
        if (signature.getImage() != null) {
            dl.putFile(DrivingLicenseService.EF_DG5, new DG5File(signature.getImage(), signature.getMimeType()).getEncoded());
        }
        try {
            Provider provider = Security.getProvider("BC");
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", provider);
            generator.initialize(new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4));
            dl.setAAKeys(generator.generateKeyPair());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not generate RSA keys for AA.");
        }
        if (cvCertificate != null) {
            KeyPair ecKeyPair = null;
            try {
                String preferredProvider = "BC";
                Provider provider = Security.getProvider(preferredProvider);
                KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", provider);
                generator.initialize(new ECGenParameterSpec(DrivingLicensePersoService.EC_CURVE_NAME));
                ecKeyPair = generator.generateKeyPair();
                dl.setEAPKeys(ecKeyPair);
                dl.setCVCertificate(cvCertificate);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("Could not generate EC keys for EAP.");
            }
        }
        if (certificate != null) {
            dl.setDocSigningCertificate(certificate);
        }
        if (privateKey != null) {
            DocumentSigner signer = new SimpleDocumentSigner(privateKey);
            if (certificate != null) {
                signer.setCertificate(certificate);
            }
            dl.setSigner(signer);
        }
        byte[] ks = getKeySeed();
        if (ks != null) {
            dl.setKeySeed(ks);
        }
    }
