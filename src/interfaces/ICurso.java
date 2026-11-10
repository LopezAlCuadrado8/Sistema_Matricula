package interfaces;

import model.Curso;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface ICurso extends IRepositorioGET, IRepositorioCRUD<Curso> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Curso curso);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}