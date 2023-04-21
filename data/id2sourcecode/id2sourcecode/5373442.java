    public void writeResults(Paginas newPage) {
        if (newPage.getPagina().length() >= 1000) newPage.setPagina(newPage.getPagina().substring(0, 1000));
        if (newPage != null && newPage.getPagina() != null) {
            String currPagina = newPage.getPagina();
            int tries = 0;
            boolean done = false;
            while (!done && tries < Constants.DEFAULT_MAXWRITETRYS) {
                tries++;
                try {
                    sessRestarter();
                    if (encrypt) {
                        currPagina = Utils.getHash(currPagina);
                        newPage.setPagina(currPagina);
                        if (newPage.getSitio() != null) {
                            newPage.setSitio(Utils.getHash(newPage.getSitio()));
                        }
                    }
                    Paginas oldPage = (Paginas) session.createCriteria(Paginas.class).add(Restrictions.eq(Paginas.NAME_paginas, currPagina)).uniqueResult();
                    if (oldPage == null) {
                        oldPage = new Paginas();
                        oldPage.setPagina(currPagina);
                        oldPage.copyPaginas(newPage);
                        session.save(oldPage);
                    } else {
                        oldPage.copyPaginas(newPage);
                        session.flush();
                    }
                    done = true;
                } catch (Exception e) {
                    treatException(e, currPagina);
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
            }
        }
    }
