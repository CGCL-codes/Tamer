    public void load() {
        new Thread() {

            @Override
            public void run() {
                try {
                    URL url = null;
                    if (src.startsWith("http://") || src.startsWith("file:")) url = new URL(src); else url = new URL(Swinger.getInstance().getContext().getBaseUrl() + '/' + src);
                    InputStream in = url.openStream();
                    Script script = shell.parse(in);
                    in.close();
                    Binding binding = new Binding();
                    binding.setVariable("swinger", Swinger.getInstance());
                    script.setBinding(binding);
                } catch (Exception e) {
                } finally {
                }
            }
        }.start();
    }
