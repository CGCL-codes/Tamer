package edu.vt.middleware.ldap.auth.ext;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import edu.vt.middleware.ldap.auth.AccountState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the state of an eDirectory account.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public class EDirectoryAccountState extends AccountState {

    /**
   * Enum to define edirectory errors. See
   * http://support.novell.com/docs/Tids/Solutions/10067240.html and
   * http://www.novell.com/documentation/nwec/nwec_enu/nwec_nds_error_codes.html
   */
    public enum Error implements AccountState.Error {

        /** failed authentication. */
        FAILED_AUTHENTICATION(-669), /** password expired. binds still succeed. */
        PASSWORD_EXPIRED(-223), /** bad password. */
        BAD_PASSWORD(-222), /** account expired. */
        ACCOUNT_EXPIRED(-220), /** maximum logins exceeded. */
        MAXIMUM_LOGINS_EXCEEDED(-217), /** login time limited. */
        LOGIN_TIME_LIMITED(-218), /** login lockout. */
        LOGIN_LOCKOUT(-197);

        /** pattern to find decimal code in edirectory messages. */
        private static final Pattern PATTERN = Pattern.compile("NDS error: (.+) \\((-\\d+)\\)");

        /** underlying error code. */
        private int code;

        /**
     * Creates a new edirectory error.
     *
     * @param  i  error code
     */
        Error(final int i) {
            code = i;
        }

        /** {@inheritDoc} */
        @Override
        public int getCode() {
            return code;
        }

        /** {@inheritDoc} */
        @Override
        public String getMessage() {
            return name();
        }

        /** {@inheritDoc} */
        @Override
        public void throwSecurityException() throws LoginException {
            switch(this) {
                case FAILED_AUTHENTICATION:
                    throw new FailedLoginException(name());
                case PASSWORD_EXPIRED:
                    throw new CredentialExpiredException(name());
                case BAD_PASSWORD:
                    throw new FailedLoginException(name());
                case ACCOUNT_EXPIRED:
                    throw new AccountExpiredException(name());
                case MAXIMUM_LOGINS_EXCEEDED:
                    throw new AccountLockedException(name());
                case LOGIN_TIME_LIMITED:
                    throw new AccountLockedException(name());
                case LOGIN_LOCKOUT:
                    throw new AccountLockedException(name());
                default:
                    throw new IllegalStateException("Unknown active directory error: " + this);
            }
        }

        /**
     * Returns the error for the supplied integer constant.
     *
     * @param  code  to find error for
     *
     * @return  error
     */
        public static Error valueOf(final int code) {
            for (Error e : Error.values()) {
                if (e.getCode() == code) {
                    return e;
                }
            }
            return null;
        }

        /**
     * Parses the supplied error messages and returns the corresponding error
     * enum. Attempts to find {@link #PATTERN} and parses the second group match
     * as a decimal integer.
     *
     * @param  message  to parse
     *
     * @return  edirectory error
     */
        public static Error parse(final String message) {
            if (message != null) {
                final Matcher matcher = PATTERN.matcher(message);
                if (matcher.find()) {
                    try {
                        return Error.valueOf(Integer.parseInt(matcher.group(2)));
                    } catch (NumberFormatException e) {
                        final Logger l = LoggerFactory.getLogger(Error.class);
                        l.warn("Error parsing edirectory error", e);
                    }
                }
            }
            return null;
        }
    }

    /** edirectory specific enum. */
    private final Error edError;

    /**
   * Creates a new edirectory account state.
   *
   * @param  exp  account expiration
   * @param  remaining  number of logins available
   */
    public EDirectoryAccountState(final Calendar exp, final int remaining) {
        super(new AccountState.Warning(exp, remaining));
        edError = null;
    }

    /**
   * Creates a new edirectory account state.
   *
   * @param  error  containing authentication failure details
   */
    public EDirectoryAccountState(final EDirectoryAccountState.Error error) {
        super(error);
        edError = error;
    }

    /**
   * Returns the edirectory error for this account state.
   *
   * @return  edirectory error
   */
    public EDirectoryAccountState.Error getEDirectoryError() {
        return edError;
    }
}
