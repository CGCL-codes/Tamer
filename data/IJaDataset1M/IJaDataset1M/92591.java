package de.iritgo.aktera.authentication;

import java.security.Principal;

/**
 * <p>
 * Basic implementation of the Principal class. By implementing our own
 * Principal for our application, we can more easily add and remove
 * instances of our principals in the authenticated Subject during the
 * login and logout process.
 *
 * @see java.security.Principal
 * @author  Paul Feuer and John Musser
 * @version 1.0
 */
public class UserDescripPrincipal implements Principal, java.io.Serializable {

    private String name;

    /**
	 * Create a <code>LoginPrincipal</code> with no
	 * user name.
	 *
	 */
    public UserDescripPrincipal() {
        name = "";
    }

    /**
	 * Create a <code>LoginPrincipal</code> using a
	 * <code>String</code> representation of the
	 * user name.
	 *
	 * <p>
	 *
	 * @param name the user identification number (UID) for this user.
	 *
	 */
    public UserDescripPrincipal(String newName) {
        name = newName;
    }

    /**
	 * Compares the specified Object with this
	 * <code>LoginPrincipal</code>
	 * for equality.  Returns true if the given object is also a
	 * <code>LoginPrincipal</code> and the two
	 * LoginPrincipals have the same name.
	 *
	 * <p>
	 *
	 * @param o Object to be compared for equality with this
	 *                <code>LoginPrincipal</code>.
	 *
	 * @return true if the specified Object is equal equal to this
	 *                <code>LoginPrincipal</code>.
	 */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof UserDescripPrincipal) {
            if (((UserDescripPrincipal) o).getName().equals(name)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
	 * Return a hash code for this <code>LoginPrincipal</code>.
	 *
	 * <p>
	 *
	 * @return a hash code for this <code>LoginPrincipal</code>.
	 */
    public int hashCode() {
        return name.hashCode();
    }

    /**
	 * Return a string representation of this
	 * <code>LoginPrincipal</code>.
	 *
	 * <p>
	 *
	 * @return a string representation of this
	 *                <code>LoginPrincipal</code>.
	 */
    public String toString() {
        return getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1) + ": " + name;
    }

    /**
	 * Return the user name for this
	 * <code>LoginPrincipal</code>.
	 *
	 * <p>
	 *
	 * @return the user name for this
	 *                <code>LoginPrincipal</code>
	 */
    public String getName() {
        return name;
    }
}
