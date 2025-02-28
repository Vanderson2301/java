import java.sql.*;
import javax.swing.JOptionPane;

public class BancodeDados {

    private final String url = "jdbc:sqlite:gerenciador_banco.db";

    // Método para criar o banco de dados e a tabela se ainda não existir
    public void criarBanco() {
        String sql = "CREATE TABLE IF NOT EXISTS conta ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "saldo REAL NOT NULL DEFAULT 0.00)";

        try (Connection conexao = DriverManager.getConnection(url);
             Statement stmt = conexao.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    // Método que verifica se já existe uma conta no banco. Se não houver, cria uma nova.
    public int obterOuCriarConta() {
        criarBanco();

        // Verifica se já existe uma conta
        String sqlVerificar = "SELECT id FROM conta LIMIT 1";

        try (Connection conexao = DriverManager.getConnection(url);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sqlVerificar)) {

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar conta: " + e.getMessage());
        }

        // Se não encontrou uma conta, cria uma nova
        String sqlCriar = "INSERT INTO conta (saldo) VALUES (0.00)";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement pstmt = conexao.prepareStatement(sqlCriar, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                int novoId = generatedKeys.getInt(1);
                JOptionPane.showMessageDialog(null, "Nenhuma conta encontrada. Criando nova conta...\nID da conta: " + novoId);
                return novoId; // Retorna o ID da nova conta criada
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar conta: " + e.getMessage());
        }
        return -1; // Retorna -1 caso ocorra algum erro
    }

    // Método para exibir o saldo de uma conta pelo ID
    public void mostrarSaldo(int id) {
        String sql = "SELECT saldo FROM conta WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                double saldo = resultado.getDouble("saldo");
                JOptionPane.showMessageDialog(null, "Seu saldo atual é: R$ " + saldo);
            } else {
                JOptionPane.showMessageDialog(null, "Conta não encontrada.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar saldo: " + e.getMessage());
        }
    }

    // Método para depositar dinheiro na conta
    public void depositar(int id, double valor) {
        if (valor <= 0) {
            JOptionPane.showMessageDialog(null, "Valor inválido para depósito.");
            return;
        }

        String sql = "UPDATE conta SET saldo = saldo + ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setDouble(1, valor);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Depósito de R$ " + valor + " realizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Conta não encontrada. Depósito não realizado.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar depósito: " + e.getMessage());
        }
    }

    // Método para realizar saque
    public void sacar(int id, double valor) {
        if (valor <= 0) {
            JOptionPane.showMessageDialog(null, "Valor inválido para saque.");
            return;
        }

        String sqlVerificarSaldo = "SELECT saldo FROM conta WHERE id = ?";
        String sqlSaque = "UPDATE conta SET saldo = saldo - ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement pstmtVerificar = conexao.prepareStatement(sqlVerificarSaldo);
             PreparedStatement pstmtSaque = conexao.prepareStatement(sqlSaque)) {

            pstmtVerificar.setInt(1, id);
            ResultSet resultado = pstmtVerificar.executeQuery();

            if (resultado.next()) {
                double saldoAtual = resultado.getDouble("saldo");

                if (saldoAtual >= valor) {
                    pstmtSaque.setDouble(1, valor);
                    pstmtSaque.setInt(2, id);
                    pstmtSaque.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Saque de R$ " + valor + " realizado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Conta não encontrada.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar saque: " + e.getMessage());
        }
    }

    // Método para realizar transferência entre contas
    public void transferir(int idOrigem, int idDestino, double valor) {
        if (valor <= 0) {
            JOptionPane.showMessageDialog(null, "Valor inválido para transferência.");
            return;
        }
        if (idDestino < 2 || idDestino > 100) {
            JOptionPane.showMessageDialog(null, "ID do destinatário inválido! Deve estar entre 2 e 100.");
            return;
        }

        String sqlVerificarSaldo = "SELECT saldo FROM conta WHERE id = ?";
        String sqlSaque = "UPDATE conta SET saldo = saldo - ? WHERE id = ?";
        String sqlDeposito = "UPDATE conta SET saldo = saldo + ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement pstmtVerificar = conexao.prepareStatement(sqlVerificarSaldo);
             PreparedStatement pstmtSaque = conexao.prepareStatement(sqlSaque);
             PreparedStatement pstmtDeposito = conexao.prepareStatement(sqlDeposito)) {

            pstmtVerificar.setInt(1, idOrigem);
            ResultSet resultado = pstmtVerificar.executeQuery();

            if (resultado.next()) {
                double saldoAtual = resultado.getDouble("saldo");

                if (saldoAtual >= valor) {
                    // Realiza o saque da conta de origem
                    pstmtSaque.setDouble(1, valor);
                    pstmtSaque.setInt(2, idOrigem);
                    pstmtSaque.executeUpdate();

                    // Realiza o depósito na conta de destino
                    pstmtDeposito.setDouble(1, valor);
                    pstmtDeposito.setInt(2, idDestino);
                    pstmtDeposito.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Transferência de R$ " + valor + " para ID " + idDestino + " realizada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente para transferência!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Conta de origem não encontrada.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar transferência: " + e.getMessage());
        }
    }
}
