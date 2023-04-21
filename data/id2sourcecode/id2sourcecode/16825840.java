        public static synchronized Properties getInstance(URL url) {
            switch(mode) {
                case BasicToken:
                    return new PropertiesImpl(url);
                case StreamingToken:
                    {
                        try {
                            PropertiesStreamingLexer lexer = new PropertiesStreamingLexer(url.openStream());
                            lexer.lex();
                            List<PropertiesToken> list = lexer.getList();
                            PropertiesImpl props = new PropertiesImpl();
                            new PropertiesParser(list, props).parse();
                            return props;
                        } catch (IOException x) {
                            x.printStackTrace();
                        }
                    }
                case Line:
                    {
                        try {
                            LineScanner lexer = new LineScanner(new InputStreamReader(url.openStream()));
                            PropertiesImpl props = new PropertiesImpl();
                            new PropertiesParser2(lexer, props).parse();
                            return props;
                        } catch (IOException x) {
                            x.printStackTrace();
                        }
                    }
            }
            return new PropertiesImpl(url);
        }
