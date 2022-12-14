package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {

	/* Módulo de conexão */
	// Parâmetros de conexão
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String password = "root";

	// Método de conexão
	private Connection conectar() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// CRUD CREATE
	public void inserirContato(JavaBeans contato) {
		String create = "insert into contatos (nome, fone, email) values (?,?,?)";

		try {
			// abrir conexao
			Connection con = conectar();

			// preparar a query para execução
			PreparedStatement pst = con.prepareStatement(create);

			// substituior os parametros ? pelo JavaBeans
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());

			// executar a query
			pst.executeUpdate();

			// encerrar conexao com o banco
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// CRUD READ
	public ArrayList<JavaBeans> listarContatos() {

		ArrayList<JavaBeans> contatos = new ArrayList<>();

		String read = "select * from contatos order by nome";

		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read);

			ResultSet rs = pst.executeQuery();

			// o laço abaixo será executado enquanto houver contatos
			while (rs.next()) {
				Integer id = rs.getInt(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);

				// populando o ArrayList
				contatos.add(new JavaBeans(id, nome, fone, email));
			}
			con.close();
			return contatos;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void selecionarContato(JavaBeans contato) {
		String read2 = "select * from contatos where id = ?";

		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read2);
			pst.setInt(1, contato.getId());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				contato.setId(rs.getInt(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// editar contato
	public void alterarContato(JavaBeans contato) {
		String create = "update contatos set nome = ?, fone = ?, email = ? where id = ?";

		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(create);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			pst.setInt(4, contato.getId());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void deletarContato(JavaBeans contato) {
		String delete = "delete from contatos where id = ?";

		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(delete);
			pst.setInt(1, contato.getId());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
