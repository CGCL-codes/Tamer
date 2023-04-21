package gestorDeEstadisticas;

/**
 * Esta intefaz se utiliza para que  el modulo que reciba los datos notifique
 * de su llegada a los observadores registrados.
 * Ejemplo de uso:
 *
 *  class clase1 implements NotificadorDatos{
 *      List<ObservadorDatos> listaDeObservadores;
 *      void addObservador(ObservadorDatos observador){
 *          listaDeObservadores.add(observador);
 *      }
 *      void addDatos(long longitud) {
 *          for (ObservadorDatos obs : listaDeObservadores)
 *              obs.llegadaDatos(longitud);
 *      }
 *      void addFichero() {
 *          for (ObservadorDatos obs : listaDeObservadores)
 *              obs.llegadaFichero(1);
 *      }
 *      
 *  }
 * @author Qiang
 */
public interface NotificadorDatos {

    void addSubidaDatos(double longitud);

    void addObservador(ObservadorDatos observador);
}
