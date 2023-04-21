    private Serializable service(Serializable request) throws I18NException {
        URLConnection urlc = null;
        try {
            urlc = url.openConnection();
            urlc.setDoInput(true);
            urlc.setDoOutput(true);
            ObjectOutputStream out = new ObjectOutputStream(urlc.getOutputStream());
            out.writeObject(request);
            out.close();
            ObjectInputStream in = new ObjectInputStream(urlc.getInputStream());
            return ((Serializable) in.readObject());
        } catch (IOException exp) {
            Message[] reasons = new Message[1];
            reasons[0] = new Message(exp.getMessage());
            throw new I18NException(new Message("exception.request.failed"), reasons);
        } catch (ClassNotFoundException exp) {
            Message[] reasons = new Message[1];
            reasons[0] = new Message(exp.getMessage());
            throw new I18NException(new Message("exception.request.failed"), reasons);
        }
    }
