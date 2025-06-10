-- 1. Criar a base de dados
CREATE DATABASE IF NOT EXISTS sgrh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. Usar a base de dados criada
USE sgrh;

-- 3. Tabela para os funcionários que vão operar o sistema
-- O técnico deve inserir os dados diretamente nesta tabela.
CREATE TABLE funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo VARCHAR(100) -- Ex: Recepcionista, Gerente
);

-- 4. Tabela para os quartos do hotel
CREATE TABLE quartos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_quarto VARCHAR(10) NOT NULL UNIQUE,
    tipo_quarto VARCHAR(100) NOT NULL, -- Ex: Simples, Duplo, Suite
    preco_por_noite DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'Vago' -- Vago, Ocupado, Manutenção
);

-- 5. Tabela para os hóspedes
CREATE TABLE hospedes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    tipo_documento VARCHAR(100) NOT NULL, -- Ex: BI, Passaporte, DIRE
    numero_documento VARCHAR(100) NOT NULL UNIQUE,
    nacionalidade VARCHAR(100) NOT NULL,
    contacto VARCHAR(100),
    email VARCHAR(255)
);

-- 6. Tabela de reservas (hospedagens) que liga um hóspede a um quarto
CREATE TABLE hospedagens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_quarto INT NOT NULL,
    id_hospede INT NOT NULL,
    id_funcionario INT NOT NULL, -- Funcionário que fez o check-in
    data_checkin DATETIME NOT NULL,
    data_checkout DATETIME, -- Fica nulo até o check-out ser feito
    estado VARCHAR(50) NOT NULL, -- Ativa, Finalizada
    FOREIGN KEY (id_quarto) REFERENCES quartos(id),
    FOREIGN KEY (id_hospede) REFERENCES hospedes(id),
    FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id)
);

-- Inserir um funcionário de teste para poder fazer login
INSERT INTO funcionarios (nome_completo, username, senha, cargo) VALUES
('Admin Principal', 'admin', '1234', 'Gerente');

================================================================================================
==============================================================================================
TODAS CLASSES
AQUI




Análise e Melhorias do Sistema de Gestão de Reservas de Hotel (SGRH)A seguir, encontrará o seu código totalmente comentado e uma análise detalhada sobre a qualidade do software, com sugestões práticas para melhorá-lo, especialmente no contexto da disciplina de Teste e Qualidade de Software.1. Código-Fonte ComentadoAqui está o seu projeto com comentários no padrão Javadoc e inline para clarificar a função de cada componente.Pacote sgrh (Camada de Apresentação/Principal)Este pacote contém a classe principal que interage com o utilizador através da consola.// FICHEIRO: Sgrh.java
package sgrh;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import sgrh.dao.FuncionarioDAO;
import sgrh.dao.HospedagemDAO;
import sgrh.dao.HospedeDAO;
import sgrh.dao.QuartoDAO;
import sgrh.model.Funcionario;
import sgrh.model.Hospedagem;
import sgrh.model.Hospede;
import sgrh.model.Quarto;

/**
 * Classe principal que serve como ponto de entrada e controlador da aplicação 
 * de gestão de hotel via consola. Orquestra a interação do utilizador
 * com as funcionalidades do sistema.
 */
public class Sgrh {

    // Atributos estáticos para manter o estado da aplicação
    private static Funcionario funcionarioLogado; // Guarda o funcionário que fez login
    private static final Scanner scanner = new Scanner(System.in); // Objeto para ler a entrada do utilizador
    private static final QuartoDAO quartoDAO = new QuartoDAO(); // Objeto de acesso a dados para Quartos
    private static final HospedeDAO hospedeDAO = new HospedeDAO(); // Objeto de acesso a dados para Hóspedes
    private static final HospedagemDAO hospedagemDAO = new HospedagemDAO(); // Objeto de acesso a dados para Hospedagens

