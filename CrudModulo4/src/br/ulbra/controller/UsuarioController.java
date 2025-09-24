package br.ulbra.controller;

import br.ulbra.DAO.UsuarioDAO;
import br.ulbra.model.Usuario;
import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private UsuarioDAO dao = new UsuarioDAO();

    public boolean salvar(Usuario u) {
        try {
            return dao.salvar(u);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Usuario buscar(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Usuario> listar() {
        try {
            return dao.listar();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean atualizar(Usuario u) {
        try {
            return dao.atualizar(u);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean remover(int id) {
        try {
            return dao.remover(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Usuario autenticar(String login, String senha) {
        try {
            return dao.autenticar(login, senha);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean existeLogin(String login) {
        try {
            return dao.existeLogin(login);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
    
  
