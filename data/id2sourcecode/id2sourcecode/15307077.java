	public static String toMD5(String origin) throws RuntimeException{
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("md5");
		}
		byte[] results = digest.digest(origin.getBytes());
		String md5String = toHex(results);
		return md5String;
	}
