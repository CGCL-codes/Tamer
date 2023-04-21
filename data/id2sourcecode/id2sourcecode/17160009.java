    private void addMsg(StringWriter xmlStringWriter, SmsPdu smsPdu, SmsAddress dest, SmsAddress sender) throws SmsException {
        SmsUserData userData = smsPdu.getUserData();
        xmlStringWriter.write("<MSG>\r\n");
        xmlStringWriter.write("<RCPREQ>Y</RCPREQ>\r\n");
        switch(smsPdu.getDcs().getAlphabet()) {
            case SmsDcs.ALPHABET_UCS2:
                xmlStringWriter.write("<OP>9</OP>\r\n");
                xmlStringWriter.write("<TEXT>");
                xmlStringWriter.write(StringUtil.bytesToHexString(userData.getData()));
                xmlStringWriter.write("</TEXT>\r\n");
                break;
            case SmsDcs.ALPHABET_GSM:
                xmlStringWriter.write("<TEXT>");
                xmlStringWriter.write(SmsPduUtil.readSeptets(userData.getData(), userData.getLength()));
                xmlStringWriter.write("</TEXT>\r\n");
                break;
            case SmsDcs.ALPHABET_8BIT:
                xmlStringWriter.write("<OP>8</OP>\r\n");
                xmlStringWriter.write("<TEXT>");
                xmlStringWriter.write(StringUtil.bytesToHexString(smsPdu.getUserDataHeaders()) + StringUtil.bytesToHexString(userData.getData()));
                xmlStringWriter.write("</TEXT>\r\n");
                break;
            default:
                throw new SmsException("Unsupported alphabet");
        }
        xmlStringWriter.write("<RCV>");
        xmlStringWriter.write(dest.getAddress());
        xmlStringWriter.write("</RCV>\r\n");
        if (sender != null) {
            xmlStringWriter.write("<SND>");
            xmlStringWriter.write(sender.getAddress());
            xmlStringWriter.write("</SND>\r\n");
        }
        if (smsPdu.getDcs().getMessageClass() == SmsDcs.MSG_CLASS_0) {
            xmlStringWriter.write("<CLASS>");
            xmlStringWriter.write("0");
            xmlStringWriter.write("</CLASS>\r\n");
        }
        xmlStringWriter.write("</MSG>\r\n");
    }
