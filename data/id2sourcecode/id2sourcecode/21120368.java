    public PdfDictionary getEncryptionDictionary() {
        PdfDictionary dic = new PdfDictionary();
        if (publicKeyHandler.getRecipientsSize() > 0) {
            PdfArray recipients = null;
            dic.put(PdfName.FILTER, PdfName.PUBSEC);
            dic.put(PdfName.R, new PdfNumber(revision));
            try {
                recipients = publicKeyHandler.getEncodedRecipients();
            } catch (Exception f) {
                throw new ExceptionConverter(f);
            }
            if (revision == STANDARD_ENCRYPTION_40) {
                dic.put(PdfName.V, new PdfNumber(1));
                dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S4);
                dic.put(PdfName.RECIPIENTS, recipients);
            } else if (revision == STANDARD_ENCRYPTION_128 && encryptMetadata) {
                dic.put(PdfName.V, new PdfNumber(2));
                dic.put(PdfName.LENGTH, new PdfNumber(128));
                dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S4);
                dic.put(PdfName.RECIPIENTS, recipients);
            } else {
                dic.put(PdfName.R, new PdfNumber(AES_128));
                dic.put(PdfName.V, new PdfNumber(4));
                dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S5);
                PdfDictionary stdcf = new PdfDictionary();
                stdcf.put(PdfName.RECIPIENTS, recipients);
                if (!encryptMetadata) stdcf.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE);
                if (revision == AES_128) stdcf.put(PdfName.CFM, PdfName.AESV2); else stdcf.put(PdfName.CFM, PdfName.V2);
                PdfDictionary cf = new PdfDictionary();
                cf.put(PdfName.DEFAULTCRYPTFILTER, stdcf);
                dic.put(PdfName.CF, cf);
                if (embeddedFilesOnly) {
                    dic.put(PdfName.EFF, PdfName.DEFAULTCRYPTFILTER);
                    dic.put(PdfName.STRF, PdfName.IDENTITY);
                    dic.put(PdfName.STMF, PdfName.IDENTITY);
                } else {
                    dic.put(PdfName.STRF, PdfName.DEFAULTCRYPTFILTER);
                    dic.put(PdfName.STMF, PdfName.DEFAULTCRYPTFILTER);
                }
            }
            MessageDigest md = null;
            byte[] encodedRecipient = null;
            try {
                md = MessageDigest.getInstance("SHA-1");
                md.update(publicKeyHandler.getSeed());
                for (int i = 0; i < publicKeyHandler.getRecipientsSize(); i++) {
                    encodedRecipient = publicKeyHandler.getEncodedRecipient(i);
                    md.update(encodedRecipient);
                }
                if (!encryptMetadata) md.update(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 });
            } catch (Exception f) {
                throw new ExceptionConverter(f);
            }
            byte[] mdResult = md.digest();
            setupByEncryptionKey(mdResult, keyLength);
        } else {
            dic.put(PdfName.FILTER, PdfName.STANDARD);
            dic.put(PdfName.O, new PdfLiteral(PdfContentByte.escapeString(ownerKey)));
            dic.put(PdfName.U, new PdfLiteral(PdfContentByte.escapeString(userKey)));
            dic.put(PdfName.P, new PdfNumber(permissions));
            dic.put(PdfName.R, new PdfNumber(revision));
            if (revision == STANDARD_ENCRYPTION_40) {
                dic.put(PdfName.V, new PdfNumber(1));
            } else if (revision == STANDARD_ENCRYPTION_128 && encryptMetadata) {
                dic.put(PdfName.V, new PdfNumber(2));
                dic.put(PdfName.LENGTH, new PdfNumber(128));
            } else if (revision == AES_256) {
                if (!encryptMetadata) dic.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE);
                dic.put(PdfName.OE, new PdfLiteral(PdfContentByte.escapeString(oeKey)));
                dic.put(PdfName.UE, new PdfLiteral(PdfContentByte.escapeString(ueKey)));
                dic.put(PdfName.PERMS, new PdfLiteral(PdfContentByte.escapeString(perms)));
                dic.put(PdfName.V, new PdfNumber(revision));
                dic.put(PdfName.LENGTH, new PdfNumber(256));
                PdfDictionary stdcf = new PdfDictionary();
                stdcf.put(PdfName.LENGTH, new PdfNumber(32));
                if (embeddedFilesOnly) {
                    stdcf.put(PdfName.AUTHEVENT, PdfName.EFOPEN);
                    dic.put(PdfName.EFF, PdfName.STDCF);
                    dic.put(PdfName.STRF, PdfName.IDENTITY);
                    dic.put(PdfName.STMF, PdfName.IDENTITY);
                } else {
                    stdcf.put(PdfName.AUTHEVENT, PdfName.DOCOPEN);
                    dic.put(PdfName.STRF, PdfName.STDCF);
                    dic.put(PdfName.STMF, PdfName.STDCF);
                }
                stdcf.put(PdfName.CFM, PdfName.AESV3);
                PdfDictionary cf = new PdfDictionary();
                cf.put(PdfName.STDCF, stdcf);
                dic.put(PdfName.CF, cf);
            } else {
                if (!encryptMetadata) dic.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE);
                dic.put(PdfName.R, new PdfNumber(AES_128));
                dic.put(PdfName.V, new PdfNumber(4));
                dic.put(PdfName.LENGTH, new PdfNumber(128));
                PdfDictionary stdcf = new PdfDictionary();
                stdcf.put(PdfName.LENGTH, new PdfNumber(16));
                if (embeddedFilesOnly) {
                    stdcf.put(PdfName.AUTHEVENT, PdfName.EFOPEN);
                    dic.put(PdfName.EFF, PdfName.STDCF);
                    dic.put(PdfName.STRF, PdfName.IDENTITY);
                    dic.put(PdfName.STMF, PdfName.IDENTITY);
                } else {
                    stdcf.put(PdfName.AUTHEVENT, PdfName.DOCOPEN);
                    dic.put(PdfName.STRF, PdfName.STDCF);
                    dic.put(PdfName.STMF, PdfName.STDCF);
                }
                if (revision == AES_128) stdcf.put(PdfName.CFM, PdfName.AESV2); else stdcf.put(PdfName.CFM, PdfName.V2);
                PdfDictionary cf = new PdfDictionary();
                cf.put(PdfName.STDCF, stdcf);
                dic.put(PdfName.CF, cf);
            }
        }
        return dic;
    }
