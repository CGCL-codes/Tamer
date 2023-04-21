    public void writeResults(Sitios newSitio) {
        if (newSitio != null && newSitio.getSitio() != null) {
            String currHost = newSitio.getSitio();
            int trys = 0;
            boolean done = false;
            while (!done && trys < Constants.DEFAULT_MAXWRITETRYS) {
                try {
                    sessRestarter();
                    if (encrypt) {
                        currHost = Utils.getHash(currHost);
                        newSitio.setSitio(currHost);
                    }
                    Sitios oldSitio = (Sitios) session.createCriteria(Sitios.class).add(Restrictions.eq(Sitios.NAME_sitio, currHost)).uniqueResult();
                    if (oldSitio == null) {
                        oldSitio = new Sitios();
                        oldSitio.setSitio(currHost);
                        oldSitio.copySitios(newSitio);
                        session.save(oldSitio);
                    } else {
                        oldSitio.copySitios(newSitio);
                        session.flush();
                    }
                    done = true;
                } catch (Exception e) {
                    treatException(e, currHost);
                } catch (ThreadDeath err) {
                    try {
                        session.clear();
                        session.close();
                    } catch (Throwable t) {
                        SimpleLog.getInstance().writeLog(3, "Erro para limpar sessão durante" + " ThreadDeath.");
                    }
                    session = null;
                    session = new HibernateFactory().getSession();
                    System.out.println("Passou aqui!");
                    throw err;
                }
                trys++;
            }
        }
    }
