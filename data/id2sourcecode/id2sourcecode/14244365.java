    public void testMain() throws Exception {
        System.out.println("main");
        final boolean LOG = false;
        String fileName = "src/test/java/big.json";
        String[] urls = new String[] { "http://json-schema.org/schema", "http://json-schema.org/hyper-schema", "http://json-schema.org/json-ref", "http://json-schema.org/interfaces", "http://json-schema.org/geo", "http://json-schema.org/card", "http://json-schema.org/calendar", "http://json-schema.org/address", fileName };
        CacheingURLResolver uriResolver = new CacheingURLResolver();
        boolean canConnect = true;
        try {
            InputStream is = new URL(urls[0]).openStream();
            is.close();
        } catch (ConnectException cex) {
            for (int i = 2; i < (urls.length - 1); i++) {
                String url = urls[i];
                uriResolver.register(new URL(url), new File("./src/test/java/" + url.replace(":", "_").replace("/", "_") + ".schema.json"));
            }
            canConnect = true;
        } catch (Exception ex) {
            canConnect = false;
        }
        boolean scanSchemas = canConnect;
        boolean testValidation = true;
        boolean testLoadSave = canConnect;
        boolean testSelfValidation = canConnect;
        if (scanSchemas) {
            String url = urls[5];
            JSONSchema schema = new JSONSchema(new BufferingReader(uriResolver.resolveURL((new URL(url)), "UTF-8").getReader()) {

                @Override
                public int read() throws IOException {
                    int c = super.read();
                    if (LOG) {
                        System.out.print((char) c);
                        System.out.flush();
                    }
                    return c;
                }
            });
            StringWriter wr = new StringWriter();
            schema.save(wr, true);
            if (LOG) {
                System.out.println(wr.toString());
            }
        }
        if (testValidation) {
            Object sample = JSONParserLite.parse(new BufferingReader(uriResolver.resolveURL((new File(fileName)).toURI().toURL(), "UTF-8").getReader()));
            {
                long start = System.nanoTime();
                SizeInfo si = Utilities.estimateObjectSize(sample, null);
                long end = System.nanoTime();
                System.out.println(MessageFormat.format("  Building schema for {1} (evaluated in {0,number,#####.## ms})" + ((LOG) ? ":\n{2}" : ""), ((end - start) / 1000000.0), fileName, (LOG) ? si : null));
            }
            JSONSchema schema = new JSONSchema();
            JSONSchemaBuilder jsb = null;
            int maxTrials = 100;
            long start = System.nanoTime();
            for (int i = 0; i < maxTrials; i++) {
                jsb = new JSONSchemaBuilder(schema, "big", sample, null);
            }
            double avg = (System.nanoTime() - start) / 1000000.0 / maxTrials;
            StringWriter wr = new StringWriter();
            start = System.nanoTime();
            schema.save(wr, true);
            double avg2 = (System.nanoTime() - start) / 1000000.0;
            start = System.nanoTime();
            P.PP P = new P.PP();
            P.set(schema.getSchema(), "properties", "offers", "items", "properties", "___externalId", "minimum", 0);
            P.set(schema.getSchema(), "properties", "offers", "items", "properties", "___externalId", "maximum", 1639);
            P.set(schema.getSchema(), "properties", "offers", "items", "properties", "___externalId", "exclusiveMaximum", true);
            P.set(schema.getSchema(), "properties", "offers", "items", "minItems", 1000);
            P.set(schema.getSchema(), "properties", "offers", "items", "maxItems", 5);
            P.set(schema.getSchema(), "properties", "offers", "items", "uniqueItems", true);
            P.set(schema.getSchema(), "properties", "offers", "items", "properties", "description", "minLength", 500);
            P.set(schema.getSchema(), "properties", "offers", "items", "properties", "description", "maxLength", 1600);
            P.set(schema.getSchema(), "properties", "offers", "items", "properties", "description", "disallow", new JSONValueType(JSONValueType.__NUMBER));
            P.set(schema.getSchema(), "properties", "places", "items", "properties", "___externalId", "minimum", 0);
            P.set(schema.getSchema(), "properties", "places", "items", "properties", "___externalId", "exclusiveMinimum", true);
            P.set(schema.getSchema(), "properties", "places", "items", "properties", "___externalId", "divisibleBy", 2.0);
            P.set(schema.getSchema(), "properties", "places", "items", "properties", "ZZZ", new JSchema());
            P.set(schema.getSchema(), "properties", "places", "items", "properties", "ZZZ", "default", 1999);
            P.set(schema.getSchema(), "properties", "places", "items", "uniqueItems", false);
            P.set(schema.getSchema(), "properties", "associations", "items", "properties", "___toResolvedVia", "enum", new String[] { "id", "aa" });
            P.set(schema.getSchema(), "properties", "associations", "items", "properties", "___modelRef", "enum", new int[] { 18, 19, 20, 35, 22, 32, 33, 21, 27, 17, 25, 30 });
            wr = new StringWriter();
            schema.save(wr, true);
            System.out.println(MessageFormat.format("  Generated subSchema for {0} in {1,number,###.##} (average out of {2} trials), printed in {4,number,###.##} ms" + ((LOG) ? ":\n {3}" : ""), fileName, avg, maxTrials, (LOG) ? wr.toString() : null, avg2));
            for (int i = 0; i < 20; i++) {
            }
            P.delete(sample, "offers", 0, "___externalId");
            P.set(sample, "offers", 1, "pluginId", new ArrayList());
            P.set(sample, "offers", 1, "___externalId", -3);
            P.set(sample, "offers", 2, "description", 456);
            P.set(sample, "places", 1, "___externalId", 0);
            List l = P.getList(sample, "offers");
            Object o = l.get(3);
            l.add(o);
            l.add(o);
            l.add(o);
            l.add(o);
            JSONValidationContext vctx = new JSONValidationContext();
            vctx.setAssignMissingDefaults(true);
            boolean hasErrors = schema.validate(vctx, schema.getSchema(), sample, true);
            List<ValidationError> errors = vctx.getValidationErrors();
            double avg3 = (System.nanoTime() - start) / 1000000.0;
            System.out.println(MessageFormat.format("  Validated in {1,number,###.##} ms, result = {0}, errors={2}.", hasErrors, avg3, errors.size()));
            System.out.println(vctx.getSummary());
            assertEquals(15, errors.size());
        }
        if (testLoadSave) {
            for (int i = 0; i < urls.length; i++) {
                Object o1 = JSONParserLite.parse(new BufferingReader(uriResolver.resolveURL(Utilities.asURL(urls[i]), "UTF-8").getReader()));
                if (LOG) {
                    System.out.println(Formatter.toJSONString(o1, false));
                }
                JSONSchema schema = null;
                try {
                    schema = new JSONSchema(uriResolver.resolveURL(Utilities.asURL(urls[i]), "UTF-8").getReader());
                } catch (JSONSchemaException jsex) {
                    continue;
                }
                StringWriter wr = new StringWriter();
                schema.save(wr);
                if (LOG) {
                    System.out.println(wr.toString());
                }
                Object o2 = JSONParserLite.parse(new BufferingReader(new StringReader(wr.toString())));
                Comparer cmp = new Comparer();
                ComparatorPair cp = cmp.compare(new ComparatorContext(), o1, o2, 100);
                if (cp.getStatus().equals(COMPARE_STATUS.match)) {
                    if (LOG) {
                        System.out.println("Loaded " + urls[i] + " and interpreted JSON schemas match.");
                    }
                } else {
                    System.out.println("\nDelta between loaded (" + urls[i] + ") and interpreted schema:\n" + cmp.dumpComparatorPair(cp, "", true));
                    System.out.println("ORIGINA/LOADEDL:\n" + Formatter.toJSONString(o1, true) + "\n" + Formatter.toJSONString(o2, true));
                    fail("Incomplete schema loading.");
                }
            }
        }
        if (testSelfValidation) {
            JSONSchema schema = new JSONSchema(new BufferingReader(uriResolver.resolveURL(Utilities.asURL(urls[0]), "UTF-8").getReader()));
            for (int i = 0; i < 8; i++) {
                Object obj = JSONParserLite.parse(new BufferingReader(uriResolver.resolveURL(Utilities.asURL(urls[i]), "UTF-8").getReader()));
                JSONValidationContext ctx = new JSONValidationContext();
                if (!schema.validate(obj)) {
                    System.out.println("  self-validation FAILED for " + urls[i]);
                    for (ValidationError err : ctx.getValidationErrors()) {
                        System.out.println("  " + err.getMessage());
                    }
                    fail("JSON schema self-validation failed for reference schema " + urls[i]);
                } else {
                    System.out.println("  self-validation PASSED for " + urls[i]);
                }
            }
        }
    }
