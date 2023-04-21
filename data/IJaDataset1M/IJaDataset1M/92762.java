package dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.hibernate.HibernateException;
import modelo.Pra_quem_assumiu;

public class Pra_quem_assumiuDAO extends DAOMenor {

    public Set validarObjeto(Set assumiu) throws HibernateException {
        if (assumiu == null) {
            return null;
        }
        HashSet<Pra_quem_assumiu> retorno = new HashSet<Pra_quem_assumiu>(assumiu.size());
        for (Iterator it = assumiu.iterator(); it.hasNext(); ) {
            Pra_quem_assumiu objeto = (Pra_quem_assumiu) it.next();
            retorno.add((Pra_quem_assumiu) this.validarObjeto(objeto, objeto.getNome()));
        }
        return retorno;
    }
}
