package org.simpleframework.http.connect;

import java.io.IOException;
import java.net.Socket;

/**
 * The <code>Configurator</code> object is used to allow custom
 * socket configurations to be used for the connected clients. 
 * This is provided as it is clear that a single configuration
 * cannot be suitable for all platforms. For example, it might
 * nessecary to change the SO_LINGER option which controls how
 * long the TCP socket remains lingering waiting for and ACK 
 * after the client closes the connection. On Linux, incorrect
 * configurations can lead to exhaustion of file handles.
 * 
 * @author Niall Gallagher
 */
public interface Configurator {

    /**
    * This method is used to configure the TCP connection
    * before the <code>Pipeline</code> is created. This will
    * enable custom implementations to be used that can set
    * various socket options that suit the server platform.
    *
    * @param sock this is the newly connected HTTP pipeline
    */
    public void configure(Socket sock) throws IOException;
}
