    @Test
    public void testConfirmationDigest() throws InvalidConfirmationException, NoSuchAlgorithmException {
        StringBuffer buffer = new StringBuffer();
        buffer.append(confirmation.getOwner().getEmail()).append(";");
        buffer.append(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(confirmation.getOwner().getCreated())).append(";");
        buffer.append(confirmation.getKey());
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] message = buffer.toString().getBytes();
        md.update(message);
        byte[] digest = md.digest();
        String digestAsString = new String(Hex.encodeHex(digest));
        Assert.assertEquals(digestAsString, confirmationService.confirmationDigest(confirmation));
    }
