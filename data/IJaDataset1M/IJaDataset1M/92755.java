package org.tamacat.httpd.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.tamacat.httpd.exception.UnauthorizedException;
import org.tamacat.httpd.util.RequestUtils;
import org.tamacat.util.StringUtils;
import org.tamacat.util.UniqueCodeGenerator;

/**
 * Implements of WS-Security Extenstion (WSSE) authentication.
 */
public class WSSEAuthProcessor extends AbstractAuthProcessor {

    static final String AUTHORIZATION = "Authorization";

    static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    static final String X_WSSE_HEADER = "X-WSSE";

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, HttpContext context) {
        String path = RequestUtils.getRequestPath(request);
        if (isFreeAccessExtensions(path) == false) {
            try {
                String remoteUser = checkUser(request, response, context);
                context.setAttribute(remoteUserKey, remoteUser);
            } catch (UnauthorizedException e) {
                response.setStatusCode(HttpStatus.SC_UNAUTHORIZED);
                setWWWAuthenticateHeader(response);
                throw e;
            }
        }
    }

    /**
	 * Login check with AuthComponent.
	 * @param request
	 * @param response
	 * @param context
	 * @return login username in request parameter.
	 * @throws UnauthorizedException
	 */
    protected String checkUser(HttpRequest request, HttpResponse response, HttpContext context) throws UnauthorizedException {
        Header wsseAuthLine = request.getFirstHeader(X_WSSE_HEADER);
        if (wsseAuthLine != null && StringUtils.isNotEmpty(wsseAuthLine.getValue())) {
            String line = wsseAuthLine.getValue().replaceFirst("UsernameToken ", "");
            WSSE wsse = new WSSE(line);
            if (authComponent != null) {
                AuthUser user = authComponent.getAuthUser(wsse.getUsername(), context);
                String username = wsse.getUsername();
                String password = wsse.getPasswordDigest();
                String passwordDigest = getPasswordDigest(wsse, user);
                if (username != null && password != null && username.equals(user.getAuthUsername()) && password.equals(passwordDigest)) {
                    if (singleSignOn != null) {
                        singleSignOn.sign(username, request, response, context);
                    }
                    return user.getAuthUsername();
                }
            }
        } else if (singleSignOn != null && singleSignOn.isSigned(request, context)) {
            return singleSignOn.getSignedUser(request, context);
        }
        throw new UnauthorizedException();
    }

    /**
	 * Get the password digest.
	 * @param wsse
	 * @param user
	 * @return password digest
	 */
    protected String getPasswordDigest(WSSE wsse, AuthUser user) {
        String password = user.getAuthPassword();
        if (password == null || wsse.getNonce() == null || wsse.getCreated() == null) {
            throw new UnauthorizedException();
        }
        byte[] nonce = new Base64().decode(wsse.getNonce().getBytes());
        byte[] created = wsse.getCreated().getBytes();
        byte[] pb = password.getBytes();
        byte[] digest = new byte[nonce.length + created.length + pb.length];
        System.arraycopy(nonce, 0, digest, 0, nonce.length);
        System.arraycopy(created, 0, digest, nonce.length, created.length);
        System.arraycopy(pb, 0, digest, nonce.length + created.length, pb.length);
        return new String(new Base64().encode(getSHA1(digest))).replace("\r", "").replace("\n", "");
    }

    /**
	 * Set the "WWW-Authenticate" response header of WSSE authenticate realm.
	 * @param response
	 */
    protected void setWWWAuthenticateHeader(HttpResponse response) {
        response.addHeader(WWW_AUTHENTICATE, "WSSE profile=\"UsernameToken\"");
    }

    /**
	 * Get the nonce.
	 * @return nonce
	 */
    protected String getNonce() {
        String nonce = UniqueCodeGenerator.generate();
        return new String(new Base64().encode(nonce.getBytes()));
    }

    /**
	 * Get the SHA1 digest.
	 * @param digest
	 * @return bytes of digest.
	 */
    protected byte[] getSHA1(byte[] digest) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
	 * Bean class for WSSE attributes. 
	 */
    static class WSSE {

        private Map<String, String> params = new LinkedHashMap<String, String>();

        WSSE(String line) {
            String[] params = line.split(",");
            for (String keyValue : params) {
                String[] param = keyValue.trim().split("=");
                if (param != null && param.length >= 2) {
                    String key = param[0].trim();
                    StringBuilder value = new StringBuilder(param[1]);
                    if (param.length > 2) {
                        for (int i = 2; i < param.length; i++) {
                            value.append("=" + param[i]);
                        }
                    }
                    setParam(key, value.toString().replaceFirst("^\"", "").replaceFirst("\"$", ""));
                }
            }
        }

        public void setParam(String key, String value) {
            this.params.put(key, value);
        }

        public Map<String, String> getParams() {
            return params;
        }

        public String getUsername() {
            return params.get("Username");
        }

        public String getPasswordDigest() {
            return params.get("PasswordDigest");
        }

        public String getNonce() {
            return params.get("Nonce");
        }

        public String getCreated() {
            return params.get("Created");
        }
    }
}
