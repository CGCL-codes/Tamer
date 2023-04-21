    public static AddressFieldImpl createMessage(InputStream stm) throws MAPException {
        if (stm == null) throw new MAPException("Error creating AddressField: stream must not be null");
        AddressFieldImpl res = new AddressFieldImpl();
        try {
            int addressLength = stm.read();
            if (addressLength == -1) throw new MAPException("Error creating AddressField: Address-Length field not found");
            if (addressLength < 0 || addressLength > 20) throw new MAPException("Error creating AddressField: Address-Length field must be equal from 0 to 20, found: addressLength");
            int addressArrayLength = (addressLength + 1) / 2;
            int typeOfAddress = stm.read();
            if (typeOfAddress == -1) throw new MAPException("Error creating AddressField: Type-of-Address field not found");
            res.typeOfNumber = TypeOfNumber.getInstance((typeOfAddress & 0x70) >> 4);
            res.numberingPlanIdentification = NumberingPlanIdentification.getInstance(typeOfAddress & 0x0F);
            res.addressValue = TbcdString.decodeString(stm, addressArrayLength);
        } catch (IOException e) {
            throw new MAPException("IOException when creating AddressField: " + e.getMessage(), e);
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when creating AddressField: " + e.getMessage(), e);
        }
        return res;
    }
