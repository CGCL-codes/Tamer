package net.community.chest.net.proto.text.ssh.message.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.community.chest.net.proto.text.ssh.SSHMsgCode;
import net.community.chest.net.proto.text.ssh.SSHProtocol;
import net.community.chest.net.proto.text.ssh.io.SSHInputDataDecoder;
import net.community.chest.net.proto.text.ssh.io.SSHOutputDataEncoder;
import net.community.chest.net.proto.text.ssh.message.AbstractSSHMsgEncoder;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 2, 2009 9:20:05 AM
 */
public class UserAuthRequest extends AbstractSSHMsgEncoder<UserAuthRequest> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8128303393120368634L;

    public UserAuthRequest() {
        super(SSHMsgCode.SSH_MSG_USERAUTH_REQUEST);
    }

    private String _userName;

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }

    private String _svcName;

    public String getServiceName() {
        return _svcName;
    }

    public void setServiceName(String n) {
        _svcName = n;
    }

    public static final String PUBLIC_KEY_METHOD_NAME = "publickey", PASSWORD_METHOD_NAME = "password", HOST_BASED_METHOD_NAME = "hostbased", NO_METHOD_NAME = "none";

    private String _mthdName;

    public String getMethodName() {
        return _mthdName;
    }

    public void setMethodName(String n) {
        _mthdName = n;
    }

    @Override
    public UserAuthRequest read(InputStream in) throws IOException {
        setUserName(SSHProtocol.readUTF8String(in));
        setServiceName(SSHProtocol.readMethodName(in));
        setMethodName(SSHProtocol.readMethodName(in));
        return this;
    }

    @Override
    public UserAuthRequest decode(SSHInputDataDecoder in) throws IOException {
        if (null == in) throw new IOException("decode(" + getMsgCode() + ") no " + SSHInputDataDecoder.class.getSimpleName() + " instance");
        setUserName(in.readUTF());
        setServiceName(in.readASCII());
        setMethodName(in.readASCII());
        return this;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        SSHProtocol.writeNonNullStringBytes(out, false, getUserName());
        SSHProtocol.writeMethodName(out, getServiceName());
        SSHProtocol.writeMethodName(out, getMethodName());
    }

    @Override
    public void encode(SSHOutputDataEncoder out) throws IOException {
        if (null == out) throw new IOException("encode(" + getMsgCode() + ") no " + SSHOutputDataEncoder.class.getSimpleName() + " instance");
        out.writeUTF(getUserName());
        out.writeASCII(getServiceName());
        out.writeASCII(getMethodName());
    }
}
