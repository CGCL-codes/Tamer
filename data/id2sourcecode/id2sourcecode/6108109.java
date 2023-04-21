    private void loadRc(String line, CommandContext context) {
        StringTokenizer tokenizer = new StringTokenizer(Core.getConfig().resolve(line));
        String command = tokenizer.nextToken();
        while (tokenizer.hasMoreTokens()) {
            String location = tokenizer.nextToken();
            try {
                URL url = new URL(location);
                InputStream in = url.openStream();
                CommandProcessor processor = new CommandProcessor(in, context.getOut(), Core.INSTANCE.getBundleContext(), location, false);
                processor.run();
            } catch (MalformedURLException e) {
                context.error(e);
            } catch (IOException e) {
                context.error(e);
            }
        }
    }
