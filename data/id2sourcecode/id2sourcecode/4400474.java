    public static void main(String args[]) {
        FileInputStream in = null;
        System.setProperty("fedoraServerHost", "localhost");
        System.setProperty("fedoraServerPort", "80");
        try {
            if (args.length < 1) {
                throw new IOException("At least one parameter needed.");
            }
            in = new FileInputStream(new File(args[0]));
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
            System.out.println("Give the path to an existing METS file, and optionally, the level of validation to perform on the re-serialized version.");
            System.exit(0);
        }
        try {
            METSLikeDODeserializer deser = new METSLikeDODeserializer();
            METSLikeDOSerializer ser = new METSLikeDOSerializer();
            HashMap desers = new HashMap();
            HashMap sers = new HashMap();
            desers.put("metslikefedora1", deser);
            sers.put("metslikefedora1", ser);
            DOTranslatorImpl trans = new DOTranslatorImpl(sers, desers);
            DigitalObject obj = new BasicDigitalObject();
            System.out.println("Deserializing...");
            trans.deserialize(in, obj, "metslikefedora1", "UTF-8", DOTranslationUtility.DESERIALIZE_INSTANCE);
            System.out.println("Done.");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.out.println("Re-serializing for STORAGE...");
            trans.serialize(obj, out, "metslikefedora1", "UTF-8", DOTranslationUtility.SERIALIZE_STORAGE_INTERNAL);
            System.out.println("Done.");
            if (args.length > 1) {
                ByteArrayInputStream newIn = new ByteArrayInputStream(out.toByteArray());
                HashMap xmlSchemaMap = new HashMap();
                xmlSchemaMap.put("metslikefedora1", "dist/server/xsd/mets-fedora-ext.xsd");
                HashMap ruleSchemaMap = new HashMap();
                ruleSchemaMap.put("metslikefedora1", "dist/server/schematron/metsExtRules1-0.xml");
                DOValidatorImpl v = new DOValidatorImpl(null, xmlSchemaMap, "dist/server/schematron/preprocessor.xslt", ruleSchemaMap);
                if (args[1].equals("1")) {
                    v.validate(newIn, "metslikefedora1", DOValidatorImpl.VALIDATE_XML_SCHEMA, "ingest");
                    System.out.println("XML Schema validation: PASSED!");
                } else {
                    if (args[1].equals("2")) {
                        v.validate(newIn, "metslikefedora1", DOValidatorImpl.VALIDATE_SCHEMATRON, "ingest");
                        System.out.println("Schematron validation: PASSED!");
                    } else {
                        System.out.println("Unrecognized validation level, '" + args[1] + "'");
                    }
                }
            } else {
                System.out.println("Here it is:");
                System.out.println(out.toString("UTF-8"));
            }
        } catch (Exception e) {
            System.out.println("Error: (" + e.getClass().getName() + "):" + e.getMessage());
            e.printStackTrace();
        }
    }
