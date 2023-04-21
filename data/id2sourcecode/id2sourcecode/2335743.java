    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Funcionario func = new Funcionario();
            func.setNome(this.txtNome.getText().toUpperCase());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            func.setDataContratacao(sdf.parse(this.txtDataContratacao.getText()));
            Object obj = this.cmbCargo.getModel().getSelectedItem();
            if (obj instanceof Cargo) {
                func.setCargo((Cargo) obj);
            } else {
                JOptionPane.showMessageDialog(this.mdi, "Cargo deve ser informado");
                this.cmbCargo.requestFocus();
                return;
            }
            obj = this.cmbDepartamento.getModel().getSelectedItem();
            if (obj instanceof Departamento) {
                func.setDepartamento((Departamento) obj);
            } else {
                JOptionPane.showMessageDialog(this.mdi, "Departamento deve ser informado");
                this.cmbDepartamento.requestFocus();
                return;
            }
            obj = this.cmbEncarregado.getModel().getSelectedItem();
            if (obj instanceof Funcionario) {
                func.setGerente((Funcionario) obj);
            }
            func.setLogin(this.txtLogin.getText());
            String senha = new String(this.txtSenha.getPassword());
            func.setSenha(senha);
            FuncionarioDAO funcDao = new FuncionarioDAO(MDIStarter.getConexao());
            int cod = this.intCampoTelas(this.txtCodigo.getText());
            if (cod > 0) {
                func.setCod(cod);
                funcDao.update(func);
            } else {
                funcDao.create(func);
                this.txtCodigo.setText(Integer.toString(func.getCod()));
            }
            JOptionPane.showMessageDialog(this.mdi, "Registro Salvo");
            this.limpaTela();
            this.atualizaComboDepartamento();
            this.atualizaComboEncarregado();
            this.atualizaComboCargo();
            this.atualizaLista();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.mdi, "Número mal formatado [Código]");
            this.txtCodigo.requestFocus();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this.mdi, "Data inválida");
            this.txtDataContratacao.requestFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.mdi, String.format("Erro ao gravar Funcionario:%s", ex.getMessage()), "Erro", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(FormFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