    /**
     * Método principal que inicia a aplicação.
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        System.out.println("===================================================");
        System.out.println("=== SISTEMA DE GESTÃO DE RESERVAS DE HOTEL (SGRH) ===");
        System.out.println("===================================================");

        // A aplicação só prossegue se o login for bem-sucedido
        if (tentarLogin()) {
            exibirMenuPrincipal();
        }

        System.out.println("\nObrigado por usar o sistema. Até a próxima!");
        scanner.close(); // Fecha o scanner para libertar recursos
    }

    /**
     * Controla o processo de login, permitindo um máximo de 3 tentativas.
     * @return {@code true} se o login for bem-sucedido, {@code false} caso contrário.
     */
    private static boolean tentarLogin() {
        int tentativas = 3;
        while (tentativas > 0) {
            System.out.println("\n--- LOGIN DE FUNCIONÁRIO ---");
            System.out.print("Utilizador: ");
            String username = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            // Cria um objeto Funcionario para passar os dados de login
            Funcionario objFuncionario = new Funcionario();
            objFuncionario.setUsername(username);
            objFuncionario.setSenha(senha);

            // Tenta autenticar o funcionário através da camada de acesso a dados (DAO)
            FuncionarioDAO dao = new FuncionarioDAO();
            funcionarioLogado = dao.autenticacaoFuncionario(objFuncionario);

            if (funcionarioLogado != null) {
                System.out.println("\nLogin realizado com sucesso! Bem-vindo(a), " + funcionarioLogado.getNomeCompleto() + ".");
                return true;
            } else {
                tentativas--;
                System.out.println("Utilizador ou senha inválida. Tentativas restantes: " + tentativas);
            }
        }
        System.out.println("Excedeu o número de tentativas de login. O sistema será encerrado.");
        return false;
    }

