package com.memoire.bu;

import com.memoire.fu.FuPreferences;
import javax.swing.JPanel;

/**
 * Un composant enfichable dans un panneau de pr�f�rences. Le composant est
 * ajout� � la suite des autres dans le panneau, verticalement.
 *
 * @author marchand@deltacad.fr
 */
public abstract class BuAbstractPreferencesComponent extends JPanel {

    BuContainerPreferencesPanel container_;

    protected FuPreferences options_;

    boolean isSavabled_ = true;

    boolean dirty_;

    /**
   * Force la d�finition du container de pr�f�rences (Bu, Fu, etc.).
   */
    public BuAbstractPreferencesComponent(FuPreferences _options) {
        options_ = _options;
    }

    void setContainer(BuContainerPreferencesPanel _container) {
        container_ = _container;
    }

    /**
   * @return true : Les pr�f�rences peuvent �tre ecrites. false sinon.
   */
    public boolean isPreferencesValidable() {
        return false;
    }

    /**
   * Enregistrement des modifications apportees dans ce panel.
   */
    public void validatePreferences() {
        updateProperties();
        options_.writeIniFile();
        setModified(false);
    }

    /**
   * @return true : Les pr�f�rences peuvent �tre appliqu�es sans sortie du dialogue.
   * false sinon.
   */
    public boolean isPreferencesApplyable() {
        return false;
    }

    /**
   * A surcharger si les pr�f�rences peuvent �tre appliqu�es.
   */
    public void applyPreferences() {
    }

    /**
   * @return true : Les pr�f�rences peuvent �tre annul�es.
   * false sinon.
   */
    public boolean isPreferencesCancelable() {
        return false;
    }

    /**
   * Annuler les modifications (relit les preferences).
   */
    public void cancelPreferences() {
        options_.readIniFile();
        updateComponent();
        setModified(false);
    }

    /**
   * Le titre du composant
   */
    public abstract String getTitle();

    /**
   * Met a jour le composant � partir des propri�t�s
   */
    protected abstract void updateComponent();

    /**
   * Met a jour les propri�t�s � partir du composant.
   */
    protected abstract void updateProperties();

    /**
   * @return True : Le composant est modifi�.
   */
    protected boolean isModified() {
        return dirty_;
    }

    protected void setModified(boolean _b) {
        dirty_ = _b;
        container_.firePropertyChange();
    }

    /**
   * @return True : Le composant est sauvable, autrement dit les valeurs donn�es sont correctes.
   */
    protected boolean isSavabled() {
        return isSavabled_;
    }

    protected void setSavabled(boolean _b) {
        isSavabled_ = _b;
        container_.firePropertyChange();
    }
}
