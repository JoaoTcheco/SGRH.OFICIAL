<%-- 
    Document   : estilos
    Created on : 02/06/2025, 13:23:54
    Author     : User
--%>

<%--
  Ficheiro: estilos.jsp
  Propósito: Contém os estilos CSS (Cascading Style Sheets) que são comuns
             a várias páginas da área do recepcionista.
  Como usar: Inclua este ficheiro noutras JSPs usando: <jsp:include page="_partials/estilos.jsp" />
--%>
<style>
    /* Estilo geral para o corpo da página */
    body { 
        font-family: Arial, sans-serif; /* Tipo de letra principal */
        margin: 0; /* Remove margens padrão do navegador */
        background-color: #f4f7f6; /* Cor de fundo suave para a página toda */
        color: #333333; /* Cor principal do texto */
        line-height: 1.6; /* Espaçamento entre linhas para melhor leitura */
    }

    /* Estilo para a área principal de conteúdo das páginas */
    .main-content { 
        padding: 25px; /* Espaço interno */
        max-width: 1200px; /* Largura máxima para não ficar muito largo em ecrãs grandes */
        margin: 20px auto; /* Centra o conteúdo na página e dá espaço em cima/baixo */
        background-color: #ffffff; /* Fundo branco para a área de conteúdo */
        border-radius: 8px; /* Cantos arredondados */
        box-shadow: 0 2px 10px rgba(0,0,0,0.08); /* Sombra suave */
    }

    /* Estilo para o cabeçalho dentro da área de conteúdo (onde fica o título e botões como "Adicionar Novo") */
    .header { 
        display: flex; /* Alinha o título e o botão lado a lado */
        justify-content: space-between; /* Coloca o título na esquerda e o botão na direita */
        align-items: center; /* Alinha verticalmente ao centro */
        margin-bottom: 25px; /* Espaço abaixo do cabeçalho */
        padding-bottom: 15px; /* Espaço abaixo da linha */
        border-bottom: 1px solid #e0e0e0; /* Linha cinzenta clara para separar */
    }

    /* Estilo para os títulos principais (H1) */
    h1 { 
        color: #2c3e50; /* Cor azul escura para os títulos */
        margin: 0; /* Remove margem padrão do h1 para controlar melhor */
        font-size: 24px; /* Tamanho do título */
    }

    /* Estilos para tabelas */
    table { 
        width: 100%; /* Tabela ocupa a largura toda disponível */
        border-collapse: collapse; /* Tira espaços entre as bordas das células, fica mais limpo */
        background: white; /* Fundo branco para a tabela */
        box-shadow: 0 1px 3px rgba(0,0,0,0.05); /* Sombra muito leve */
        border-radius: 5px; /* Cantos arredondados para a tabela */
        overflow: hidden; /* Garante que o border-radius funciona bem com as células */
    }
    th, td { /* Estilos para células de cabeçalho (th) e células normais (td) */
        border: 1px solid #e0e0e0; /* Borda cinzenta clara */
        padding: 12px 15px; /* Espaço dentro das células (vertical e horizontal) */
        text-align: left; /* Alinha o texto à esquerda */
        font-size: 14px; /* Tamanho do texto dentro da tabela */
    }
    th { /* Estilo específico para células de cabeçalho */
        background-color: #f8f9fa; /* Fundo cinza muito claro */
        font-weight: bold; /* Texto a negrito */
        color: #495057; /* Cor do texto do cabeçalho */
    }
    tr:nth-child(even) { /* Estilo para linhas pares da tabela (começando da segunda) */
        background-color: #fdfdfd; /* Cor de fundo um pouco diferente para facilitar a leitura */
    }
    tr:hover { /* Estilo quando o rato passa por cima de uma linha da tabela */
        background-color: #f1f1f1; /* Cor de fundo muda para destacar */
    }

    /* Estilos para botões */
    .btn-novo { /* Botão para "Adicionar Novo" */
        background-color: #28a745; /* Cor verde */
        color: white; 
        padding: 10px 18px; 
        text-decoration: none; /* Tira o sublinhado de link */
        border-radius: 5px; 
        font-size: 14px;
        transition: background-color 0.2s ease; /* Efeito suave ao mudar cor */
        border: none; /* Remove borda padrão de botões */
        cursor: pointer; /* Rato vira mãozinha */
    }
    .btn-novo:hover { 
        background-color: #218838; /* Verde mais escuro */
    }
    .btn-action { /* Estilo base para botões de ação pequenos (ex: Editar, Apagar) */
        color: white; 
        padding: 6px 12px; 
        text-decoration: none; 
        border-radius: 4px; 
        margin-right: 8px; /* Espaço à direita do botão */
        font-size: 0.9em; /* Texto um pouco mais pequeno */
        border: none;
        cursor: pointer;
        transition: background-color 0.2s ease;
    }
    .btn-edit { background-color: #007bff; /* Azul para Editar */ }
    .btn-edit:hover { background-color: #0056b3; }
    .btn-delete { background-color: #dc3545; /* Vermelho para Apagar */ }
    .btn-delete:hover { background-color: #c82333; }
    
    /* Estilos para formulários */
    .form-container { /* Caixa que envolve o formulário */
        background: white; 
        padding: 25px; 
        border-radius: 8px; 
    }
    .form-group { /* Cada grupo de etiqueta + campo de texto */
        margin-bottom: 20px; 
    }
    .form-group label { 
        display: block; /* Etiqueta ocupa a linha toda */
        margin-bottom: 8px; 
        font-weight: bold; 
        color: #495057; /* Cor da etiqueta */
    }
    /* Estilo para vários tipos de campos de formulário */
    .form-group input[type="text"],
    .form-group input[type="password"],
    .form-group input[type="email"],
    .form-group input[type="tel"], /* Para telefone */
    .form-group input[type="date"], /* Para datas */
    .form-group select { /* Para caixas de seleção (dropdown) */
        width: 100%; /* Ocupa a largura toda */
        padding: 10px; 
        border: 1px solid #ced4da; /* Borda cinzenta */
        border-radius: 4px; 
        box-sizing: border-box; /* Importante para o padding não estragar a largura */
        font-size: 14px;
    }
    .form-group select { /* Ajuste para caixas de seleção terem altura parecida com inputs */
        height: 40px; 
    }
    .form-group small { /* Para textinhos de ajuda abaixo dos campos */
        font-size: 0.85em;
        color: #6c757d; /* Cinza */
        display: block;
        margin-top: 5px;
    }
    .form-actions { /* Área onde ficam os botões de Salvar/Cancelar do formulário */
        margin-top: 25px; 
        padding-top: 20px;
        border-top: 1px solid #e0e0e0; /* Linha separadora */
        text-align: right; /* Alinha os botões à direita */
    }
    .btn-cancel { /* Botão de Cancelar */
        background-color: #6c757d; /* Cinza */
        color: white; 
        padding: 10px 18px; 
        text-decoration: none; 
        border-radius: 5px; 
        margin-left: 10px; /* Espaço à esquerda, se houver outro botão antes */
        font-size: 14px;
        border: none;
        cursor: pointer;
        transition: background-color 0.2s ease;
    }
    .btn-cancel:hover { background-color: #5a6268; }

    /* Estilos para mostrar o estado dos quartos na tabela de forma colorida */
    .status-Disponível { color: #28a745; font-weight: bold; } /* Verde */
    .status-Ocupado { color: #dc3545; font-weight: bold; }    /* Vermelho */
    .status-Reservado { color: #fd7e14; font-weight: bold; }  /* Laranja */
    .status-Em_Manutenção { color: #ffc107; font-weight: bold; } /* Amarelo */
    .status-A_Limpar { color: #17a2b8; font-weight: bold; }    /* Azul claro (ciano) */

    /* Estilos para mensagens de erro ou sucesso dentro de formulários ou no topo de listas */
    .mensagem-formulario-erro {
        color: #721c24; /* Cor do texto */
        background-color: #f8d7da; /* Cor de fundo */
        border: 1px solid #f5c6cb; /* Cor da borda */
        padding: 10px 15px;
        border-radius: 4px;
        margin-bottom: 20px;
        font-size: 0.9em;
    }
     .mensagem-formulario-sucesso {
        color: #155724;
        background-color: #d4edda;
        border: 1px solid #c3e6cb;
        padding: 10px 15px;
        border-radius: 4px;
        margin-bottom: 20px;
        font-size: 0.9em;
    }
</style>
