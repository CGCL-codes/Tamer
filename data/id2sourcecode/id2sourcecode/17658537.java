    public boolean sendMessage(SendMail sm, File messageFile) {
        List addressees = sm.getAddressees();
        if (addressees == null || addressees.size() == 0) return false;
        if (!connect()) return false;
        try {
            setEcho(true);
            FastStringBuffer sb = new FastStringBuffer("mail from:<");
            sb.append(sm.getFromAddress());
            sb.append('>');
            writeLine(sb.toString());
            if (getResponse() != 250) return false;
            for (int i = 0; i < addressees.size(); i++) {
                String addressee = (String) addressees.get(i);
                String addr = sm.getAddress(addressee);
                if (addr == null) {
                    errorText = "Invalid addressee \"" + addressee + "\"";
                    return false;
                }
                sb.setText("rcpt to:<");
                sb.append(addr);
                sb.append('>');
                writeLine(sb.toString());
                if (getResponse() != 250) {
                    errorText = "Address not accepted \"" + addr + "\"";
                    return false;
                }
            }
            writeLine("data");
            if (getResponse() != 354) return false;
            setEcho(false);
            BufferedReader messageFileReader = new BufferedReader(new InputStreamReader(messageFile.getInputStream()));
            String s;
            while ((s = messageFileReader.readLine()) != null) writeLine(s);
            setEcho(true);
            writeLine(".");
            if (getResponse() != 250) return false;
            quit();
        } catch (Throwable t) {
            Log.error(t);
        } finally {
            setEcho(false);
            disconnect();
        }
        AddressBook addressBook = AddressBook.getGlobalAddressBook();
        for (int i = 0; i < addressees.size(); i++) {
            String addressee = (String) addressees.get(i);
            MailAddress a = MailAddress.parseAddress(addressee);
            if (a != null) {
                addressBook.maybeAddMailAddress(a);
                addressBook.promote(a);
            }
        }
        AddressBook.saveGlobalAddressBook();
        return true;
    }
