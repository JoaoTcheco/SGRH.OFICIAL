/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.QuartoDAO;
import dao.ReservaDAO;
import java.util.List;
import model.Quarto;
import model.Reserva;

/**
 * Contém a lógica de negócio para as operações relacionadas com Reservas.
 * Esta classe é importante porque criar uma reserva envolve mais do que um passo
 * na base de dados (criar a reserva e atualizar o estado do quarto).
 */
public class ReservaService {

    // O ReservaService precisa de falar com o ReservaDAO e com o QuartoDAO.
    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final QuartoDAO quartoDAO = new QuartoDAO();

    /**
     * Lógica principal para criar uma nova reserva.
     * Este método faz duas coisas importantes:
     * 1. Guarda a nova reserva na tabela 'reservas'.
     * 2. Atualiza o estado do quarto reservado para 'RESERVADO' na tabela 'quartos'.
     *
     * IMPORTANTE: Num sistema mais avançado, estes dois passos deveriam ser feitos
     * dentro de uma "transação" da base de dados. Isso garante que, se o segundo passo
     * falhar, o primeiro é desfeito, para não deixar os dados inconsistentes.
     * No nosso sistema básico, fazemos um passo de cada vez.
     *
     * @param reserva O objeto Reserva com os dados da nova reserva.
     * @return true se AMBOS os passos (criar reserva E atualizar quarto) deram certo,
     * false se algum deles deu erro.
     */
    public boolean criarReserva(Reserva reserva) {
        // Passo 1: Tenta adicionar a reserva na base de dados.
        boolean reservaFoiCriadaComSucesso = reservaDAO.adicionar(reserva);
        
        // Passo 2: Se a reserva foi criada com sucesso, então tenta atualizar o estado do quarto.
        if (reservaFoiCriadaComSucesso) {
            // Pede ao QuartoDAO para mudar o estado do quarto (com o ID que está na reserva)
            // para RESERVADO.
            boolean quartoAtualizadoComSucesso = quartoDAO.atualizarStatus(reserva.getIdQuarto(), Quarto.StatusQuarto.RESERVADO);
            return quartoAtualizadoComSucesso; // O resultado final depende deste segundo passo
        }
        
        // Se a reserva não foi criada (passo 1 falhou), retorna false.
        return false;
    }

    /**
     * Pede ao ReservaDAO uma lista com todas as reservas registadas.
     * @return Uma lista de objetos Reserva.
     */
    public List<Reserva> listarTodas() {
       return reservaDAO.listarTodas();
    }
}

