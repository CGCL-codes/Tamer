            public void addPoints() throws InkMLComplianceException {
                Scanner stringScanner = new Scanner(input);
                Pattern pattern = Pattern.compile(",|F|T|\\*|\\?|[\"'!]?-?(\\.[0-9]+|[0-9]+\\.[0-9]+|[0-9]+)");
                int i = 0;
                while (true) {
                    String result = stringScanner.findWithinHorizon(pattern, input.length());
                    if (result == null) {
                        break;
                    }
                    if (result.equals(",") || i >= formatter.size()) {
                        next();
                        i = 0;
                        continue;
                    }
                    set(formatter.get(i).getChannel().getName(), formatter.get(i).consume(result));
                    i++;
                }
            }
