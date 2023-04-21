    public static String downloadWithChecksum(URL url, OutputStream checksumTarget, OutputStream downloadTarget, boolean requireChecksum) throws IOException, NoSuchAlgorithmException {
        URL checksumURL = null;
        String extension = null;
        for (String cse : new String[] { "sha1", "md5" }) {
            checksumURL = new URL(url.toExternalForm() + "." + cse);
            if (URLUtil.exists(checksumURL)) {
                extension = cse;
                break;
            }
        }
        if (checksumURL == null && requireChecksum) throw new RuntimeException("Required checksum not found for: " + url);
        if (checksumURL == null) {
            IOUtil.transfer(url.openStream(), downloadTarget);
        } else {
            ByteArrayOutputStream checksumOut = new ByteArrayOutputStream();
            IOUtil.transfer(checksumURL.openStream(), checksumTarget == null ? checksumOut : new IOUtil.MultiplexedOutputStream(checksumOut, checksumTarget));
            ChecksumUtil.ChecksumInputStream input = new ChecksumUtil.ChecksumInputStream(url.openStream(), ChecksumUtil.getAlgorithmFromExtension(extension));
            String checksumRaw = new String(checksumOut.toByteArray());
            String checksum = parseChecksumLine(checksumRaw);
            if (checksum == null) throw new RuntimeException("Checksum invalid: " + checksumRaw);
            IOUtil.transfer(input, downloadTarget);
            String calculated = ((ChecksumUtil.ChecksumInputStream) input).getChecksumHexed();
            if (!checksum.equals(calculated)) throw new RuntimeException("Checksum did not match: " + calculated + "/" + checksumRaw);
        }
        return ChecksumUtil.getAlgorithmFromExtension(extension);
    }
