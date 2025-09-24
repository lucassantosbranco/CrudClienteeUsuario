package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO extends AbstractDAO implements CrudRepository<Usuario> {

    private String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    @Override
    public boolean salvar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (login, senha, nome, ativo) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, criptografarSenha(u.getSenha()));
            ps.setString(3, u.getNome());
            ps.setBoolean(4, u.isAtivo());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
            return true;
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, login, senha, nome, ativo FROM usuario WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("nome"),
                        rs.getBoolean("ativo")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, login, nome, ativo FROM usuario ORDER BY nome";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("nome"),
                    rs.getBoolean("ativo")
                );
                lista.add(u);
            }
        }
        return lista;
    }

    @Override
    public boolean atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuario SET login = ?, nome = ?, ativo = ? WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, u.getNome());
            ps.setBoolean(3, u.isAtivo());
            ps.setInt(4, u.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remover(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT id, login, senha, nome, ativo FROM usuario WHERE login = ? AND ativo = 1";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("senha");
                    if (BCrypt.checkpw(senha, hash)) {
                        return new Usuario(
                            rs.getInt("id"),
                            rs.getString("login"),
                            hash,
                            rs.getString("nome"),
                            rs.getBoolean("ativo")
                        );
                    }
                }
            }
        }
        return null;
    }

    public boolean existeLogin(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
}
