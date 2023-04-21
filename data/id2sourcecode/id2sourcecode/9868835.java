        @Override
        public void run() {
            final BufferedReader stdIn = new BufferedReader(new InputStreamReader(Channels.newInputStream(new FileInputStream(FileDescriptor.in).getChannel())));
            try {
                for (; ; ) {
                    System.out.println("Press q ENTER ro quit...");
                    final String line = stdIn.readLine();
                    if ("q".equalsIgnoreCase(line)) {
                        break;
                    }
                }
                executeShutdownHooks();
                System.exit(1);
            } catch (final ClosedByInterruptException e) {
            } catch (final IOException e) {
                log.error("Error reading stdin: ", e);
            }
        }
