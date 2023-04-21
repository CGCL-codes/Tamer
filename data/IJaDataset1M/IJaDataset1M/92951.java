package org.cis.jproyinv.comun.vistas;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import org.cis.jproyinv.comun.modelo.Docente;
import org.cis.jproyinv.comun.negocio.AdministrarDocenteUCController;
import org.edisoncor.gui.panel.PanelImage;
import org.jdesktop.observablecollections.ObservableList;

public class BuscarDocente extends javax.swing.JDialog {

    public AdministrarDocenteUCController docenteUCC = new AdministrarDocenteUCController();

    public boolean invokes = false;

    public Docente elegido = new Docente();

    public BuscarDocente(boolean invokes) {
        super(new javax.swing.JFrame(), true);
        initComponents();
        this.invokes = invokes;
        if (this.invokes) {
            btnCrear.setVisible(false);
            btnEditar.setVisible(false);
            btnAsignar.setVisible(true);
        } else {
            btnAsignar.setVisible(false);
        }
        this.setLocationRelativeTo(this);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        panelBuscarDocente = new org.edisoncor.gui.panel.PanelImage();
        panelRoundTranslucidoComplete1 = new org.edisoncor.gui.panel.PanelRoundTranslucidoComplete();
        txtCriterio = new org.edisoncor.gui.textField.TextFieldRound();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        panelRect1 = new org.edisoncor.gui.panel.PanelRect();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResultadoBusqueda = new javax.swing.JTable();
        btnCrear = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnAsignar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jproyinv.JproyinvApp.class).getContext().getResourceMap(BuscarDocente.class);
        setTitle(resourceMap.getString("Form.title"));
        setAlwaysOnTop(true);
        setName("Form");
        setResizable(false);
        panelBuscarDocente.setIcon(resourceMap.getIcon("panelBuscarDocente.icon"));
        panelBuscarDocente.setName("panelBuscarDocente");
        panelRoundTranslucidoComplete1.setName("panelRoundTranslucidoComplete1");
        txtCriterio.setName("txtCriterio");
        txtCriterio.setOpaque(true);
        jLabel3.setFont(resourceMap.getFont("jLabel3.font"));
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground"));
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        javax.swing.GroupLayout panelRoundTranslucidoComplete1Layout = new javax.swing.GroupLayout(panelRoundTranslucidoComplete1);
        panelRoundTranslucidoComplete1.setLayout(panelRoundTranslucidoComplete1Layout);
        panelRoundTranslucidoComplete1Layout.setHorizontalGroup(panelRoundTranslucidoComplete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRoundTranslucidoComplete1Layout.createSequentialGroup().addGap(30, 30, 30).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(52, Short.MAX_VALUE)));
        panelRoundTranslucidoComplete1Layout.setVerticalGroup(panelRoundTranslucidoComplete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRoundTranslucidoComplete1Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(panelRoundTranslucidoComplete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(txtCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(30, Short.MAX_VALUE)));
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        btnBuscar.setText(resourceMap.getString("btnBuscar.text"));
        btnBuscar.setName("btnBuscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        btnLimpiar.setText(resourceMap.getString("btnLimpiar.text"));
        btnLimpiar.setName("btnLimpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelRect1.setName("panelRect1");
        jScrollPane1.setName("jScrollPane1");
        tblResultadoBusqueda.setName("tblResultadoBusqueda");
        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${listObservable}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tblResultadoBusqueda);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${numeroDocumentoIdentificacion}"));
        columnBinding.setColumnName("Numero Documento Identificacion");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nombres}"));
        columnBinding.setColumnName("Nombres");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${apellidos}"));
        columnBinding.setColumnName("Apellidos");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(tblResultadoBusqueda);
        tblResultadoBusqueda.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tblResultadoBusqueda.columnModel.title0"));
        tblResultadoBusqueda.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tblResultadoBusqueda.columnModel.title1"));
        tblResultadoBusqueda.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tblResultadoBusqueda.columnModel.title2"));
        btnCrear.setText(resourceMap.getString("btnCrear.text"));
        btnCrear.setName("btnCrear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });
        btnEditar.setText(resourceMap.getString("btnEditar.text"));
        btnEditar.setName("btnEditar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelRect1Layout = new javax.swing.GroupLayout(panelRect1);
        panelRect1.setLayout(panelRect1Layout);
        panelRect1Layout.setHorizontalGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRect1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE).addGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(btnCrear, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE).addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)).addContainerGap()));
        panelRect1Layout.setVerticalGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelRect1Layout.createSequentialGroup().addGap(26, 26, 26).addGroup(panelRect1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(panelRect1Layout.createSequentialGroup().addComponent(btnCrear).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnEditar))).addContainerGap(30, Short.MAX_VALUE)));
        btnCerrar.setText(resourceMap.getString("btnCerrar.text"));
        btnCerrar.setName("btnCerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        btnAsignar.setText(resourceMap.getString("btnAsignar.text"));
        btnAsignar.setName("btnAsignar");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelBuscarDocenteLayout = new javax.swing.GroupLayout(panelBuscarDocente);
        panelBuscarDocente.setLayout(panelBuscarDocenteLayout);
        panelBuscarDocenteLayout.setHorizontalGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelBuscarDocenteLayout.createSequentialGroup().addGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelBuscarDocenteLayout.createSequentialGroup().addGap(20, 20, 20).addComponent(jLabel1)).addGroup(panelBuscarDocenteLayout.createSequentialGroup().addGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBuscarDocenteLayout.createSequentialGroup().addContainerGap().addComponent(btnAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBuscarDocenteLayout.createSequentialGroup().addContainerGap().addComponent(panelRect1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBuscarDocenteLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(panelBuscarDocenteLayout.createSequentialGroup().addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(panelRoundTranslucidoComplete1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        panelBuscarDocenteLayout.setVerticalGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelBuscarDocenteLayout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelRoundTranslucidoComplete1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnBuscar).addComponent(btnLimpiar)).addGap(13, 13, 13).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelRect1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelBuscarDocenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnAsignar).addComponent(btnCerrar)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelBuscarDocente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelBuscarDocente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        bindingGroup.bind();
        pack();
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (invokes == true) {
            this.setVisible(false);
        } else {
            this.dispose();
        }
    }

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        txtCriterio.setText("");
        list.clear();
        listObservable.clear();
        tblResultadoBusqueda.repaint();
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        listObservable.clear();
        listObservable.addAll(docenteUCC.buscar(txtCriterio.getText().toUpperCase()));
        tblResultadoBusqueda.repaint();
    }

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {
        docenteUCC.setDoc(new Docente());
        EditarDocente crear = new EditarDocente(false, docenteUCC.getDoc());
        this.dispose();
        crear.setVisible(true);
    }

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            docenteUCC.setDoc(docenteUCC.editar(docenteUCC.buscar(tblResultadoBusqueda.getValueAt(tblResultadoBusqueda.getSelectedRow(), 0) + "").get(0).getId()));
            EditarDocente crear = new EditarDocente(true, docenteUCC.getDoc());
            this.dispose();
            crear.setVisible(true);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            docenteUCC.setDoc(docenteUCC.editar(docenteUCC.buscar(tblResultadoBusqueda.getValueAt(tblResultadoBusqueda.getSelectedRow(), 0) + "").get(0).getId()));
            elegido = docenteUCC.getDoc();
            this.setVisible(false);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private javax.swing.JButton btnAsignar;

    private javax.swing.JButton btnBuscar;

    private javax.swing.JButton btnCerrar;

    private javax.swing.JButton btnCrear;

    private javax.swing.JButton btnEditar;

    private javax.swing.JButton btnLimpiar;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private org.edisoncor.gui.panel.PanelImage panelBuscarDocente;

    private org.edisoncor.gui.panel.PanelRect panelRect1;

    private org.edisoncor.gui.panel.PanelRoundTranslucidoComplete panelRoundTranslucidoComplete1;

    private javax.swing.JTable tblResultadoBusqueda;

    private org.edisoncor.gui.textField.TextFieldRound txtCriterio;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    private List<Docente> list = new ArrayList();

    private ObservableList<Docente> listObservable = org.jdesktop.observablecollections.ObservableCollections.observableList(list);

    public List<Docente> getList() {
        return list;
    }

    public void setList(List<Docente> list) {
        this.list = list;
    }

    public ObservableList<Docente> getListObservable() {
        return listObservable;
    }

    public void setListObservable(ObservableList<Docente> listObservable) {
        this.listObservable = listObservable;
    }

    public PanelImage getPanelBuscarDocente() {
        return panelBuscarDocente;
    }

    public void setPanelBuscarDocente(PanelImage panelBuscarDocente) {
        this.panelBuscarDocente = panelBuscarDocente;
    }

    public JTable getTblResultadoBusqueda() {
        return tblResultadoBusqueda;
    }

    public void setTblResultadoBusqueda(JTable tblResultadoBusqueda) {
        this.tblResultadoBusqueda = tblResultadoBusqueda;
    }

    public Docente getElegido() {
        return elegido;
    }

    public void setElegido(Docente elegido) {
        this.elegido = elegido;
    }
}
