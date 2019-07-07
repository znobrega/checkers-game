
/**
 * Representa uma Casa do tabuleiro.
 * Possui uma posi�ao (i,j) e pode conter uma Pe�a.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author Jose Carlos Nobrega &lt;josenobrega@cc.ci.ufpb.br&gt;
 */
public class Casa {
    
    private Tabuleiro tabuleiro;
    private int i;
    private int j;
    private Peca peca;

    public Casa(Tabuleiro tabuleiro , int i, int j) {
        this.tabuleiro = tabuleiro;
        this.i = i;
        this.j = j;
        this.peca = null;
    }
    

    public void colocarPeca(Peca peca) {
        this.peca = peca;
    }
    
    public void removerPeca() {
        peca = null;
    }

    public Peca getPeca() {
        return peca;
    }
    
    public boolean possuiPeca() {
        return peca != null;
    }
    
    public Tabuleiro getTabuleiroCasa(){
        return tabuleiro;
    }
    
    public int getX(){
        return i;
    }
    
    public int getY(){
        return j;
    }
}
