import javax.swing.JOptionPane;

public class Menu {
    BancodeDados banco = new BancodeDados();

    public void opcoesMenu(int idConta) {
        while (true) { // Loop para manter o menu rodando até o usuário sair
            String opcoes = JOptionPane.showInputDialog(null, "Escolha uma das opções:\n"
                    + "1 - Saldo\n"
                    + "2 - Saque\n"
                    + "3 - Depósito\n"
                    + "4 - Transferência\n"
                    + "5 - Sair");

            if (opcoes == null) {
                break; // Sai do loop se o usuário cancelar
            }

            try {
                int opcao = Integer.parseInt(opcoes);
                switch (opcao) {
                    case 1:
                        banco.mostrarSaldo(idConta);
                        break;
                    case 2:
                        String valorSaque = JOptionPane.showInputDialog(null, "Digite o valor do saque:");
                        if (valorSaque != null) {
                            double valor = Double.parseDouble(valorSaque);
                            banco.sacar(idConta, valor);
                        }
                        break;
                    case 3:
                        String valorDeposito = JOptionPane.showInputDialog(null, "Digite o valor do depósito:");
                        if (valorDeposito != null) {
                            double valor = Double.parseDouble(valorDeposito);
                            banco.depositar(idConta, valor);
                        }
                        break;
                    case 4:
                        String idDestino = JOptionPane.showInputDialog(null, "Digite o ID do destinatário (2 a 100):");
                        String valorTransferencia = JOptionPane.showInputDialog(null, "Digite o valor da transferência:");
                        if (idDestino != null && valorTransferencia != null) {
                            int destinatario = Integer.parseInt(idDestino);
                            double valor = Double.parseDouble(valorTransferencia);
                            banco.transferir(idConta, destinatario, valor);
                        }
                        break;
                    case 5:
                        JOptionPane.showMessageDialog(null, "Saindo do programa...");
                        return; // Sai do método e encerra o menu
                    default:
                        JOptionPane.showMessageDialog(null, "Opção inválida! Escolha um número de 1 a 5.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida! Digite um número válido.");
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.banco.criarBanco();
        ValidarUsuario validarUsuario = new ValidarUsuario();

        String nome = validarUsuario.NomeUsuario();
        String cpf = validarUsuario.ValidadorCPF();

        int idConta = menu.banco.obterOuCriarConta();
        if (idConta != -1) {
            JOptionPane.showMessageDialog(null, "Bem-vindo! " + nome + ", portador do CPF: " + cpf);
            JOptionPane.showMessageDialog(null, "O ID da sua conta é: " + idConta);
            menu.opcoesMenu(idConta);
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao criar conta. O programa será encerrado.");
        }
    }
}
