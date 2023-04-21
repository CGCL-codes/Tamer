    @Override
    public void run() {
        String prompt = host.options.get("PROMPT") != null ? (String) host.options.get("PROMPT") : ".*\\$$";
        try {
            URL url = new URL("telnet", host.address, Integer.parseInt(host.port), "", new thor.net.URLStreamHandler());
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            ((TelnetURLConnection) urlConnection).setTelnetTerminalHandler(new SimpleTelnetHandler());
            OutputStream out = urlConnection.getOutputStream();
            InputStream in = urlConnection.getInputStream();
            login(in, out);
            output += command;
            out.write(command.getBytes());
            WaitFor rw = new WaitFor(in, prompt);
            output += rw.output;
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
