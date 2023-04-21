    @Override
    public String serve(HttpServletRequest request, HttpServletResponse response) throws FatalException {
        Tournament tournament = getTournament(request);
        if (tournament == null) return ServletCst.REDIRECT_TO_TOURNAMENT_LIST_SERVLET;
        HttpSession session = request.getSession();
        List<Player> players = (List<Player>) session.getAttribute(SessionCst.ATTR_RANKING);
        List<Team> teams = (List<Team>) session.getAttribute("rankingTeam");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipfile = new ZipOutputStream(out);
        String nameOfFile;
        StringBuilder buffer;
        Team teamPlayer, teamSession;
        int index;
        List<DisplayTableGame> displayTableGames;
        List<Table> tables;
        int nbRounds;
        String scoreGame;
        DisplayTableGame displayTableGame;
        for (Player player : players) {
            buffer = new StringBuilder("<html><head><title>");
            buffer.append(player.getPrettyPrintName());
            buffer.append("</title><link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\" /></head>");
            buffer.append("<body><table><tr><td>");
            buffer.append(player.getPrettyPrintName());
            buffer.append("</td><td>");
            buffer.append(player.getEma());
            buffer.append("</td></tr></table><table><tr><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.POSITION));
            buffer.append("</th><td>");
            buffer.append(player.getRank());
            buffer.append("</td></tr><tr><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.POINTS));
            buffer.append("</th><td>");
            buffer.append(player.getPoints());
            buffer.append("</td><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.SCORE));
            buffer.append("</th><td>");
            buffer.append(player.getScore());
            buffer.append("</td></tr><tr><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_GAMES));
            buffer.append("</th><td>");
            buffer.append(player.getNbGames());
            buffer.append("</td><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_SELFPICK_VICTORY));
            buffer.append("</th><td>");
            buffer.append(player.getNbWinAndSelfDraw());
            buffer.append("</td></tr><tr><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_SELFPICK));
            buffer.append("</th><td>");
            buffer.append(player.getSelfDraw());
            buffer.append("</td><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_VICTORY));
            buffer.append("</th><td>");
            buffer.append(player.getWin());
            buffer.append("</td></tr><tr><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_DEFEAT));
            buffer.append("</th><td>");
            buffer.append(player.getNbDefeatForRankingPage());
            buffer.append("</td><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_GIVEN));
            buffer.append("</th><td>");
            buffer.append(player.getLose());
            buffer.append("</td></tr><tr><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_SUSTAIN_SELFPICK));
            buffer.append("</th><td>");
            buffer.append(player.getNbSustainSelfpickForRankingPage());
            buffer.append("</td><th class=\"left\">");
            buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.NB_DRAW));
            buffer.append("</th><td>");
            buffer.append(player.getNbDrawForRankingPage());
            buffer.append("</td></tr></table>");
            teamPlayer = TeamService.getTeamForPlayer(player, tournament);
            if (teamPlayer != null) {
                if (teams != null) {
                    teamSession = null;
                    index = 1;
                    for (Team team : teams) {
                        if (team != null && team.getId() != null && team.getId().equals(teamPlayer.getId())) {
                            teamSession = team;
                            break;
                        }
                        index++;
                    }
                    if (teamSession != null) {
                        buffer.append("<table><tr><th>");
                        buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.TEAM));
                        buffer.append("</th><th>");
                        buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.POINTS));
                        buffer.append("</th><th>");
                        buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.SCORE));
                        buffer.append("</th><th>");
                        buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.POSITION));
                        buffer.append("</th></tr><tr><td>");
                        buffer.append(teamSession.getNameAndPlayers());
                        buffer.append("</td><td>");
                        buffer.append(teamSession.getPrettyPrintPoints());
                        buffer.append("</td><td>");
                        buffer.append(teamSession.getScore());
                        buffer.append("</td><td>");
                        buffer.append(index);
                        buffer.append("</td></tr></table>");
                    }
                }
            }
            tables = TableService.getAllByTournament(tournament);
            if (tables != null) {
                nbRounds = 1;
                for (Table table : tables) {
                    if (!(table.getPlayer1().getId().equals(player.getId()) || table.getPlayer2().getId().equals(player.getId()) || table.getPlayer3().getId().equals(player.getId()) || table.getPlayer4().getId().equals(player.getId()))) continue;
                    buffer.append("<table><tr><th colspan=\"4\">");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.ROUND));
                    buffer.append(' ');
                    buffer.append(nbRounds++);
                    buffer.append("</th><th colspan=\"4\">Table ");
                    buffer.append(table.getName());
                    buffer.append("</th></tr><tr><td colspan=\"8\">");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.PLAYER));
                    buffer.append(" : ");
                    buffer.append(table.getPlayer1().getPrettyPrintName());
                    buffer.append(",");
                    buffer.append(table.getPlayer2().getPrettyPrintName());
                    buffer.append(",");
                    buffer.append(table.getPlayer3().getPrettyPrintName());
                    buffer.append(",");
                    buffer.append(table.getPlayer4().getPrettyPrintName());
                    buffer.append("</td></tr><tr><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.GAME));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.SCORE));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.GAME));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.SCORE));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.GAME));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.SCORE));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.GAME));
                    buffer.append("</th><th>");
                    buffer.append(BundleCst.BUNDLE.getString(BundleCst.EXPORT_PLAYERS.SCORE));
                    buffer.append("</th></tr>");
                    displayTableGames = GameService.getDisplayedTableGames(table);
                    int k = 0;
                    for (int nbDTG = 0; true; nbDTG = nbDTG + 4) {
                        if (nbDTG >= displayTableGames.size()) {
                            nbDTG = ++k;
                            if (k == 4) break; else {
                                buffer.append("<tr>");
                            }
                        }
                        displayTableGame = displayTableGames.get(nbDTG);
                        scoreGame = null;
                        if (table.getPlayer1().getId().equals(player.getId())) scoreGame = displayTableGame.getPlayer1ScorePrettyPrint(); else if (table.getPlayer2().getId().equals(player.getId())) scoreGame = displayTableGame.getPlayer2ScorePrettyPrint(); else if (table.getPlayer3().getId().equals(player.getId())) scoreGame = displayTableGame.getPlayer3ScorePrettyPrint(); else if (table.getPlayer4().getId().equals(player.getId())) scoreGame = displayTableGame.getPlayer4ScorePrettyPrint();
                        if (scoreGame != null) {
                            buffer.append("<td>");
                            buffer.append(nbDTG + 1);
                            buffer.append("</td><td>");
                            buffer.append(scoreGame.length() == 0 ? "-" : scoreGame);
                            buffer.append("</td>");
                        }
                    }
                    int scoreTable = -1;
                    if (table.getResult() != null) {
                        if (table.getPlayer1().getId().equals(player.getId())) scoreTable = table.getResult().getScorePlayer1(); else if (table.getPlayer2().getId().equals(player.getId())) scoreTable = table.getResult().getScorePlayer2(); else if (table.getPlayer3().getId().equals(player.getId())) scoreTable = table.getResult().getScorePlayer3(); else if (table.getPlayer4().getId().equals(player.getId())) scoreTable = table.getResult().getScorePlayer4();
                    }
                    buffer.append("<tr><th colspan=\"4\">Total</th><th colspan=\"4\">");
                    buffer.append(scoreTable);
                    buffer.append("</th></tr></table>");
                }
            }
            buffer.append("</body></html>");
            nameOfFile = player.getEma();
            if (nameOfFile == null || nameOfFile.length() == 0) nameOfFile = player.getFirstname() + player.getLastname();
            try {
                zipfile.putNextEntry(new ZipEntry(nameOfFile + ".html"));
                zipfile.write(buffer.toString().getBytes());
                zipfile.closeEntry();
            } catch (IOException e) {
                LOGGER.error("?", e);
            }
        }
        try {
            zipfile.flush();
            zipfile.close();
        } catch (IOException e) {
            LOGGER.error("?", e);
        }
        String fileName = tournament.getName();
        fileName = fileName.replaceAll("[^a-zA-Z0-9]", "_");
        if (LOGGER.isDebugEnabled()) LOGGER.debug("filename=" + fileName);
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "_export_players.zip\"");
        OutputStream outResponse = null;
        try {
            outResponse = response.getOutputStream();
            outResponse.write(out.toByteArray());
        } catch (IOException e) {
            LOGGER.error("?", e);
        } finally {
            if (outResponse != null) try {
                outResponse.flush();
                outResponse.close();
            } catch (IOException e) {
                LOGGER.error("?", e);
            }
        }
        return null;
    }
