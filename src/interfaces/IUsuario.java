package interfaces;

import model.Usuario;
import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IUsuario extends IRepositorioGET, IRepositorioCRUD<Usuario> {
    // SE AGREGA MÉTODO PARA ACTUALIZAR POR ID
    void EditarPorId(Usuario usuario);
    // SE AGREGA MÉTODO PARA BORRAR POR ID
    void BorrarPorId(int id);
}