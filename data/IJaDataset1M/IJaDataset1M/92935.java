package com.eidansoft.formularioHTML.impl;

import com.eidansoft.formularioHTML.IFormulario;
import com.eidansoft.formularioHTML.IParametro;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Alejandro Lorente
 * @author  eidansoft ARROBA gmail PUNTITO com
 * @version 1.0
 * 
 * Esta clase permite el comodo uso de los tipicos formularios usados
 * en la web, y mediante el se puede enviar lo q se necesite mediante los 
 * metodos POST o GET simulando un navegador
 * 
 * GET tiene un limite de 255 caracteres en la linea
 * de parametros, lo cual no comprueba esta clase. Esto es una limitacion propia del protocolo.
 *
 * Para usar, se crea una nueva instancia, se añaden los 
 * parametros del formulario y se envia con el metodo elegido.
 *
 * Los parametros son objetos de tipo "Parametro"
 */
public class FormularioImpl implements IFormulario {

    /** Los parametros del formulario */
    private Set<IParametro> parametros;

    /** Logger */
    private Logger log = Logger.getLogger(FormularioImpl.class.getName());

    /** Host de conexión */
    private String host;

    /** Ruta de conexión */
    private String ruta;

    /** Protocolo de conexión */
    private String protocolo;

    /** Cadena de identificación como navegador */
    private String identificacionNavegador = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.12) Gecko/20080129 Iceweasel/2.0.0.12 (Debian-2.0.0.12-0etch1)";

    /** Constructor por defecto
     */
    public FormularioImpl() {
        parametros = new TreeSet<IParametro>();
    }

    /** Añade un nuevo parametro
     *  @param nombre El nombre del parámetro
     *  @param valor El valor del parámetro
     */
    public void addParametro(String nombre, String valor) throws ErrorFormularioException {
        IParametro tmp = new ParametroImpl(nombre, valor);
        parametros.add(tmp);
    }

    /** Esta funcion crea la cadena de parametros con sus respectivos
     *  valores, todos separados por el operador ampersand (&)
     *  que es el que se utiliza en el estandard de los formularios Web*/
    private String getParametros() {
        Iterator<IParametro> it = parametros.iterator();
        String cad = "";
        int ancho = 0;
        while (it.hasNext()) {
            IParametro tmp = it.next();
            cad = cad + tmp.getParametro() + "&";
        }
        if (cad.length() - 1 < 0) ancho = 0; else ancho = cad.length() - 1;
        cad = cad.substring(0, ancho);
        return cad;
    }

    /**
     * Encargado de devolver los diferentes atributos que se pasarán al formulario con la estructura que necesitan
     * los datos para ser enviados como POST
     */
    private String getParametrosPOST() {
        String res = this.getParametros();
        return res;
    }

    /** Esta funcion envia el formulario a través del metodo POST.
     *  Requiere conocer el HOST al q se va a conectar,
     *  la ruta de la página que recibirá el formulario de datos
     *  y el protocolo a utilizar, que puede ser:
     *  <ul>
     *  <li>HTTP</li>
     *  <li>HTTPS</li>
     *  </ul>
     * 
     *  La funcion ademas devuelve la respuesta del servidor tras ejecutar
     *  el formulario. Si esa respuesta es una pagina Web en html, devolverá
     *  el codigo html tal cual, que tendrá que ser interpretado después.
     */
    public String enviaPOST() {
        String respuesta = "";
        try {
            URL url = new URL(getProtocolo(), getHost(), getRuta());
            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("USER-AGENT", getIdentificacionNavegador());
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            String content = this.getParametrosPOST();
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String str;
            while (null != ((str = input.readLine()))) {
                respuesta = respuesta + str;
            }
            input.close();
        } catch (MalformedURLException me) {
            log.severe("URL malformada: " + me.getMessage());
        } catch (IOException ioe) {
            log.severe("Excepcion de IO " + ioe.getMessage());
        }
        return respuesta;
    }

    /** Devuelve el host seleccionado */
    public String getHost() {
        return host;
    }

    /** Establece el Host de conexion */
    public void setHost(String host) throws ErrorFormularioException {
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        Pattern patron1 = Pattern.compile("(\\w{3,}[.](.){2,4})$");
        Matcher terminacion = patron1.matcher(host);
        if (terminacion.find()) {
            this.host = host;
        } else {
            throw new ErrorFormularioException("El HOST introducido no es válido.");
        }
    }

    /** Devuelve la ruta seleccionada */
    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        if (!ruta.startsWith("/")) {
            ruta = "/".concat(ruta);
        }
        this.ruta = ruta;
    }

    /** Devuelve el protocolo seleccionado */
    public String getProtocolo() {
        return protocolo;
    }

    /** Establece el protocolo de conexión, sólo estan permitidos los protocolos HTTP y HTTPS.
     */
    public void setProtocolo(String protocolo) throws ErrorFormularioException {
        if (protocolo.equalsIgnoreCase("HTTP") || protocolo.equalsIgnoreCase("HTTPS")) {
            this.protocolo = protocolo;
        } else {
            throw new ErrorFormularioException("Sólo son válidos los protocolos 'HTTP' y 'HTTPS'. Seleccione un protocolo válido.");
        }
    }

    /** Envia el formulario por GET */
    public String enviaGET() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getIdentificacionNavegador() {
        return identificacionNavegador;
    }

    public void setIdentificacionNavegador(String identificacionNavegador) {
        this.identificacionNavegador = identificacionNavegador;
    }
}
