package interfaces;

import model.Docente;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IDocente extends IRepositorioGET, IRepositorioCRUD<Docente> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Docente docente);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}