    /**
     * Exibe o menu principal da aplicação e processa a escolha do utilizador.
     * O menu fica em loop até o utilizador escolher a opção de sair (0).
     */
    private static void exibirMenuPrincipal() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- PAINEL PRINCIPAL ---");
            System.out.println("1. Gestão de Quartos");
            System.out.println("2. Realizar Check-in de Hóspede");
            System.out.println("3. Realizar Check-out (Funcionalidade Futura)");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        menuGestaoQuartos();
                        break;
                    case 2:
                        realizarCheckIn();
                        break;
                    case 3:
                        System.out.println("\nFuncionalidade ainda não implementada.");
                        break;
                    case 0:
                        break; // Sai do loop
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, insira um número válido.");
            }
        }
    }

    /**
     * Exibe o submenu para a gestão de quartos (CRUD - Create, Read, Update, Delete).
     */
    private static void menuGestaoQuartos() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- GESTÃO DE QUARTOS ---");
            System.out.println("1. Listar todos os Quartos");
            System.out.println("2. Cadastrar novo Quarto");
            System.out.println("3. Atualizar dados de um Quarto");
            System.out.println("4. Excluir um Quarto");
            System.out.println("0. Voltar ao Painel Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1: listarQuartos(); break;
                    case 2: cadastrarQuarto(); break;
                    case 3: atualizarQuarto(); break;
                    case 4: excluirQuarto(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, insira um número válido.");
            }
        }
    }

    /**
     * Lista todos os quartos registados na base de dados.
     */
    private static void listarQuartos() {
        System.out.println("\n--- LISTA DE QUARTOS ---");
        List<Quarto> lista = quartoDAO.listarQuartos();
        System.out.printf("%-5s | %-15s | %-15s | %-15s | %-10s\n", "ID", "NÚMERO", "TIPO", "PREÇO (MZN)", "ESTADO");
        System.out.println("-----------------------------------------------------------------------");
        for (Quarto q : lista) {
            System.out.printf("%-5d | %-15s | %-15s | %-15.2f | %-10s\n", q.getId(), q.getNumeroQuarto(), q.getTipoQuarto(), q.getPrecoPorNoite(), q.getEstado());
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    /**
     * Solicita os dados de um novo quarto ao utilizador e chama o DAO para o registar.
     */
    private static void cadastrarQuarto() {
        System.out.println("\n--- CADASTRAR NOVO QUARTO ---");
        try {
            Quarto quarto = new Quarto();
            System.out.print("Número do Quarto: ");
            quarto.setNumeroQuarto(scanner.nextLine());
            System.out.print("Tipo do Quarto (Ex: Simples, Duplo, Suite): ");
            quarto.setTipoQuarto(scanner.nextLine());
            System.out.print("Preço por Noite (MZN): ");
            quarto.setPrecoPorNoite(Double.parseDouble(scanner.nextLine()));
            quarto.setEstado("Vago"); // Por regra de negócio, um quarto novo está sempre vago

            quartoDAO.cadastrarQuarto(quarto);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar quarto. Verifique os dados inseridos.");
        }
    }

    /**
     * Solicita o ID de um quarto e os novos dados para o atualizar.
     */
    private static void atualizarQuarto() {
        listarQuartos(); // Mostra os quartos existentes para facilitar a escolha
        System.out.println("\n--- ATUALIZAR QUARTO ---");
        try {
            System.out.print("Digite o ID do quarto que deseja atualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Recolhe os novos dados
            Quarto quarto = new Quarto();
            quarto.setId(id);
            System.out.print("Novo número do quarto: ");
            quarto.setNumeroQuarto(scanner.nextLine());
            System.out.print("Novo tipo do quarto: ");
            quarto.setTipoQuarto(scanner.nextLine());
            System.out.print("Novo preço por noite: ");
            quarto.setPrecoPorNoite(Double.parseDouble(scanner.nextLine()));
            System.out.print("Novo estado (Vago, Ocupado, Manutencao): ");
            quarto.setEstado(scanner.nextLine());

            quartoDAO.atualizarQuarto(quarto);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar quarto. Verifique o ID e os dados inseridos.");
        }
    }
    
    /**
     * Solicita o ID de um quarto e chama o DAO para o excluir.
     */
    private static void excluirQuarto() {
        listarQuartos(); // Mostra os quartos para facilitar a escolha
        System.out.println("\n--- EXCLUIR QUARTO ---");
        try {
            System.out.print("Digite o ID do quarto que deseja excluir: ");
            int id = Integer.parseInt(scanner.nextLine());
            quartoDAO.excluirQuarto(id);
        } catch (Exception e) {
            System.out.println("Erro ao excluir quarto. Verifique o ID.");
        }
    }

    /**
     * Conduz o processo de check-in, que envolve:
     * 1. Selecionar um quarto vago.
     * 2. Registar um novo hóspede.
     * 3. Criar o registo de hospedagem, ligando o quarto ao hóspede.
     */
    private static void realizarCheckIn() {
        System.out.println("\n--- REALIZAR CHECK-IN ---");
        try {
            // Passo 1: Listar e selecionar o quarto
            System.out.println("\n** Quartos Vagos Disponíveis **");
            List<Quarto> quartosVagos = quartoDAO.listarQuartosPorEstado("Vago");
             if(quartosVagos.isEmpty()){
                 System.out.println("Nenhum quarto vago no momento. Não é possível fazer check-in.");
                 return; // Termina a operação se não houver quartos
             }
            System.out.printf("%-5s | %-15s | %-15s | %-15s\n", "ID", "NÚMERO", "TIPO", "PREÇO (MZN)");
            System.out.println("-----------------------------------------------------------------");
            for (Quarto q : quartosVagos) {
                System.out.printf("%-5d | %-15s | %-15s | %-15.2f\n", q.getId(), q.getNumeroQuarto(), q.getTipoQuarto(), q.getPrecoPorNoite());
            }
            System.out.print("\nDigite o ID do quarto escolhido: ");
            int idQuarto = Integer.parseInt(scanner.nextLine());

            // Passo 2: Cadastrar os dados do hóspede
            System.out.println("\n** Dados do Hóspede **");
            Hospede hospede = new Hospede();
            System.out.print("Nome Completo: ");
            hospede.setNomeCompleto(scanner.nextLine());
            System.out.print("Tipo de Documento (BI, Passaporte, DIRE): ");
            hospede.setTipoDocumento(scanner.nextLine());
            System.out.print("Número do Documento: ");
            hospede.setNumeroDocumento(scanner.nextLine());
            System.out.print("Nacionalidade: ");
            hospede.setNacionalidade(scanner.nextLine());
            System.out.print("Contacto Telefónico: ");
            hospede.setContacto(scanner.nextLine());
            System.out.print("Email (opcional): ");
            hospede.setEmail(scanner.nextLine());

            // O DAO cadastra o hóspede e retorna o objeto com o ID gerado pela BD
            Hospede hospedeCadastrado = hospedeDAO.cadastrarHospede(hospede);
            
            if (hospedeCadastrado != null) {
                // Passo 3: Criar a hospedagem
                Hospedagem hospedagem = new Hospedagem();
                hospedagem.setIdQuarto(idQuarto);
                hospedagem.setIdHospede(hospedeCadastrado.getId());
                hospedagem.setIdFuncionario(funcionarioLogado.getId()); // Usa o funcionário logado
                hospedagem.setDataCheckin(LocalDateTime.now()); // Data e hora atuais
                
                hospedagemDAO.realizarCheckIn(hospedagem); // Executa a transação de check-in
            } else {
                System.out.println("Falha ao cadastrar o hóspede. Check-in cancelado.");
            }

        } catch (Exception e) {
            System.out.println("Ocorreu um erro durante o processo de check-in: " + e.getMessage());
        }
    }
}
Pacote sgrh.model (Camada de Modelo)Este pacote contém as classes "POJO" (Plain Old Java Object) que representam as entidades da sua base de dados. Cada classe corresponde a uma tabela.// Ficheiro: Funcionario.java
package sgrh.model;
/**
 * Representa um funcionário do hotel.
 * Mapeia a tabela 'funcionarios' da base de dados.
 */
public class Funcionario {
    private int id;
    private String nomeCompleto;
    private String username;
    private String senha;
    
    // Getters e Setters para aceder e modificar os atributos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

// Ficheiro: Quarto.java
package sgrh.model;
/**
 * Representa um quarto do hotel.
 * Mapeia a tabela 'quartos' da base de dados.
 */
public class Quarto {
    private int id;
    private String numeroQuarto;
    private String tipoQuarto;
    private double precoPorNoite;
    private String estado;
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumeroQuarto() { return numeroQuarto; }
    public void setNumeroQuarto(String numeroQuarto) { this.numeroQuarto = numeroQuarto; }
    public String getTipoQuarto() { return tipoQuarto; }
    public void setTipoQuarto(String tipoQuarto) { this.tipoQuarto = tipoQuarto; }
    public double getPrecoPorNoite() { return precoPorNoite; }
    public void setPrecoPorNoite(double precoPorNoite) { this.precoPorNoite = precoPorNoite; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

// Ficheiro: Hospede.java
package sgrh.model;
/**
 * Representa um hóspede do hotel.
 * Mapeia a tabela 'hospedes' da base de dados.
 */
public class Hospede {
    private int id;
    private String nomeCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nacionalidade;
    private String contacto;
    private String email;
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// Ficheiro: Hospedagem.java
package sgrh.model;
import java.time.LocalDateTime;
/**
 * Representa a relação de hospedagem (check-in/check-out).
 * Mapeia a tabela 'hospedagens', que liga um hóspede a um quarto.
 */
public class Hospedagem {
    private int id;
    private int idQuarto;
    private int idHospede;
    private int idFuncionario;
    private LocalDateTime dataCheckin;
    private LocalDateTime dataCheckout;
    private String estado;
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdQuarto() { return idQuarto; }
    public void setIdQuarto(int idQuarto) { this.idQuarto = idQuarto; }
    public int getIdHospede() { return idHospede; }
    public void setIdHospede(int idHospede) { this.idHospede = idHospede; }
    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
    public LocalDateTime getDataCheckin() { return dataCheckin; }
    public void setDataCheckin(LocalDateTime dataCheckin) { this.dataCheckin = dataCheckin; }
    public LocalDateTime getDataCheckout() { return dataCheckout; }
    public void setDataCheckout(LocalDateTime dataCheckout) { this.dataCheckout = dataCheckout; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
Pacote sgrh.dao (Camada de Acesso a Dados)Este pacote é responsável por toda a comunicação com a base de dados. As classes DAO (Data Access Object) isolam a lógica de SQL do resto da aplicação.// Ficheiro: ConexaoDAO.java
package sgrh.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Classe responsável por estabelecer e retornar uma conexão com a base de dados MySQL.
 */
public class ConexaoDAO {
    /**
     * Tenta estabelecer uma conexão com a base de dados 'sgrh'.
     * @return um objeto {@link Connection} se for bem-sucedido, ou {@code null} se ocorrer um erro.
     */
    public Connection conectaBD() {
        Connection conn = null;
        try {
            // String de conexão JDBC para MySQL
            String url = "jdbc:mysql://localhost:3306/sgrh?user=root&password=";
            conn = DriverManager.getConnection(url);
        } catch (SQLException erro) {
            // Em caso de erro, imprime a mensagem no console
            System.out.println("Erro de Conexão: " + erro.getMessage());
        }
        return conn;
    }
}

// Ficheiro: FuncionarioDAO.java
package sgrh.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sgrh.model.Funcionario;
/**
 * DAO para operações relacionadas aos funcionários na base de dados.
 */
public class FuncionarioDAO {
    Connection conn;
    /**
     * Verifica as credenciais de um funcionário na base de dados.
     * @param objFuncionario Objeto contendo o username e a senha a serem verificados.
     * @return Um objeto {@link Funcionario} com todos os dados se a autenticação for bem-sucedida, caso contrário, {@code null}.
     */
    public Funcionario autenticacaoFuncionario(Funcionario objFuncionario) {
        conn = new ConexaoDAO().conectaBD();
        Funcionario funcionarioAutenticado = null;
        try {
            // Usa PreparedStatement para prevenir SQL Injection
            String sql = "SELECT * FROM funcionarios WHERE username = ? AND senha = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, objFuncionario.getUsername());
            pstm.setString(2, objFuncionario.getSenha());
            
            ResultSet rs = pstm.executeQuery();
            
            // Se encontrar um resultado, preenche o objeto de retorno
            if (rs.next()) {
                funcionarioAutenticado = new Funcionario();
                funcionarioAutenticado.setId(rs.getInt("id"));
                funcionarioAutenticado.setNomeCompleto(rs.getString("nome_completo"));
                funcionarioAutenticado.setUsername(rs.getString("username"));
            }
            pstm.close();
            conn.close();
        } catch (SQLException erro) {
            System.out.println("FuncionarioDAO: " + erro);
        }
        return funcionarioAutenticado;
    }
}

// Ficheiro: QuartoDAO.java
package sgrh.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sgrh.model.Quarto;
/**
 * DAO para operações CRUD (Create, Read, Update, Delete) na tabela 'quartos'.
 */
public class QuartoDAO {
    Connection conn;

    /**
     * Insere um novo quarto na base de dados.
     * @param objQuarto O objeto Quarto com os dados a serem inseridos.
     */
    public void cadastrarQuarto(Quarto objQuarto) {
        conn = new ConexaoDAO().conectaBD();
        String sql = "INSERT INTO quartos (numero_quarto, tipo_quarto, preco_por_noite, estado) VALUES (?, ?, ?, ?)";
        // try-with-resources garante que o PreparedStatement é fechado
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, objQuarto.getNumeroQuarto());
            pstm.setString(2, objQuarto.getTipoQuarto());
            pstm.setDouble(3, objQuarto.getPrecoPorNoite());
            pstm.setString(4, objQuarto.getEstado());
            pstm.execute();
            System.out.println("Quarto Cadastrado com Sucesso!");
            conn.close();
        } catch (SQLException erro) {
            System.out.println("QuartoDAO Cadastrar: " + erro);
        }
    }

    /**
     * Lista todos os quartos da base de dados, ordenados pelo número.
     * @return Uma lista de objetos {@link Quarto}.
     */
    public List<Quarto> listarQuartos() {
        conn = new ConexaoDAO().conectaBD();
        String sql = "SELECT * FROM quartos ORDER BY numero_quarto";
        List<Quarto> lista = new ArrayList<>();
        try (PreparedStatement pstm = conn.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Quarto objQuarto = new Quarto();
                objQuarto.setId(rs.getInt("id"));
                objQuarto.setNumeroQuarto(rs.getString("numero_quarto"));
                objQuarto.setTipoQuarto(rs.getString("tipo_quarto"));
                objQuarto.setPrecoPorNoite(rs.getDouble("preco_por_noite"));
                objQuarto.setEstado(rs.getString("estado"));
                lista.add(objQuarto);
            }
            conn.close();
        } catch (SQLException erro) {
            System.out.println("QuartoDAO Listar: " + erro);
        }
        return lista;
    }
    
    /**
     * Lista todos os quartos que correspondem a um determinado estado (ex: "Vago").
     * @param estado O estado do quarto a ser filtrado.
     * @return Uma lista de objetos {@link Quarto} filtrada.
     */
    public List<Quarto> listarQuartosPorEstado(String estado) {
         conn = new ConexaoDAO().conectaBD();
        String sql = "SELECT * FROM quartos WHERE estado = ? ORDER BY numero_quarto";
        List<Quarto> lista = new ArrayList<>();
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, estado);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Quarto objQuarto = new Quarto();
                objQuarto.setId(rs.getInt("id"));
                objQuarto.setNumeroQuarto(rs.getString("numero_quarto"));
                objQuarto.setTipoQuarto(rs.getString("tipo_quarto"));
                objQuarto.setPrecoPorNoite(rs.getDouble("preco_por_noite"));
                objQuarto.setEstado(rs.getString("estado"));
                lista.add(objQuarto);
            }
            conn.close();
        } catch (SQLException erro) {
            System.out.println("QuartoDAO ListarPorEstado: " + erro);
        }
        return lista;
    }

    /**
     * Atualiza os dados de um quarto existente.
     * @param objQuarto O objeto Quarto com o ID do quarto a ser atualizado e os novos dados.
     */
    public void atualizarQuarto(Quarto objQuarto) {
        conn = new ConexaoDAO().conectaBD();
        String sql = "UPDATE quartos SET numero_quarto = ?, tipo_quarto = ?, preco_por_noite = ?, estado = ? WHERE id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, objQuarto.getNumeroQuarto());
            pstm.setString(2, objQuarto.getTipoQuarto());
            pstm.setDouble(3, objQuarto.getPrecoPorNoite());
            pstm.setString(4, objQuarto.getEstado());
            pstm.setInt(5, objQuarto.getId());
            pstm.execute();
            System.out.println("Quarto Atualizado com Sucesso!");
            conn.close();
        } catch (SQLException erro) {
            System.out.println("QuartoDAO Atualizar: " + erro);
        }
    }

    /**
     * Exclui um quarto da base de dados com base no seu ID.
     * @param idQuarto O ID do quarto a ser excluído.
     */
    public void excluirQuarto(int idQuarto) {
        conn = new ConexaoDAO().conectaBD();
        String sql = "DELETE FROM quartos WHERE id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, idQuarto);
            pstm.execute();
            System.out.println("Quarto Excluído com Sucesso!");
            conn.close();
        } catch (SQLException erro) {
            System.out.println("QuartoDAO Excluir: " + erro);
        }
    }
}

