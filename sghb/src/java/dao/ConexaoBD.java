/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por criar e gerir a ligação (conexão) com a base de dados MySQL.
 * É como o "porteiro" da nossa base de dados.
 */
public class ConexaoBD {

    // --- Configure aqui os seus dados de conexão com a base de dados ---
    // O URL diz onde está a base de dados e qual o nome dela.
    private static final String URL = "jdbc:mysql://localhost:3306/sghb_db?useTimezone=true&serverTimezone=UTC";
    // Nome de utilizador para aceder à base de dados. "root" é comum em ambientes de teste.
    private static final String USUARIO = "root"; // IMPORTANTE: Mude para o seu usuário do MySQL!
    // Senha para aceder à base de dados.
    private static final String SENHA = ""; // IMPORTANTE: Mude para a sua senha do MySQL!
    // Nome do "motorista" (driver) que o Java usa para falar com MySQL.
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Método principal para obter uma ligação à base de dados.
     * As outras classes DAO vão chamar este método sempre que precisarem de falar com a base de dados.
     * @return um objeto Connection (a ligação).
     * @throws SQLException se acontecer algum erro ao tentar ligar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Passo 1: Carregar o "motorista" (driver) do MySQL.
            Class.forName(DRIVER);
            // Passo 2: Pedir ao DriverManager para criar uma ligação usando o URL, usuário e senha.
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            // Este erro acontece se o Java não encontrar o ficheiro .jar do driver do MySQL.
            System.err.println("O 'motorista' (driver) JDBC do MySQL não foi encontrado. Verifique as bibliotecas do projeto.");
            throw new SQLException("Driver JDBC do MySQL não encontrado.", e);
        }
    }
}


