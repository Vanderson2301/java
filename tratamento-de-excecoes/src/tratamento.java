public class tratamento {
    @SuppressWarnings("static-access")
    public static void main(String[] args) {

        try{
            diminuirLetras ltpqn = new diminuirLetras();
            ltpqn.lowCase();

        } catch(NullPointerException e){
            System.out.println("Exceção NullPointerEexception identificada");
            System.out.println("Método diminuirLetras() tratado/n" +e);
        }
    }
}
