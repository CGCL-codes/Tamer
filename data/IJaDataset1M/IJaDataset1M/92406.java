package telas_teste;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author CABS
 */
public class Window_04 extends JFrame {

    JTextField nome = new JTextField(30);

    GridBagLayout gridbag = new GridBagLayout();

    JPanel pane = new JPanel();

    public Window_04() {
        super("Ficha do Paciente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane.setLayout(gridbag);
        JTextField foto = new JTextField();
        JLabel nomepacienteLabel = new JLabel("Nome: ");
        JTextField nomepaciente = new JTextField();
        JLabel sexoLabel = new JLabel("Sexo: ");
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento: ");
        JLabel emailLabel = new JLabel("Email: ");
        JTextField email = new JTextField();
        JLabel enderecoLabel = new JLabel("Endereço: ");
        JLabel complementoLabel = new JLabel("Complemento: ");
        JLabel cidadeLabel = new JLabel("Cidade: ");
        JLabel medicoLabel = new JLabel("Medico: ");
        JLabel planodeSaudeLabel = new JLabel("Plano de Saude: ");
        JLabel numeroPlanoLabel = new JLabel("Numero do Plano: ");
        JLabel validadeLabel = new JLabel("Validade: ");
        addComponent(foto, 0, 0, 2, 8, 20, 100, GridBagConstraints.BOTH, GridBagConstraints.EAST);
        addComponent(nomepacienteLabel, 1, 0, 1, 1, 10, 20, GridBagConstraints.NONE, GridBagConstraints.EAST);
        addComponent(nomepaciente, 2, 0, 5, 1, 50, 20, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        addComponent(sexoLabel, 1, 1, 1, 1, 10, 20, GridBagConstraints.NONE, GridBagConstraints.EAST);
        addComponent(emailLabel, 1, 2, 1, 1, 10, 20, GridBagConstraints.NONE, GridBagConstraints.EAST);
        addComponent(email, 2, 1, 5, 1, 50, 20, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        add(pane);
        setVisible(true);
    }

    private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, int fill, int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.fill = fill;
        constraints.anchor = anchor;
        gridbag.setConstraints(component, constraints);
        pane.add(component);
    }
}
