import javax.swing.JOptionPane;

/**
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author Jose Carlos Nobrega &lt;josenobrega@cc.ci.ufpb.br&gt;
 */
public class Peca {

    public static final int PEDRA_BRANCA = 0;
    public static final int DAMA_BRANCA = 1;
    public static final int PEDRA_VERMELHA = 2;
    public static final int DAMA_VERMELHA = 3;

    private Casa casa;
    private int tipo;
    private int direction;
    private String type;

    public Peca(Casa casa, int tipo) {
        this.casa = casa;
        this.tipo = tipo;
        casa.colocarPeca(this);

        setDirection();
        if(tipo == 0 || tipo == 1){
            type = "White";
        } 
        else {
            type = "Red";
        }
    }

    public void moveDirection(int direction, int newX, int newY) {
        if(((newX+direction) == casa.getX()) 
        && 
        (newY-1 == casa.getY() || newY+1 == casa.getY())) {
            Tabuleiro board = casa.getTabuleiroCasa();
            Casa novaCasa = board.getCasa(newX, newY);
            casa.removerPeca();
            novaCasa.colocarPeca(this);
            casa = novaCasa;
            board.getJogo().changeTurn();

            queen(newX, board.getJogo());   
        }
    }

    public void eatCasa(int newX, int newY){
        if((newX+2 == casa.getX() || newX-2 == casa.getX()) 
        &&(newY+2 == casa.getY() || newY-2 == casa.getY())){
            casa.removerPeca();
            Tabuleiro board = casa.getTabuleiroCasa();
            Casa newCasa = board.getCasa(newX, newY);

            newCasa.colocarPeca(this);
            casa = newCasa;
            
            countDead(board);
        }
    }

    public void queen(int newX, Jogo jogo){
        if(((newX == 7 && getTipo() == 0) || (newX == 7 && getTipo() == 1)) && jogo.getNeedEatBoolean()) {
            setTipo(1);
            direction = 1;
        } else if (newX == 0 && getTipo() == 1 && jogo.getNeedEatBoolean()) {
            direction = -1;
        } 
                
        if (((newX == 0 && getTipo() == 2) || (newX == 0 && getTipo() == 3)) && jogo.getNeedEatBoolean()) {
            setTipo(3);
            direction = -1;
        } else if (newX == 7 && getTipo() == 3 && jogo.getNeedEatBoolean()) {
            direction = 1;
        }           

    }   
    
    public void countDead(Tabuleiro board){
        Jogo jogo = board.getJogo();
        if(casa.getPeca().checkTypePeca() == "Red"){
           jogo.setWhiteDeads(jogo.getWhiteDeads()+ 1);
        } else if(casa.getPeca().checkTypePeca() == "White"){
           jogo.setRedDeads(jogo.getRedDeads()+ 1 );
        }
        
        if(jogo.getWhiteDeads() == 12){
            JOptionPane.showMessageDialog(jogo.getJanelaPrincipal(), "VERMELHAS ganharam!!!!");
            JOptionPane.showMessageDialog(jogo.getJanelaPrincipal(), "VERMELHAS ganharam!!!!");
            JOptionPane.showMessageDialog(jogo.getJanelaPrincipal(), "VERMELHAS ganharam!!!!");
                                    
        } else if(jogo.getRedDeads() == 12) {
            JOptionPane.showMessageDialog(jogo.getJanelaPrincipal(), "BRANCAS ganharam!!!!");
            JOptionPane.showMessageDialog(jogo.getJanelaPrincipal(), "BRANCAS ganharam!!!!");           
            JOptionPane.showMessageDialog(jogo.getJanelaPrincipal(), "BRANCAS ganharam!!!!");            
        }
    }
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getDirection() {
        return direction;
    }

    public void changeDirection() {
        direction *= -1;
    }

    public void setDirection() {
        switch(tipo){
            case 0:
                direction = -1;
                break;
            case 1:
                direction = 1;
                break;
            case 2:
                direction = 1;
                break;
            case 3:
                direction = -1;
                break;
        }
    }

    public String checkTypePeca() {
        return type;
    }
}