// Ficheiro: HospedeDAO.java
package sgrh.dao;
import java.sql.*;
import sgrh.model.Hospede;
/**
 * DAO para operações relacionadas aos hóspedes.
 */
public class HospedeDAO {
    Connection conn;

    /**
     * Cadastra um novo hóspede e retorna o objeto com o ID gerado pela base de dados.
     * @param objHospede O hóspede a ser cadastrado.
     * @return O mesmo objeto {@link Hospede}, mas com o atributo 'id' preenchido, ou {@code null} em caso de erro.
     */
    public Hospede cadastrarHospede(Hospede objHospede) {
        conn = new ConexaoDAO().conectaBD();
        String sql = "INSERT INTO hospedes (nome_completo, tipo_documento, numero_documento, nacionalidade, contacto, email) VALUES (?, ?, ?, ?, ?, ?)";
        // Statement.RETURN_GENERATED_KEYS permite recuperar o ID auto-gerado
        try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, objHospede.getNomeCompleto());
            pstm.setString(2, objHospede.getTipoDocumento());
            pstm.setString(3, objHospede.getNumeroDocumento());
            pstm.setString(4, objHospede.getNacionalidade());
            pstm.setString(5, objHospede.getContacto());
            pstm.setString(6, objHospede.getEmail());
            
