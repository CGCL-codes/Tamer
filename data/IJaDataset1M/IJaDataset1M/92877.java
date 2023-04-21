package edu.cebanc.spring.biblioteca.service;

import java.util.List;
import edu.cebanc.spring.biblioteca.bean.AutorBean;
import edu.cebanc.spring.biblioteca.bean.BajaBean;
import edu.cebanc.spring.biblioteca.bean.EditorialBean;
import edu.cebanc.spring.biblioteca.bean.EjemplarBean;
import edu.cebanc.spring.biblioteca.bean.GeneroBean;
import edu.cebanc.spring.biblioteca.bean.IdiomaBean;
import edu.cebanc.spring.biblioteca.bean.LibroBean;

public interface LibrosService {

    public List<LibroBean> listadoLibros();

    public void guardarLibro(LibroBean libro);

    public LibroBean cargarLibro(String id);

    public void eliminarLibro(String id);

    public List<AutorBean> getAutores(LibroBean libro);

    public void addAutorToLibro(LibroBean libro, AutorBean autor);

    public void deleteAutorLibro(LibroBean libro, String id_autor);

    public List<GeneroBean> getGeneros(LibroBean libro);

    public void addGeneroToLibro(LibroBean libro, GeneroBean genero);

    public void deleteGeneroLibro(LibroBean libro, String id_genero);

    public List<EditorialBean> getEditoriales();

    public List<IdiomaBean> getIdiomas();

    public void addEjemplarToLibro(LibroBean libro, EjemplarBean genero);

    public void bajaEjemplarLibro(BajaBean baja);

    public EjemplarBean cargarEjemplar(String id);

    public void updateEjemplar(EjemplarBean ejemplar);
}
