package interfaces;

import model.Pago;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IPago extends IRepositorioGET, IRepositorioCRUD<Pago> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Pago pago);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}