package org.santeplanning.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.santeplanning.model.EnchainementCycle;
import org.santeplanning.model.EnchainementCycleFactory;
import org.santeplanning.model.UtilisateurAdapter;
import org.santeplanning.state.EditEnchainementCycleState;
import org.stateengine.action.AActionOnState;
import org.stateengine.entity.IStateEngineUser;
import org.stateengine.state.IStateContainer;
import org.stateengine.state.State;
import org.stateengine.storage.IDBConnection;

public class AddEnchainementCycleAction extends AActionOnState {

    private static final long serialVersionUID = 1L;

    public AddEnchainementCycleAction(String label) {
        super(label);
    }

    public void perform(IDBConnection connection, IStateEngineUser loggedUser, HttpSession sess, HttpServletRequest request, IStateContainer parent, State state) {
        parent.setState(new EditEnchainementCycleState((EnchainementCycle) EnchainementCycleFactory.instance().createNew(((UtilisateurAdapter) loggedUser).getUtilisateur()), state));
    }
}
