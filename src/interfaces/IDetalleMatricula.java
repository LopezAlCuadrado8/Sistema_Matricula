package interfaces;

import model.DetalleMatricula;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IDetalleMatricula extends IRepositorioGET, IRepositorioCRUD<DetalleMatricula> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR NOTA
    void EditarPorId(DetalleMatricula detalle);
    // SE AGREGA MÉTODO PARA RETIRAR CURSO (ESTADO = 0)
    void Retirar(int idMatricula, int idCurso);
}