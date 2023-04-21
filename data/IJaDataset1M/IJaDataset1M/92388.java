package br.com.fiapbank.negocio.cliente;

import java.util.List;
import br.com.fiapbank.dominio.Conta;
import br.com.fiapbank.dominio.Transacao;
import br.com.fiapbank.negocio.NegocioException;

/**
 * possui as operacoes que o cliente pode realizar no sistema
 * 
 * 
 * @author robson
 *
 */
public interface ClienteNegocio {

    public List<Transacao> getTransacoes(Conta conta, String senha, String fraseSecreta) throws NegocioException;

    public Conta getSaldo(Conta conta, String senha, String fraseSecreta) throws NegocioException;

    public void alterarFraseSecreta(Conta conta, String senha, String fraseSecreta, String novaFraseSecreta) throws NegocioException;

    public void aplicarPoupanca(Conta conta, String senha, String fraseSecreta, Double valor) throws NegocioException;

    public void resgatarPoupanca(Conta conta, String senha, String fraseSecreta, Double valor) throws NegocioException;

    public void realizarTransferencia(Conta conta, String senha, String fraseSecreta, Double valor, Conta contaDestino) throws NegocioException;

    public void realizarDocTed(Conta conta, String senha, String fraseSecreta, Double valor, Conta contadestino) throws NegocioException;
}
