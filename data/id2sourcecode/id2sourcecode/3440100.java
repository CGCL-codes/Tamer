    protected List<Entity> dump(String kind) throws IOException, AuthoricationRequiredException {
        AppEngineEnv env = Kotan.get().getEnv();
        HttpHost target = new HttpHost(env.getHost(), env.getPort(), "http");
        HttpGet httpGet = new HttpGet("/_kotan/load?kind=" + kind);
        HttpResponse response = clientManager.httpClient.execute(target, httpGet);
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
            throw new AuthoricationRequiredException();
        }
        List<Entity> list = new ArrayList<Entity>();
        if (entity != null) {
            ObjectInputStream input = null;
            try {
                input = new ObjectInputStream(entity.getContent());
                while (true) {
                    Entity e = (Entity) input.readObject();
                    list.add(e);
                }
            } catch (java.io.EOFException e) {
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (input != null) input.close();
            }
        }
        return list;
    }
