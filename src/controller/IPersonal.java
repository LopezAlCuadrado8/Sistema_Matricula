package controller;

import model.Personal;

import repository.IRepositorioCRUD;
import repository.IRepositorioGET;

public interface IPersonal extends IRepositorioGET, IRepositorioCRUD<Personal> {

}
