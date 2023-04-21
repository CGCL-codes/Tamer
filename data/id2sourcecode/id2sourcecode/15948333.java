    public static void main(String[] args) {
        try {
            if (!(args.length == 3 || args.length == 4)) {
                System.out.print(usageStr);
                System.exit(1);
            }
            issuanceProps = new Properties();
            issuanceProps.loadFromXML(new FileInputStream(args[0]));
            int loglevel = -1;
            try {
                int tmpInt = new Integer(issuanceProps.getProperty("client_loglevel")).intValue();
                if (tmpInt > 0 || tmpInt < 6) loglevel = tmpInt;
            } catch (java.lang.Exception e) {
            }
            URL certURL = new URL(args[1]);
            File tmpFile = File.createTempFile("issuance", ".url");
            System.out.println(" writing url " + certURL.toString() + " to file " + tmpFile.toURL().toString());
            FileOutputStream fos = new FileOutputStream(tmpFile);
            URLConnection certConnection = certURL.openConnection();
            InputStream certIS = certConnection.getInputStream();
            while (true) {
                int certInt = certIS.read();
                if (certInt == -1) break;
                fos.write(certInt);
            }
            FileInputStream fileFIS = new FileInputStream(tmpFile);
            MessageDigest md = MessageDigest.getInstance(issuanceProps.getProperty("digest_algorithm"));
            DigestInputStream dis = new DigestInputStream(fileFIS, md);
            fos.close();
            fos = new FileOutputStream("/dev/null");
            Utils.bufferedCopy(dis, fos);
            String digestValue = Utils.toHex(dis.getMessageDigest().digest());
            tmpFile.delete();
            String urlStr = certURL.toString();
            CertificationRequest certReq = new CertificationRequest();
            PrivateKey privKey = (PrivateKey) getPrivateKey(issuanceProps);
            certReq.addInformationIdentifier(new InformationIdentifier(urlStr, digestValue));
            String seriesTitle = null;
            if (args.length == 4 && args[3] != null) {
                certReq.addInformationIdentifier(new InformationIdentifier(args[3].trim()));
                seriesTitle = new String("Contribution of " + args[3].trim() + " to " + args[1] + ": " + args[2]);
            } else {
                seriesTitle = new String("" + args[1] + ": " + args[2] + "");
            }
            certReq.setTitle(seriesTitle);
            certReq.setNotAfter(new Date(System.currentTimeMillis() + 20000));
            certReq.setNotBefore(new Date());
            certReq.sign(privKey);
            if (loglevel > 3) System.out.println("CertificationRequest:\n" + certReq.toString() + "");
            if (!certReq.checkFields()) {
                System.out.println("Fields not filled!");
                return;
            }
            if (loglevel == 5) {
                File crFile = File.createTempFile("certification-request-", ".xml");
                java.io.FileWriter crWriter = new java.io.FileWriter(crFile);
                crWriter.write(certReq.toString());
                crWriter.close();
                System.out.println("Wrote certification request to " + crFile.getCanonicalPath() + ".");
            }
            String certResponse = IssuanceUtils.processCertificationRequest(issuanceProps, certReq.toString());
            if (certResponse == null) {
                System.out.println("No information currency series was received.");
            }
            if (certResponse != null) {
                InformationCurrencySeries ics = new InformationCurrencySeries(new ByteArrayInputStream(Utils.convertEntities(certResponse).getBytes()));
                try {
                    File outputFile = File.createTempFile("ics-", ".xml", new File(issuanceProps.getProperty("ics_output_directory")));
                    if (outputFile == null) {
                        System.out.println("Error writing information currency file.  Received information currency series is:");
                        System.out.println(ics.toString());
                        System.exit(1);
                    }
                    FileWriter fw = new FileWriter(outputFile);
                    fw.write("<!-- " + Utils.addEntities(seriesTitle) + " -->\n");
                    fw.write(ics.toString());
                    fw.flush();
                    fw.close();
                    System.out.println(" Wrote information currency series to " + outputFile.toString());
                    System.exit(0);
                } catch (java.lang.Exception e) {
                    System.out.println(" Error writing information currency file.  Received information currency series is:");
                    System.out.println(ics.toString());
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
