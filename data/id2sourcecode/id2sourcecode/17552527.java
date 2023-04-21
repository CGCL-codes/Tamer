    public List getMensagens() throws Exception {
        List mensagens = new ArrayList();
        String servidor = Configuracao.getString("plugin.buscador.pop.servidor");
        String sporta = Configuracao.getString("plugin.buscador.pop.porta");
        String usuario = Configuracao.getString("plugin.buscador.pop.usuario");
        String senha = Configuracao.getString("plugin.buscador.pop.senha");
        int porta = 0;
        if (servidor == null) {
            throw new Exception("A propriedade 'plugin.buscador.pop.servidor' deve ser informada ao usar este plugin");
        }
        if (sporta == null) {
            porta = 110;
        } else {
            porta = Integer.parseInt(sporta);
        }
        if (usuario == null) {
            throw new Exception("A propriedade 'plugin.buscador.pop.usuario' deve ser informada ao usar este plugin");
        }
        if (senha == null) {
            throw new Exception("A propriedade 'plugin.buscador.pop.senha' deve ser informada ao usar este plugin");
        }
        POP3Client client = new POP3Client();
        client.connect(servidor, porta);
        if (client.login(usuario, senha)) {
            POP3MessageInfo[] envelopes = client.listMessages();
            for (int i = 0; i < envelopes.length; i++) {
                POP3MessageInfo envelope = envelopes[i];
                Reader dados = client.retrieveMessage(envelope.number);
                StringBuffer texto = new StringBuffer();
                char chars[] = new char[4096];
                int ler = 0;
                while ((ler = dados.read(chars)) != -1) {
                    texto.append(chars, 0, ler);
                }
                String conteudo = texto.toString();
                String linhas[] = conteudo.split("\n");
                Mensagem mensagem = new Mensagem();
                boolean conteudoIniciou = false;
                StringBuffer corpo = new StringBuffer();
                for (int j = 0; j < linhas.length; j++) {
                    String linha = linhas[j];
                    if (linha.toUpperCase().matches("Message-Id:.*".toUpperCase())) {
                        String id = linha;
                        id = id.toUpperCase().replaceAll("Message-Id: ".toUpperCase(), "");
                        id = id.replaceAll("[<>@]", "");
                        mensagem.setId(id);
                    }
                    if (linha.matches("Subject:.*")) {
                        mensagem.setAssunto(linha.replaceAll("Subject: ", ""));
                    }
                    if (conteudoIniciou) {
                        corpo.append(linha + "\n");
                    }
                    if (linha.equals("")) {
                        conteudoIniciou = true;
                    }
                }
                mensagem.setMensagem(corpo.toString());
                mensagens.add(mensagem);
                client.deleteMessage(envelope.number);
            }
        } else {
            throw new Exception("Nao foi possivel conectar ao servidor " + servidor + " com as credenciais informadas. Mensagem do servidor: " + client.getReplyString());
        }
        client.logout();
        client.disconnect();
        return mensagens;
    }
