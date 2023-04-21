    public personagem(String Nome, int fe, int intuicao, int criatividade, int vontade, int impeto, int reflexo) {
        int forca = (impeto + vontade) / 2;
        int inteligencia = (intuicao + criatividade) / 2;
        int resistencia = vontade;
        int percepcao = intuicao;
        int agilidade = (impeto + reflexo) / 2;
        int protecao = 0;
        set_Nome(Nome);
        this.inventario = new inventario(10);
        this.fe = new atributo("Fe", fe);
        this.intuicao = new atributo("Intuicao", intuicao);
        this.criatividade = new atributo("Criatividade", criatividade);
        this.vontade = new atributo("Vontade", vontade);
        this.impeto = new atributo("�mpeto", impeto);
        this.reflexo = new atributo("Reflexo", reflexo);
        this.forca = new atributo("forca", forca);
        this.inteligencia = new atributo("Inteligencia", inteligencia);
        this.resistencia = new atributo("Resistencia", resistencia);
        this.percepcao = new atributo("Percep��o", percepcao);
        this.agilidade = new atributo("Agilidade", agilidade);
        this.dano = new atributo("Dano", 0);
        this.habilidade = new atributo("Habilidade", 2);
        this.penalidade = 0;
    }
