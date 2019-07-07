
/**
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author Jose Carlos Nobrega &lt;josenobrega@cc.ci.ufpb.br&gt;
 */
public class Tabuleiro {
    
    private Jogo jogo;
    private Casa[][] casas;

    public Tabuleiro(Jogo jogo) {
        this.jogo = jogo;
        casas = new Casa[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = new Casa(this, i, j);
                casas[i][j] = casa;
            }
        }
    }

    public Casa getCasa(int i, int j) {
        return casas[i][j];
    }
    
    public Jogo getJogo() {
        return jogo;
    }
}
