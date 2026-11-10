package interfaces;

import model.Carrera;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface ICarrera extends IRepositorioGET, IRepositorioCRUD<Carrera> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID (EL CRUD BASE USA CÓDIGO STRING)
    void EditarPorId(Carrera carrera);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}