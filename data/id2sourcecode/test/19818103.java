	private static void doSignCert2() throws MessageException {
		boolean save = getBoolean("save");
		boolean saveKey = getBoolean("saveKey");
		String subjectType = getString("subjectType");
		String pasteKey = getString("pasteKey");

		if (subjectType == null) {
			throw new MessageException("Invalid Subject Type");
		}
		PublicKey publicKey = null;
		PrivateKey privateKey = null;
		if (pasteKey != null && !pasteKey.isEmpty()) {
			byte[] keyData = Base64.decode(pasteKey);
			privateKey = CertUtil.parsePrivateKey(keyData);
			if (privateKey == null) {
				privateKey = CertUtil.parseOpenSSLKey(keyData);
			}
			// RSAPrivateCrtKey required
			publicKey = CertUtil.getPublicKey(privateKey);
		}

		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(get().keyAlg);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(get().keySize);
		int id = save ? 0 : -1;
		CertExt certExt = new CertExt();
		X500Name subjectDn = null;
		switch (subjectType) {
		case "new":
			if (publicKey == null) {
				KeyPair keyPair = kpg.generateKeyPair();
				publicKey = keyPair.getPublic();
				privateKey = keyPair.getPrivate();
			}
			break;
		case "exist":
			byte[] uploadCert = getUploadOrPaste("Cert", "Request or Certificate");
			if ("PKCS10".equals(getString("fileType"))) {
				PKCS10 request;
				try {
					request = new PKCS10(uploadCert);
				} catch (Exception e) {
					try {
						request = new PKCS10(Base64.decode(new String(uploadCert)));
					} catch (Exception e2) {
						throw new MessageException("Invalid PKCS10 Request");
					}
				}
				subjectDn = request.getSubjectName();
				if (publicKey == null) {
					publicKey = request.getSubjectPublicKeyInfo();
				}
			} else {
				X509Certificate cert = parseCert(uploadCert);
				subjectDn = getSubjectDn(cert);
				certExt = new CertExt(cert);
				if (publicKey == null) {
					publicKey = cert.getPublicKey();
				}
			}
			if (getBoolean("genNewKey")) {
				KeyPair keyPair = kpg.generateKeyPair();
				publicKey = keyPair.getPublic();
				privateKey = keyPair.getPrivate();
			}
			break;
		case "store":
			CertKey certKey = getKeyStore("subject_");
			X509Certificate cert = certKey.getCertificateChain()[0];
			subjectDn = getSubjectDn(cert);
			certExt = new CertExt(cert);
			if (publicKey == null) {
				publicKey = cert.getPublicKey();
				privateKey = certKey.getKey();
			}
			break;
		case "db":
			int reissue = getInt("reissue");
			if (reissue == 0) {
				throw new MessageException("No Certificate Selected");
			}
			if (getBoolean("replace")) {
				id = reissue;
			}
			CertItem certItem = CertForgeDB.getCert(reissue);
			cert = certItem.getCertificate();
			subjectDn = getSubjectDn(cert);
			certExt = new CertExt(cert);
			if (publicKey == null) {
				if (getBoolean("retainKey")) {
					publicKey = cert.getPublicKey();
					privateKey = certItem.getPrivateKey();
				} else {
					KeyPair keyPair = kpg.generateKeyPair();
					publicKey = keyPair.getPublic();
					privateKey = keyPair.getPrivate();
				}
			}
			break;
		default:
			throw new MessageException("Invalid Subject Type");
		}

		ArrayList<RdnEntry> rdnList = new ArrayList<>();
		if (subjectDn == null) {
			for (int i = 0; i < EMPTY_KEY_NAMES.length; i ++) {
				rdnList.add(new RdnEntry(EMPTY_KEY_NAMES[i], ""));
			}
		} else {
			List<RDN> rdns = subjectDn.rdns();
			for (int i = rdns.size() - 1; i >= 0; i --) {
				String[] keyValue = rdns.get(i).toString().split("=", 2);
				rdnList.add(new RdnEntry(keyValue[0], keyValue[1]));
			}
		}
		byte[] snBytes = Bytes.random(16);
		snBytes[0] &= 0x7f;
		snBytes[0] |= 0x40;
		PrivateKey[] privateKey_ = new PrivateKey[1];
		byte[] encodedSigner = getEncodedSigner(privateKey_);

		put("id", "" + id);
		put("sn", new BigInteger(snBytes).toString());
		put("publicKey", publicKey == null ? "" :
				Base64.encode(publicKey.getEncoded()));
		put("privateKey", privateKey == null ? "" :
				Base64.encode(privateKey.getEncoded()));
		put("signerKey", encodedSigner == null ? "" :
				Base64.encode(privateKey_[0].getEncoded()));
		put("encodedSigner", encodedSigner == null ? "" :
				Base64.encode(encodedSigner));
		put("notBefore", Time.toDateString(certExt.notBefore));
		put("notAfter", Time.toDateString(certExt.notAfter));
		put("ca", "" + certExt.ca);
		put("crl", certExt.crl);
		put("saveKey", "" + saveKey);
		forwardSignCert2(rdnList, certExt.usages, certExt.extUsages);
	}
