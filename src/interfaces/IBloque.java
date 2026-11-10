package interfaces;

import model.Bloque;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IBloque extends IRepositorioGET, IRepositorioCRUD<Bloque> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Bloque bloque);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}