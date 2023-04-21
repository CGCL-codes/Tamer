    private void sign() {
        if (from.compareTo(frame1.getMyId().getName()) == 0) {
            if (debug) System.out.println("signing message");
            text = new String(text + "<key>" + (frame1.getMyId()).getKeyAddress() + "</key>");
            if (encryptSign && recipient != null) {
                System.out.println("encrypting message");
                text = frame1.getCrypto().encryptSign(text, frame1.getMyId().getPrivKey(), recipient.getKey());
                subject = new String("ENCRYPTED MSG FOR : " + recipient.getStrippedName());
            } else text = frame1.getCrypto().sign(text, (frame1.getMyId()).getPrivKey());
            from = new String(from + "@" + frame1.getCrypto().digest(frame1.getMyId().getKey()));
            signed = true;
        }
    }
