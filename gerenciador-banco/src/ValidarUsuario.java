import javax.swing.JOptionPane;

public class ValidarUsuario {
    public String NomeUsuario(){
        String nomeCompleto = JOptionPane.showInputDialog(null, "Digite seu nome completo.");
                return nomeCompleto;
    }
    public String ValidadorCPF() {
            int tentativas = 0;
            int limite_de_tentativas = 3;

        while (tentativas < limite_de_tentativas) {
            String cpf = JOptionPane.showInputDialog(null, "Digite seu CPF, sem pontos e traços.");
            if (cpf == null) {
                return null; // Se o usuário cancelar a entrada, retorna null
            }
            cpf = cpf.replaceAll("\\D", ""); // Remove tudo que não for número
            
            if (cpf.length() == 11) { // O CPF deve ter exatamente 11 números

                String primeirosdigito = cpf.substring(0, 9);
                String segundodigito = cpf.substring(0, 10);
                String doisUltimos = cpf.substring(cpf.length()-2);
                int somaprimeiro = 0;
                int somasegundo = 0;
                int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
                int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
                
                /*Primeiro Verificador */
                for(int i = 0; i < primeirosdigito.length(); i++){
                    
                    int digito = Character.getNumericValue(primeirosdigito.charAt(i));
                    somaprimeiro += digito * pesos1[i];

                }
                int r1 = (somaprimeiro % 11) < 2 ? 0 : 11 - (somaprimeiro % 11);

                /*Segundo Verificador */
                for(int i = 0; i < segundodigito.length(); i++){
                    
                    int digito = Character.getNumericValue(segundodigito.charAt(i));
                    somasegundo += digito * pesos2[i];

                }
                    int r2 = (somasegundo % 11) < 2 ? 0 : 11 - (somasegundo % 11);
                
                /*Junção e verificação dos digitos */
                String verificador = String.valueOf(r1) + String.valueOf(r2);
                
                if (verificador.equals(doisUltimos)){
                    return cpf;
                }else {
                    tentativas += 1;
                    if (tentativas < limite_de_tentativas){
                        JOptionPane.showMessageDialog(null, "CPF INCORRETO, DIGITE NOVAMENTE!", "ERRO!!!", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "VOCÊ ERROU MUITAS VEZES, INICIE O PROGRAMA NOVAMENTE!", "ERRO!!!", JOptionPane.INFORMATION_MESSAGE);
                            return null;
                    }
                }
            } else{
                tentativas += 1;
                    if (tentativas < limite_de_tentativas){
                        JOptionPane.showMessageDialog(null, "CPF INCORRETO, DIGITE NOVAMENTE!", "ERRO!!!", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "VOCÊ ERROU MUITAS VEZES, INICIE O PROGRAMA NOVAMENTE!", "ERRO!!!", JOptionPane.INFORMATION_MESSAGE);
                            return null;
                    }
            }
        }
            return null;
        }
    }