            int affectedRows = pstm.executeUpdate();
            
            if (affectedRows > 0) {
                // Recupera a chave (ID) gerada
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        objHospede.setId(rs.getInt(1)); // Define o ID no objeto
                    }
                }
            }
            conn.close();
            return objHospede;
        } catch (SQLException erro) {
            System.out.println("HospedeDAO Cadastrar: " + erro);
            return null;
        }
    }
}

// Ficheiro: HospedagemDAO.java
package sgrh.dao;
import java.sql.*;
import sgrh.model.Hospedagem;
/**
 * DAO para gerir as operações de hospedagem, como check-in e check-out.
 */
public class HospedagemDAO {
    Connection conn;

    /**
     * Realiza a operação de check-in, que é uma transação:
     * 1. Atualiza o estado do quarto para 'Ocupado'.
     * 2. Insere um novo registo na tabela 'hospedagens'.
     * Se uma das operações falhar, ambas são revertidas (rollback).
     * @param objHospedagem Objeto com os dados da hospedagem.
     */
    public void realizarCheckIn(Hospedagem objHospedagem) {
        conn = new ConexaoDAO().conectaBD();
        try {
            // Desativa o auto-commit para controlar a transação manualmente
            conn.setAutoCommit(false);
            
            // 1. Atualiza o estado do quarto
            String sqlUpdateQuarto = "UPDATE quartos SET estado = 'Ocupado' WHERE id = ?";
            try(PreparedStatement pstmUpdate = conn.prepareStatement(sqlUpdateQuarto)){
                pstmUpdate.setInt(1, objHospedagem.getIdQuarto());
                pstmUpdate.executeUpdate();
            }

            // 2. Insere o registo de hospedagem
            String sqlInsertHospedagem = "INSERT INTO hospedagens (id_quarto, id_hospede, id_funcionario, data_checkin, estado) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmInsert = conn.prepareStatement(sqlInsertHospedagem)) {
                pstmInsert.setInt(1, objHospedagem.getIdQuarto());
                pstmInsert.setInt(2, objHospedagem.getIdHospede());
                pstmInsert.setInt(3, objHospedagem.getIdFuncionario());
                pstmInsert.setTimestamp(4, Timestamp.valueOf(objHospedagem.getDataCheckin()));
                pstmInsert.setString(5, "Ativa");
                pstmInsert.execute();
            }
            
            // Se tudo correu bem, efetiva as alterações na base de dados
            conn.commit();
            System.out.println("\nCheck-in realizado com sucesso!");
            
        } catch (SQLException erro) {
            try {
                // Se ocorreu algum erro, desfaz todas as alterações
                conn.rollback();
                System.out.println("HospedagemDAO CheckIn (Erro): " + erro + ". A operação foi cancelada.");
            } catch (SQLException e) {
                System.out.println("HospedagemDAO Rollback: " + e);
            }
        } finally {
            // No final, reativa o auto-commit e fecha a conexão
             try {
                 if(conn != null) {
                     conn.setAutoCommit(true);
                     conn.close();
                 }
             } catch (SQLException ex) {
                System.out.println("HospedagemDAO setAutoCommit/close: " + ex);
             }
        }
    }
}
2. Análise de Teste e Qualidade de SoftwareO seu projeto é um ótimo ponto de partida. Ele está funcional para as tarefas propostas e bem estruturado com a separação em pacotes model e dao. No entanto, para a disciplina de Teste e Qualidade, há várias melhorias importantes que podem ser feitas.2.1. Segurança (Prioridade Alta)Vulnerabilidade Crítica: Senhas em Texto PlanoProblema: A sua tabela funcionarios e a lógica de autenticação (FuncionarioDAO) armazenam e comparam senhas em texto plano (ex: admin/1234). Esta é uma das falhas de segurança mais graves que um sistema pode ter. Se a base de dados for comprometida, todas as senhas dos funcionários serão expostas.Solução: Nunca armazene senhas diretamente. Use uma função de hashing criptográfico forte e lenta.Hashing: Quando um funcionário for registado ou a senha for alterada, aplique uma função de hash (como BCrypt ou Argon2) à senha antes de a guardar na base de dados.Verificação: Para fazer o login, o sistema deve:a. Obter o hash da senha armazenado na base de dados para o username fornecido.b. Aplicar a mesma função de hash à senha que o utilizador digitou no login.c. Comparar o hash gerado com o hash armazenado. Eles devem ser iguais.Exemplo com BCrypt (usando uma biblioteca como jBCrypt):// Para registar/criar um hash
String senhaOriginal = "1234";
String hashGerado = BCrypt.hashpw(senhaOriginal, BCrypt.gensalt()); 
// Armazenar `hashGerado` na base de dados

