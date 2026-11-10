package interfaces;

import model.Estudiante;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IEstudiante extends IRepositorioGET, IRepositorioCRUD<Estudiante> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Estudiante estudiante);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}