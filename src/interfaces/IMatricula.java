package interfaces;

import model.Matricula;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IMatricula extends IRepositorioGET, IRepositorioCRUD<Matricula> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Matricula matricula);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
    // SE AGREGA MÉTODO PARA ANULAR MATRÍCULA (LLAMA AL SP)
    void Anular(int id);
}