// Para verificar no login
String senhaDigitada = "1234";
String hashDoBanco = "..."; // Obter o hash da BD
if (BCrypt.checkpw(senhaDigitada, hashDoBanco)) {
    // Login bem-sucedido
}
Prevenção de SQL Injection (Ponto Forte)Análise: Você já está a usar PreparedStatement em todas as suas queries, o que é a prática recomendada para prevenir ataques de injeção de SQL. Este é um grande ponto a favor do seu projeto em termos de segurança. Continue a usar este método para todos os dados que vêm do utilizador.2.2. Robustez e ManutenibilidadeGestão de Conexões com a Base de DadosProblema: Em cada método de cada classe DAO, uma nova conexão com a base de dados é aberta e fechada (new ConexaoDAO().conectaBD()). Abrir e fechar conexões é uma operação "cara" (lenta e consome recursos). Em sistemas com muitos utilizadores, isso pode sobrecarregar a base de dados e a aplicação.Solução (Boas Práticas):Connection Pooling: Utilize um pool de conexões (ex: HikariCP, c3p0). Um pool mantém um conjunto de conexões abertas e prontas para uso. A sua aplicação "pede emprestada" uma conexão do pool e "devolve-a" quando termina, o que é muito mais eficiente.Injeção de Dependência (Alternativa Simples): Em vez de cada DAO criar a sua própria conexão, a conexão poderia ser criada na classe principal (Sgrh) e passada como parâmetro para os métodos DAO que participam numa mesma operação. Isso também melhora a gestão de transações.Tratamento de ErrosProblema: Atualmente, os erros são simplesmente impressos na consola (System.out.println("DAO Erro: " + erro)). Isso mistura a lógica de tratamento de erros com a lógica de acesso a dados e não permite que a camada que chamou o método (a UI, no caso) saiba que algo deu errado para poder reagir adequadamente (ex: mostrar uma mensagem amigável ao utilizador).Solução:Propagar Exceções: Em vez de capturar a SQLException no DAO e imprimir, propague-a. Modifique a assinatura dos seus métodos DAO para throws SQLException.Custom Exceptions: Para um código ainda mais limpo, crie as suas próprias exceções (ex: public class QuartoNaoEncontradoException extends Exception {}). O seu DAO capturaria a SQLException e lançaria uma exceção mais específica.Centralizar a Captura: A camada de UI (a classe Sgrh) seria responsável por capturar essas exceções e exibir uma mensagem apropriada para o utilizador.Validação de Dados de EntradaProblema: O sistema confia que o utilizador vai inserir dados válidos. Por exemplo, o que acontece se o utilizador inserir um preço de quarto negativo? Ou deixar o número do quarto em branco? Ou digitar "VAGO" em vez de "Vago" para o estado de um quarto?Solução: Valide todas as entradas do utilizador na classe Sgrh antes de as enviar para a camada DAO.Verifique se campos obrigatórios não estão vazios.Use expressões regulares (regex) para validar formatos (ex: email, contacto telefónico).Garanta que números estão dentro de um intervalo esperado (preços > 0).Para campos como "estado", use um enum em Java (public enum EstadoQuarto { VAGO, OCUPADO, MANUTENCAO }) para garantir consistência e evitar erros de digitação.2.3. TestabilidadeFalta de Testes AutomatizadosProblema: O código atual é difícil de testar de forma automática. Para testar o QuartoDAO, por exemplo, é necessário uma base de dados real e em execução. Para testar a lógica em Sgrh, é preciso simular a entrada de um utilizador na consola.Solução:Testes Unitários (com JUnit): Crie testes para cada classe DAO. Para isolar os testes da base de dados real, pode usar uma biblioteca de mocking (como Mockito) para simular o comportamento da conexão e do PreparedStatement.Separar Lógica de Negócio da UI: Se a lógica de negócio (ex: as regras de check-in) fosse movida da classe Sgrh para uma nova camada de "Serviços", seria muito mais fácil testá-la unitariamente, sem depender da consola.Testes de Integração: Crie um conjunto separado de testes que usem uma base de dados de teste (pode ser uma base de dados real, mas dedicada a testes, ou uma base de dados em memória como H2) para verificar se as queries SQL e a integração com a base de dados funcionam como esperado.2.4. Melhorias Funcionais SugeridasImplementar o Check-out: A funcionalidade mais óbvia a adicionar. O processo seria:Listar hóspedes com check-in "Ativo".O utilizador seleciona uma hospedagem.O sistema calcula o valor total a pagar (preço_por_noite * número_de_noites).Atualiza a tabela hospedagens com a data_checkout e o estado "Finalizada".Atualiza o estado do quarto para "Vago".Gestão de Hóspedes: Adicionar um menu para pesquisar, listar e atualizar dados de hóspedes já registados.Gestão de Funcionários: Permitir que um "Gerente" possa adicionar, editar ou remover outros funcionários.Relatórios Simples: Criar opções para gerar relatórios, como:Taxa de ocupação atual.Lista de check-ins realizados num determinado dia.Histórico de hospedagens de um cliente.Conclusão para a DisciplinaEste projeto demonstra uma boa compreensão da arquitetura em camadas (Modelo, DAO) e do uso de JDBC. Os pontos fortes são a estrutura organizada e a prevenção de SQL Injection. Para elevar a qualidade do software, o foco principal deve ser em segurança (implementar hashing de senhas imediatamente), robustez (melhorar a gestão de conexões e o tratamento de erros) e testabilidade (introduzir testes unitários e de integração). A implementação destas melhorias demonstraria um conhecimento sólido dos princípios de engenharia e qualidade de software.











