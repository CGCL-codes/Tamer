    @Override
    protected String execute(Player player, Map<String, Object> params, Session session) throws Exception {
        String channel = (String) params.get("channel");
        String message = (String) params.get("message");
        if (!ChatManager.getInstance().isInChannel(channel, player.getId())) throw new IllegalOperationException("Vous ne pouvez pas envoyer de message sur ce canal.");
        if (player.getEkey().length() > 0 && message.contains(player.getEkey())) throw new IllegalOperationException("Ne transmettez pas votre clé d'export à d'autres joueurs.");
        LoggingSystem.getChatLogger().info("[" + channel + "] " + player.getLogin() + ": " + message);
        message = message.replace(new String(new char[] { 10, 13 }), " ").trim();
        message = MessageTools.tidyHTML(message, MessageTools.KEEP_QUOTES);
        StringBuffer content = new StringBuffer();
        String type;
        Player whisperTarget = null;
        int target;
        boolean updateChannels = false;
        List<Update> updates = new ArrayList<Update>();
        if (!ChatManager.getInstance().isChannelEnabled(player, channel)) {
            ChatManager.getInstance().setChannelEnabled(player, channel, true);
            updateChannels = true;
        }
        if (player.getBanChat() > Utilities.now()) {
            Date date = new Date(player.getBanChat() * 1000);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy à HH:mm:ss");
            target = TARGET_SELF;
            type = TYPE_ERROR;
            content.append("Vous avez été banni du chat jusqu'au ");
            content.append(format.format(date));
            content.append(".");
        } else if (message.startsWith("/")) {
            if (message.startsWith("/me ")) {
                content.append(message);
                target = TARGET_CHANNEL;
                type = TYPE_DEFAULT;
            } else if (message.startsWith("/trophee") || message.startsWith("/trophée") || message.startsWith("/t ")) {
                Player commandTarget = null;
                String login = "";
                boolean valid = true;
                if (message.contains(" ")) {
                    String[] split = message.split(" ", 2);
                    if (split.length < 2) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Syntaxe : /t joueur");
                        valid = false;
                    } else {
                        login = split[1];
                        if (login.startsWith("\"") && login.lastIndexOf("\"") != 0) login = message.split("\"", 3)[1];
                        commandTarget = DataAccess.getPlayerByLogin(login);
                    }
                } else {
                    commandTarget = player;
                    login = player.getLogin();
                }
                if (valid && commandTarget == null) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Joueur inconnu : " + login + ".");
                } else {
                    content.append("Trophées acquis par ");
                    content.append(login);
                    content.append(" : ");
                    List<Achievement> achievements = commandTarget.getAchievements();
                    String[] colors = { "#ffc000", "#ff9100", "#ff5e00", "#ff2f00", "#ff0000" };
                    boolean firstLevel = true;
                    synchronized (achievements) {
                        for (int i = 5; i >= 1; i--) {
                            StringBuffer buffer = new StringBuffer();
                            boolean first = true;
                            for (Achievement achievement : achievements) {
                                int level = achievement.getLevel();
                                if (level == i) {
                                    if (first) first = false; else buffer.append(", ");
                                    buffer.append(Messages.getString("achievement" + achievement.getType()));
                                }
                            }
                            if (buffer.length() > 0) {
                                if (firstLevel) firstLevel = false; else content.append(" ; ");
                                content.append("<span style=\"color: ");
                                content.append(colors[i - 1]);
                                content.append("\">");
                                content.append(Messages.getString("achievement.level" + i));
                                content.append(" ");
                                content.append(buffer);
                                content.append("</span>");
                            }
                        }
                    }
                    target = TARGET_SELF;
                    type = TYPE_INFO;
                }
            } else if (message.equals("/played") || message.equals("/joué")) {
                target = TARGET_SELF;
                type = TYPE_INFO;
                content.append("Vous avez joué " + (player.getPlayedTime(Player.SCALE_DAY) / 3600) + "h sur Fallen Galaxy aujourd'hui, " + (player.getPlayedTime(Player.SCALE_MONTH) / 3600) + "h ce mois, et " + (player.getPlayedTime(Player.SCALE_OVERALL) / 3600) + "h en tout !");
            } else if (message.startsWith("/public ") || message.startsWith("/p ")) {
                String value = message.substring(message.indexOf(" ") + 1);
                int index;
                try {
                    index = Integer.parseInt(value);
                } catch (Exception e) {
                    index = -1;
                }
                if (index < 1 || index > 999) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Syntaxe : /public numéro");
                } else {
                    String channelName = ChatManager.PUBLIC_CHANNEL_PREFIX + index;
                    if (!ChatManager.getInstance().isExistingChannel(channelName)) {
                        if (player.hasRight(Player.PREMIUM)) {
                            target = TARGET_SELF;
                            type = TYPE_INFO;
                            content.append("Vous avez rejoint le canal public " + index + ".");
                            updateChannels = true;
                            Set<String> channels = new HashSet<String>(ChatManager.getInstance().getPlayerChannels(player.getId()));
                            for (String c : channels) if (c.startsWith(ChatManager.PUBLIC_CHANNEL_PREFIX)) ChatManager.getInstance().leaveChannel(c, player.getId());
                            ChatManager.getInstance().joinChannel(channelName, player);
                        } else {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Compte premium requis pour pouvoir créer un canal.");
                        }
                    } else {
                        if (ChatManager.getInstance().getChannelPlayers(channelName).contains(player.getId())) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Vous vous trouvez déjà dans le canal public " + index + ".");
                        } else {
                            target = TARGET_SELF;
                            type = TYPE_INFO;
                            content.append("Vous avez rejoint le canal public " + index + ".");
                            Set<String> channels = new HashSet<String>(ChatManager.getInstance().getPlayerChannels(player.getId()));
                            updateChannels = true;
                            for (String c : channels) if (c.startsWith(ChatManager.PUBLIC_CHANNEL_PREFIX)) ChatManager.getInstance().leaveChannel(c, player.getId());
                            ChatManager.getInstance().joinChannel(channelName, player);
                        }
                    }
                }
            } else if (message.startsWith("/join ") || message.startsWith("/rejoindre ") || message.startsWith("/j ")) {
                String value = message.substring(message.indexOf(" ") + 1);
                if (value.length() == 0) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Syntaxe : /rejoindre nom canal");
                } else {
                    NameStringFormat format = new NameStringFormat();
                    String channelName = (String) format.format(value);
                    if (channelName == null || channelName.length() > 20) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Le nom du canal doit comporter au " + "maximum 20 des caractères alphanumériques, " + "espaces ou apostrophes.");
                    } else if (Badwords.containsBadwords(channelName)) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Le nom du canal n'est pas autorisé.");
                    } else {
                        if (!ChatManager.getInstance().isExistingChannel(channelName)) {
                            if (player.hasRight(Player.PREMIUM)) {
                                target = TARGET_SELF;
                                type = TYPE_INFO;
                                content.append("Vous avez rejoint le canal " + channelName + ".");
                                updateChannels = true;
                                Set<String> channels = new HashSet<String>(ChatManager.getInstance().getPlayerChannels(player.getId()));
                                for (String c : channels) {
                                    if (!c.startsWith(ChatManager.ALLY_CHANNEL_PREFIX) && !c.startsWith(ChatManager.EMBASSY_CHANNEL_PREFIX) && !c.startsWith(ChatManager.PUBLIC_CHANNEL_PREFIX)) {
                                        ChatManager.getInstance().leaveChannel(c, player.getId());
                                    }
                                }
                                ChatManager.getInstance().joinChannel(channelName, player);
                            } else {
                                target = TARGET_SELF;
                                type = TYPE_ERROR;
                                content.append("Compte premium requis pour pouvoir créer un canal.");
                            }
                        } else {
                            if (ChatManager.getInstance().getChannelPlayers(channelName).contains(player.getId())) {
                                target = TARGET_SELF;
                                type = TYPE_ERROR;
                                content.append("Vous vous trouvez déjà dans le canal " + channelName + ".");
                            } else {
                                target = TARGET_SELF;
                                type = TYPE_INFO;
                                content.append("Vous avez rejoint le canal " + channelName + ".");
                                Set<String> channels = new HashSet<String>(ChatManager.getInstance().getPlayerChannels(player.getId()));
                                updateChannels = true;
                                for (String c : channels) {
                                    if (!c.startsWith(ChatManager.ALLY_CHANNEL_PREFIX) && !c.startsWith(ChatManager.EMBASSY_CHANNEL_PREFIX) && !c.startsWith(ChatManager.PUBLIC_CHANNEL_PREFIX)) {
                                        ChatManager.getInstance().leaveChannel(c, player.getId());
                                    }
                                }
                                ChatManager.getInstance().joinChannel(channelName, player);
                            }
                        }
                    }
                }
            } else if (message.startsWith("/embassy ") || message.startsWith("/ambassade ") || message.startsWith("/a ")) {
                String embassy = message.substring(message.indexOf(" ") + 1);
                if (embassy.length() == 0) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Syntaxe : /ambassade alliance ou /ambassade tag");
                } else {
                    Ally ally = DataAccess.getAllyByName(embassy);
                    if (ally == null) ally = DataAccess.getAllyByTag(embassy);
                    if (ally == null) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Alliance inconnue : " + embassy + ".");
                    } else if (player.getIdAlly() != 0 && player.getAlly().getTreatyWithAlly(ally).equals(Treaty.ENEMY)) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous ne pouvez pas accèder à l'ambassade d'une alliance ennemie.");
                    } else if (player.getIdAlly() != 0 && embassy.equals(player.getAllyName())) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous ne pouvez rejoindre votre propre ambassade.");
                    } else {
                        target = TARGET_SELF;
                        type = TYPE_INFO;
                        content.append("Vous avez rejoint l'ambassade de " + ally.getName() + ".");
                        updateChannels = true;
                        Set<String> channels = new HashSet<String>(ChatManager.getInstance().getPlayerChannels(player.getId()));
                        String playerAllyName = player.getAllyName();
                        for (String channelName : channels) {
                            if (channelName.startsWith(ChatManager.EMBASSY_CHANNEL_PREFIX) && !ChatManager.getInstance().getChannelName(channelName).substring(1).equals(playerAllyName)) {
                                ChatManager.getInstance().leaveChannel(channelName, player.getId());
                                break;
                            }
                        }
                        ChatManager.getInstance().joinChannel(ChatManager.EMBASSY_CHANNEL_PREFIX + ally.getName(), player);
                    }
                }
            } else if (message.startsWith("/kick ") || message.startsWith("/éjecter ")) {
                if (channel.startsWith(ChatManager.EMBASSY_CHANNEL_PREFIX)) {
                    if (player.getIdAlly() != 0 && ChatManager.getInstance().getChannelName(Utilities.formatString(channel)).substring(1).equals(player.getAllyName())) {
                        String login = message.substring(message.indexOf(" ") + 1);
                        login = login.trim();
                        Player kickedPlayer = DataAccess.getPlayerByLogin(login);
                        if (kickedPlayer == null) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Joueur inconnu : " + login + ".");
                        } else if (kickedPlayer.getLogin().equals(player.getLogin())) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Vous ne pouvez pas vous éjecter vous-même !");
                        } else if (kickedPlayer.getIdAlly() == player.getIdAlly()) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Vous ne pouvez pas éjecter un membre de votre alliance !");
                        } else if (!ChatManager.getInstance().isInChannel(channel, kickedPlayer.getId())) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append(login);
                            content.append(" n'est pas présent sur le canal.");
                        } else {
                            target = TARGET_CHANNEL;
                            type = TYPE_MODERATION;
                            ChatManager.getInstance().leaveChannel(channel, kickedPlayer.getId());
                            UpdateTools.queueChatChannelsUpdate(kickedPlayer.getId());
                            content.append(kickedPlayer.getLogin());
                            content.append(" a été éjecté de l'ambassade de " + player.getAllyName() + " par " + player.getLogin() + ".");
                            String kickedPlayerContent = "Vous avez été éjecté de l'ambassade de " + player.getAllyName() + " par " + player.getLogin() + ".";
                            String kickedPlayerChannel = ChatManager.getInstance().getChannelName(ChatManager.getInstance().getPlayerChannels(kickedPlayer.getId()).iterator().next());
                            JSONObject json = new JSONObject();
                            json.put(ChatMessageData.FIELD_CONTENT, kickedPlayerContent);
                            json.put(ChatMessageData.FIELD_DATE, Utilities.now());
                            json.put(ChatMessageData.FIELD_TYPE, TYPE_MODERATION);
                            json.put(ChatMessageData.FIELD_CHANNEL, kickedPlayerChannel);
                            json.put(ChatMessageData.FIELD_AUTHOR, kickedPlayer.getLogin());
                            json.put(ChatMessageData.FIELD_RIGHTS, kickedPlayer.hasRight(Player.MODERATOR) ? "moderator" : "player");
                            json.put(ChatMessageData.FIELD_ALLY_TAG, kickedPlayer.getAllyTag());
                            json.put(ChatMessageData.FIELD_ALLY_NAME, kickedPlayer.getAllyName());
                            UpdateTools.queueChatUpdate(kickedPlayer.getId(), json.toString());
                        }
                    } else {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous ne pouvez pas éjecter de joueur sur ce canal.");
                    }
                } else if (!channel.startsWith(ChatManager.PUBLIC_CHANNEL_PREFIX) && !channel.startsWith(ChatManager.ALLY_CHANNEL_PREFIX)) {
                    if (ChatManager.getInstance().getChannelOwner(channel) == player.getId()) {
                        String login = message.substring(message.indexOf(" ") + 1);
                        login = login.trim();
                        Player kickedPlayer = DataAccess.getPlayerByLogin(login);
                        if (kickedPlayer == null) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Joueur inconnu : " + login + ".");
                        } else if (kickedPlayer.getLogin().equals(player.getLogin())) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append("Vous ne pouvez pas vous éjecter vous-même !");
                        } else if (!ChatManager.getInstance().isInChannel(channel, kickedPlayer.getId())) {
                            target = TARGET_SELF;
                            type = TYPE_ERROR;
                            content.append(login);
                            content.append(" n'est pas présent sur le canal.");
                        } else {
                            target = TARGET_CHANNEL;
                            type = TYPE_MODERATION;
                            ChatManager.getInstance().leaveChannel(channel, kickedPlayer.getId());
                            UpdateTools.queueChatChannelsUpdate(kickedPlayer.getId());
                            content.append(kickedPlayer.getLogin());
                            content.append(" a été éjecté du canal " + channel + " par " + player.getLogin() + ".");
                            String kickedPlayerContent = "Vous avez été éjecté du canal " + channel + " par " + player.getLogin() + ".";
                            String kickedPlayerChannel = ChatManager.getInstance().getChannelName(ChatManager.getInstance().getPlayerChannels(kickedPlayer.getId()).iterator().next());
                            JSONObject json = new JSONObject();
                            json.put(ChatMessageData.FIELD_CONTENT, kickedPlayerContent);
                            json.put(ChatMessageData.FIELD_DATE, Utilities.now());
                            json.put(ChatMessageData.FIELD_TYPE, TYPE_MODERATION);
                            json.put(ChatMessageData.FIELD_CHANNEL, kickedPlayerChannel);
                            json.put(ChatMessageData.FIELD_AUTHOR, kickedPlayer.getLogin());
                            json.put(ChatMessageData.FIELD_RIGHTS, kickedPlayer.hasRight(Player.MODERATOR) ? "moderator" : "player");
                            json.put(ChatMessageData.FIELD_ALLY_TAG, kickedPlayer.getAllyTag());
                            json.put(ChatMessageData.FIELD_ALLY_NAME, kickedPlayer.getAllyName());
                            UpdateTools.queueChatUpdate(kickedPlayer.getId(), json.toString());
                        }
                    } else {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Seul le créateur du canal peut éjecter des joueurs.");
                    }
                } else {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Vous ne pouvez pas éjecter de joueur sur ce canal.");
                }
            } else if (message.equals("/quit") || message.equals("/quitter") || message.equals("/q")) {
                if (channel.startsWith(ChatManager.EMBASSY_CHANNEL_PREFIX)) {
                    if (player.getIdAlly() != 0 && ChatManager.getInstance().getChannelName(Utilities.formatString(channel)).substring(1).equals(player.getAllyName())) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous ne pouvez pas quitter votre ambassade.");
                    } else {
                        String channelName = ChatManager.getInstance().getChannelName(Utilities.formatString(channel)).substring(1);
                        ChatManager.getInstance().leaveChannel(channel, player.getId());
                        updateChannels = true;
                        target = TARGET_SELF;
                        type = TYPE_INFO;
                        content.append("Vous avez quitté l'ambassade de " + channelName + ".");
                    }
                } else if (!channel.startsWith(ChatManager.ALLY_CHANNEL_PREFIX) && !channel.startsWith(ChatManager.ALLY_CHANNEL_PREFIX)) {
                    if (ChatManager.getInstance().isExistingChannel(channel) && ChatManager.getInstance().getChannelPlayers(channel).contains(player.getId())) {
                        String channelName = ChatManager.getInstance().getChannelName(Utilities.formatString(channel));
                        ChatManager.getInstance().leaveChannel(channel, player.getId());
                        updateChannels = true;
                        target = TARGET_SELF;
                        type = TYPE_INFO;
                        content.append("Vous avez quitté le canal " + channelName + ".");
                    } else {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous ne pouvez pas quitter ce canal.");
                    }
                } else {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Vous ne pouvez pas quitter ce canal.");
                }
            } else if (message.equals("/who") || message.equals("/qui")) {
                target = TARGET_SELF;
                type = TYPE_WHO;
                Set<Integer> players = ChatManager.getInstance().getChannelPlayers(channel);
                List<String> playersLogin = new ArrayList<String>(players.size());
                synchronized (players) {
                    for (Integer idPlayer : players) {
                        Player onlinePlayer = DataAccess.getPlayerById(idPlayer);
                        playersLogin.add(onlinePlayer.getLogin() + (onlinePlayer.getIdAlly() != 0 ? "|" + onlinePlayer.getAllyTag() + "|" + onlinePlayer.getAllyName() : ""));
                    }
                }
                Collections.sort(playersLogin, String.CASE_INSENSITIVE_ORDER);
                int i = 0;
                for (String login : playersLogin) content.append((i++ == 0 ? "" : ",") + login);
            } else if (message.startsWith("/w ") || message.startsWith("/whisper ") || message.startsWith("/m ") || message.startsWith("/murmurer ")) {
                String[] split = message.split(" ", 3);
                if (split.length < 3) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Syntaxe : /m joueur message, ou /m \"joueur\" message");
                } else {
                    if (split[1].startsWith("\"") && (split[1] + split[2]).lastIndexOf("\"") != 0) split = message.split("\"", 3);
                    whisperTarget = DataAccess.getPlayerByLogin(split[1]);
                    if (whisperTarget == null) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Joueur inconnu : " + split[1] + ". " + "Placez le nom du joueur entre \"...\" si " + "son nom comporte des espaces.");
                    } else if (!ConnectionManager.getInstance().isConnected(whisperTarget.getId())) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append(split[1] + " n'est pas connecté sur le chat.");
                    } else if (whisperTarget.getBanChat() > Utilities.now()) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append(split[1] + " a été banni du chat.");
                    } else if (whisperTarget.isIgnoring(player.getId())) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append(split[1] + " ne souhaite pas recevoir de messages de votre part.");
                    } else {
                        target = TARGET_WHISPER;
                        type = TYPE_WHISPER_SENT;
                        content.append(split[2].trim());
                    }
                }
            } else if (message.equals("/time") || message.equals("/heure")) {
                target = TARGET_SELF;
                type = TYPE_INFO;
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy à HH:mm:ss");
                content.append("Nous sommes le ");
                content.append(format.format(date));
            } else if (message.equals("/online") || message.equals("/enligne") || message.equals("/?")) {
                target = TARGET_SELF;
                type = TYPE_ONLINE;
                Set<Integer> players = ConnectionManager.getInstance().getConnectedPlayers();
                List<String> playersLogin = new ArrayList<String>(players.size());
                synchronized (players) {
                    for (Integer idPlayer : players) {
                        Player onlinePlayer = DataAccess.getPlayerById(idPlayer);
                        playersLogin.add(onlinePlayer.getLogin() + (onlinePlayer.getIdAlly() != 0 ? "|" + onlinePlayer.getAllyTag() + "|" + onlinePlayer.getAllyName() : ""));
                    }
                }
                Collections.sort(playersLogin, String.CASE_INSENSITIVE_ORDER);
                int i = 0;
                for (String login : playersLogin) content.append((i++ == 0 ? "" : ",") + login);
            } else if (message.equals("/ally") || message.equals("/alliance")) {
                target = TARGET_SELF;
                type = TYPE_ALLY_MEMBERS;
                Ally ally = player.getAlly();
                if (ally == null) {
                    type = TYPE_ERROR;
                    content.append("Vous n'avez pas d'alliance !");
                } else {
                    List<Player> members = ally.getMembers();
                    List<String> playersLogin = new ArrayList<String>(members.size());
                    synchronized (members) {
                        for (Player member : members) {
                            if (ConnectionManager.getInstance().isConnected(member.getId())) playersLogin.add(member.getLogin());
                        }
                    }
                    Collections.sort(playersLogin, String.CASE_INSENSITIVE_ORDER);
                    int i = 0;
                    for (String login : playersLogin) content.append((i++ == 0 ? "" : ",") + login);
                }
            } else if (message.equals("/help") || message.equals("/aide")) {
                target = TARGET_SELF;
                type = TYPE_INFO;
                content.append("Liste des commandes :<br/>" + "/aide /murmurer (/m) /me /alliance /qui /enligne (/?) /heure /ping /joué /ambassade (/a) /public (/p) /rejoindre (/j) /quitter (/q) /éjecter /trophées (/t)");
            } else if (message.startsWith("/ping")) {
                target = TARGET_SELF;
                type = TYPE_PING;
                content.append(message.length() > 6 ? message.substring(6) : "");
            } else if (message.startsWith("/ban")) {
                String[] split = message.split(" ", 3);
                if (!player.hasRight(Player.MODERATOR)) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Commande réservée aux modérateurs.");
                } else if (split.length < 3) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Syntaxe : /ban joueur durée(m|h|d) ; avec " + "m = minutes, h = heures, d = jours. Exemple : " + "/ban dark 12h banni le joueur dark du chat pour 12h.");
                } else {
                    if (split[1].startsWith("\"") && (split[1] + split[2]).lastIndexOf("\"") != 0) split = message.split("\"", 3);
                    split[2] = split[2].trim();
                    Player bannedTarget = DataAccess.getPlayerByLogin(split[1]);
                    String lastChar = String.valueOf(split[2].charAt(split[2].length() - 1));
                    int length = -1;
                    try {
                        length = Integer.parseInt(split[2].substring(0, split[2].length() - 1));
                    } catch (Exception e) {
                    }
                    if ((!lastChar.equalsIgnoreCase("m") && !lastChar.equalsIgnoreCase("h") && !lastChar.equalsIgnoreCase("d")) || length < 0) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Syntaxe : /ban joueur durée(m|h|d) ; avec " + "m = minutes, h = heures, d = jours. Exemple : " + "/ban dark 12h banni le joueur dark du chat pour 12h.");
                    } else if (bannedTarget == null) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Joueur inconnu : " + split[1] + ".");
                    } else if (bannedTarget.hasRight(Player.MODERATOR) || bannedTarget.hasRight(Player.ADMINISTRATOR) || bannedTarget.hasRight(Player.SUPER_ADMINISTRATOR)) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous n'êtes pas autorisé à effectuer " + "cette opération sur un modérateur.");
                    } else {
                        if (lastChar.equalsIgnoreCase("m")) length *= 60; else if (lastChar.equalsIgnoreCase("h")) length *= 60 * 60; else if (lastChar.equalsIgnoreCase("d")) length *= 60 * 60 * 24;
                        synchronized (bannedTarget.getLock()) {
                            bannedTarget = DataAccess.getEditable(bannedTarget);
                            bannedTarget.setBanChat(Utilities.now() + length);
                            DataAccess.save(bannedTarget);
                        }
                        target = TARGET_CHANNEL;
                        type = TYPE_MODERATION;
                        Date date = new Date(bannedTarget.getBanChat() * 1000);
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy à HH:mm:ss");
                        content.append(bannedTarget.getLogin());
                        content.append(" a été banni ");
                        content.append("du chat par ");
                        content.append(player.getLogin());
                        content.append(" jusqu'au ");
                        content.append(format.format(date));
                        content.append(".");
                    }
                }
            } else if (message.startsWith("/unban")) {
                String[] split = message.split(" ", 2);
                if (!player.hasRight(Player.MODERATOR)) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Commande réservée aux modérateurs.");
                } else if (split.length < 2) {
                    target = TARGET_SELF;
                    type = TYPE_ERROR;
                    content.append("Syntaxe : /unban joueur");
                } else {
                    String login = split[1];
                    if (login.startsWith("\"") && login.lastIndexOf("\"") != 0) login = message.split("\"", 3)[1];
                    Player unbannedTarget = DataAccess.getPlayerByLogin(login);
                    if (unbannedTarget == null) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Joueur inconnu : " + login + ".");
                    } else if (unbannedTarget.hasRight(Player.MODERATOR) || unbannedTarget.hasRight(Player.ADMINISTRATOR) || unbannedTarget.hasRight(Player.SUPER_ADMINISTRATOR)) {
                        target = TARGET_SELF;
                        type = TYPE_ERROR;
                        content.append("Vous n'êtes pas autorisé à effectuer " + "cette opération sur un modérateur.");
                    } else {
                        synchronized (unbannedTarget.getLock()) {
                            unbannedTarget = DataAccess.getEditable(unbannedTarget);
                            unbannedTarget.setBanChat(0);
                            DataAccess.save(unbannedTarget);
                        }
                        target = TARGET_CHANNEL;
                        type = TYPE_MODERATION;
                        content.append(player.getLogin());
                        content.append(" a levé le bannissement sur ");
                        content.append(unbannedTarget.getLogin());
                        content.append(".");
                    }
                }
            } else {
                target = TARGET_SELF;
                type = TYPE_ERROR;
                content.append("Commande inconnue. " + "Tapez /help pour afficher la liste des commandes.");
            }
        } else {
            target = TARGET_CHANNEL;
            content.append(message);
            type = TYPE_DEFAULT;
        }
        JSONObject json = new JSONObject();
        json.put(ChatMessageData.FIELD_CONTENT, content);
        json.put(ChatMessageData.FIELD_DATE, Utilities.now());
        json.put(ChatMessageData.FIELD_TYPE, type);
        json.put(ChatMessageData.FIELD_CHANNEL, channel);
        json.put(ChatMessageData.FIELD_AUTHOR, player.getLogin());
        json.put(ChatMessageData.FIELD_RIGHTS, player.hasRight(Player.MODERATOR) ? "moderator" : "player");
        json.put(ChatMessageData.FIELD_ALLY_TAG, player.getAllyTag());
        json.put(ChatMessageData.FIELD_ALLY_NAME, player.getAllyName());
        String jsonString = json.toString();
        if (updateChannels) updates.add(Update.getChatChannelsUpdate());
        switch(target) {
            case TARGET_CHANNEL:
                Set<Integer> players = ChatManager.getInstance().getChannelPlayers(channel);
                synchronized (players) {
                    for (int i = 0; i < 2; i++) {
                        for (Integer idPlayer : players) {
                            Player chattingPlayer = DataAccess.getPlayerById(idPlayer);
                            if ((i == 0 && chattingPlayer.isSettingsCensorship()) || (i == 1 && !chattingPlayer.isSettingsCensorship())) continue;
                            if (!ChatManager.getInstance().isChannelEnabled(chattingPlayer, channel)) continue;
                            if (idPlayer == player.getId()) updates.add(Update.getChatUpdate(jsonString)); else UpdateTools.queueChatUpdate(chattingPlayer.getId(), jsonString);
                        }
                        if (i == 0) {
                            json.put(ChatMessageData.FIELD_CONTENT, Badwords.parse(content.toString()));
                            jsonString = json.toString();
                        }
                    }
                }
                break;
            case TARGET_SELF:
                updates.add(Update.getChatUpdate(jsonString));
                break;
            case TARGET_WHISPER:
                if (player.isSettingsCensorship()) json.put(ChatMessageData.FIELD_CONTENT, Badwords.parse(content.toString()));
                json.put(ChatMessageData.FIELD_TYPE, TYPE_WHISPER_SENT);
                json.put(ChatMessageData.FIELD_AUTHOR, whisperTarget.getLogin());
                json.put(ChatMessageData.FIELD_ALLY_TAG, whisperTarget.getAllyTag());
                json.put(ChatMessageData.FIELD_ALLY_NAME, whisperTarget.getAllyName());
                updates.add(Update.getChatUpdate(json.toString()));
                if (whisperTarget.isSettingsCensorship()) json.put(ChatMessageData.FIELD_CONTENT, Badwords.parse(content.toString())); else json.put(ChatMessageData.FIELD_CONTENT, content);
                json.put(ChatMessageData.FIELD_TYPE, TYPE_WHISPER_RECEIVED);
                json.put(ChatMessageData.FIELD_AUTHOR, player.getLogin());
                json.put(ChatMessageData.FIELD_ALLY_TAG, player.getAllyTag());
                json.put(ChatMessageData.FIELD_ALLY_NAME, player.getAllyName());
                if (whisperTarget.getId() == player.getId()) updates.add(Update.getChatUpdate(json.toString())); else UpdateTools.queueChatUpdate(whisperTarget.getId(), json.toString());
                break;
        }
        return UpdateTools.formatUpdates(player, updates);
    }
