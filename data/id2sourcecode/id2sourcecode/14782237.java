    public Object source(URL url) throws EvalError, IOException {
        return eval(new BufferedReader(new InputStreamReader(url.openStream())), this.getNameSpace(), "URL: " + url.toString());
    }
