package org.basex.core.proc;

import static org.basex.core.Text.*;
import org.basex.core.Proc;
import org.basex.core.User;
import org.basex.core.Commands.Cmd;
import org.basex.core.Commands.CmdCreate;
import org.basex.io.PrintOutput;

/**
 * Evaluates the 'create user' command and creates a new user.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * @author Christian Gruen
 */
public final class CreateUser extends Proc {

    /**
   * Default constructor.
   * @param name user name
   * @param pw password
   */
    public CreateUser(final String name, final String pw) {
        super(User.ADMIN, name, pw);
    }

    @Override
    protected boolean exec(final PrintOutput out) {
        final String user = args[0];
        final String pass = args[1];
        return pass == null || pass.isEmpty() ? error(PASSNO, user) : context.users.create(user, pass) ? info(USERCREATE, user) : error(USERKNOWN, user);
    }

    @Override
    public String toString() {
        return Cmd.CREATE + " " + CmdCreate.USER + args();
    }
}
