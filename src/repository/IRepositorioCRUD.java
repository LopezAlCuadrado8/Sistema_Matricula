package repository;

public interface IRepositorioCRUD<T> {
    void Registrar(T registro);
    void Editar(T registro);
    void Borrar(String codigo);
}
