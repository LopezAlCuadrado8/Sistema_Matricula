package interfaces;

import model.Aula;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IAula extends IRepositorioGET, IRepositorioCRUD<Aula> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Aula aula);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}