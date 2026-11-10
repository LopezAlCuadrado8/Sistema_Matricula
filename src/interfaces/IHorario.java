package interfaces;

import model.Horario;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IHorario extends IRepositorioGET, IRepositorioCRUD<Horario> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Horario horario);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}