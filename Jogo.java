import javax.swing.JOptionPane;

/**
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author Jose Carlos Nobrega &lt;josenobrega@cc.ci.ufpb.br&gt;
 */
public class Jogo {

    private Tabuleiro tabuleiro;
    private Peca[] reds;
    private Peca[] whites;
    private JanelaPrincipal janela;
    private Casa needEat;
    private boolean turn;
    private boolean win;
    private String winner;
    private int turnCount;
    private int whiteDeads;
    private int redDeads;

    public Jogo(JanelaPrincipal janela) {
        this.janela = janela;
        tabuleiro = new Tabuleiro(this);
        reds = new Peca[12];
        whites = new Peca[12];
        criarPecas();

        needEat = null;
        turn = true;
        win = false;
        winner = null;
        turnCount = 0;
        whiteDeads = 0;
        redDeads = 0;
    }

    private void criarPecas() {
        setBlacks();
        setWhites();       
    }

    public void setBlacks() {
        int i = 0;
        for(int x = 5; x < 8; x++) {
            if(x == 6){
                for(int y = 0; y < 8; y+=2) {
                    reds[i] = new Peca(tabuleiro.getCasa(x,y), Peca.PEDRA_VERMELHA);
                    i++;
                };
            } 
            else {
                for(int y = 1; y < 8; y+=2) {
                    reds[i] = new Peca(tabuleiro.getCasa(x,y), Peca.PEDRA_VERMELHA);
                    i++;                
                }
            }
        }
    }

    public void setWhites(){
        int i = 0;
        for(int x = 0; x < 3; x++) {
            if(x == 1) {
                for(int y = 1; y < 8; y+=2) {
                    whites[i] = new Peca(tabuleiro.getCasa(x,y), Peca.PEDRA_BRANCA);
                    i++;
                };
            } 
            else {
                for(int y = 0; y < 8; y+=2) {
                    whites[i] = new Peca(tabuleiro.getCasa(x,y), Peca.PEDRA_BRANCA);
                    i++;    
                }
            }
        }   
    }
    
    //MOVER
    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa nowCasa = tabuleiro.getCasa(origemX, origemY);
        Casa newCasa = tabuleiro.getCasa(destinoX, destinoY);

        if((!newCasa.possuiPeca()) && getNeedEatBoolean() && (canPlay(nowCasa)) && (origemX-destinoX == 1 || origemX-destinoX == -1)) {
            move(origemX, origemY, destinoX, destinoY);
            //getJogo().changeTurn();
        } 
        else if(!newCasa.possuiPeca() && canPlay(nowCasa)) {
            eatFinal(origemX, origemY, destinoX, destinoY);
        } 
        else {
            JOptionPane.showMessageDialog(getJanelaPrincipal(), "Movimento inválido");
        }

    }

    /**
     * Comanda uma Pe�a na posicao (origemX, origemY) fazer um movimento 
     * para (destinoX, destinoY).
     * 
     * @param origemX linha da Casa de origem.
     * @param origemY coluna da Casa de origem.
     * @param destinoX linha da Casa de destino.
     * @param destinoY coluna da Casa de destino.
     */
    public void move(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);

        Peca peca = origem.getPeca();
        peca.moveDirection(peca.getDirection(), destinoX, destinoY);          
    }

    public void eatFinal(int origemX, int origemY, int destinoX, int destinoY){
        Casa nowCasa = tabuleiro.getCasa(origemX, origemY);
        Casa middleCasa = null;
        Casa newCasa = null;
        //cima direita
        if(destinoX-origemX == 2 && destinoY-origemY == 2){
            middleCasa = tabuleiro.getCasa(origemX+1, origemY+1);
            //cima esquerda
        } else if(destinoX-origemX == 2 && destinoY-origemY == -2) {
            middleCasa = tabuleiro.getCasa(origemX+1, origemY-1);
            //BAIXO DIREITA
        } else if(destinoX-origemX == -2 && destinoY-origemY == 2){
            middleCasa = tabuleiro.getCasa(origemX-1, origemY+1);
        } else if(destinoX-origemX == -2 && destinoY-origemY == -2){
            middleCasa = tabuleiro.getCasa(origemX-1, origemY-1);
        } else {
            middleCasa = null;
        }
        
        if(middleCasa != null) {
            newCasa = finalCasaUpdated(origemX, origemY, middleCasa.getX(), middleCasa.getY());
        }
        
        if(checkEat(nowCasa, middleCasa, newCasa) && canPlay(nowCasa)){
            checkEatPecaUpdated(nowCasa, middleCasa, newCasa);
        } 

    }
    

    public boolean checkEat(Casa nowCasa, Casa middleCasa, Casa newCasa){        
        if( ((newCasa != null && nowCasa != null && middleCasa !=null) && (!newCasa.possuiPeca() && middleCasa.possuiPeca()) )
        && (nowCasa.getPeca().checkTypePeca() != middleCasa.getPeca().checkTypePeca())
        && !getNeedEatBoolean()) {   
            if(nowCasa == needEat)  {
                return true;
            }  
            else {
                JOptionPane.showMessageDialog(getJanelaPrincipal(), "Coma com a peça anterior!!!");
                return false;
            }           
        } 
        else if((newCasa != null) && (!newCasa.possuiPeca() && middleCasa.possuiPeca())
        && (nowCasa.getPeca().checkTypePeca() != middleCasa.getPeca().checkTypePeca())) {
            return true;
        } 
        else {
            return false;
        }
    }

    public void checkEatPecaUpdated(Casa nowCasa, Casa middleCasa, Casa newCasa){
        Peca pecaInit = nowCasa.getPeca();        
        pecaInit.eatCasa(newCasa.getX(), newCasa.getY());

        middleCasa.removerPeca(); 
        if(checkDiagonalsEat(newCasa)) {
        } else {
            setNeedEat(null);
            changeTurn();
        }
        pecaInit.queen(newCasa.getX(), this);
    }

    public boolean checkDiagonalsEat(Casa afterEatCasa) {
        //Create ALL MIDLE CASAS AND FINALS
        Casa[] times = new Casa[4];
        Casa[] finals = new Casa[4];
        if(afterEatCasa.getX()-1 < 0 || afterEatCasa.getY()+1 > 7) {
            times[0] = null;
        } else {
            times[0] = tabuleiro.getCasa(afterEatCasa.getX()-1, afterEatCasa.getY()+1);
        }
        if(afterEatCasa.getX()+1 > 7 || afterEatCasa.getY()+1 > 7) {
            times[1] = null;
        } else {
            times[1] = tabuleiro.getCasa(afterEatCasa.getX()+1, afterEatCasa.getY()+1);
        }
        if(afterEatCasa.getX()-1 < 0 || afterEatCasa.getY()-1 < 0) {
            times[2] = null;
        } else {
            times[2] = tabuleiro.getCasa(afterEatCasa.getX()-1, afterEatCasa.getY()-1);
        }
        if(afterEatCasa.getX()+1 > 7 || afterEatCasa.getY()-1 < 0) {
            times[3] = null;
        } else {
            times[3] = tabuleiro.getCasa(afterEatCasa.getX()+1, afterEatCasa.getY()-1);
        }

        int j = 0;
        for(int i = 0; i < times.length; i++) {           
            if ( times[i] != null && (times[i].getPeca() != null) 
            && (times[i].getPeca().checkTypePeca() != afterEatCasa.getPeca().checkTypePeca())) {
                finals[j] = finalCasaUpdated(afterEatCasa.getX(), afterEatCasa.getY(), times[i].getX(), times[i].getY());                ;
                if((finals[j] != null) && (!finals[j].possuiPeca())) {
                    setNeedEat(afterEatCasa);
                    return true;
                }
                j++;    
            }
        }
        return false;
    }

    public Casa finalCasaUpdated(int xOrigem, int yOrigem, int xFinal, int yFinal){        
        Casa newCasa;
        //BAIXO
        if(xOrigem > xFinal) {
            if((yOrigem > yFinal) && xFinal>0 && yFinal>0) {
                //BAIXO ESQUERDA
                newCasa = tabuleiro.getCasa(xFinal-1, yFinal-1);
            } 
            else if((yOrigem < yFinal) && xFinal>0 && yFinal<7){
                //baixo direita
                newCasa = tabuleiro.getCasa(xFinal-1, yFinal+1);
            } 
            else {
                return null;
            }
        } 
        else {
            //CIMA ESQUERDA
            if((yOrigem > yFinal) && xFinal<7 && yFinal>0 ) {
                newCasa = tabuleiro.getCasa(xFinal+1, yFinal-1);
            } 
            //CIMA DIREITA
            else if((yOrigem < yFinal) && xFinal<7 && yFinal<7) {
                newCasa = tabuleiro.getCasa(xFinal+1, yFinal+1);
            } 
            else {
                return null;
            }
        }
        return newCasa;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public boolean getNeedEatBoolean() {
        if(needEat == null){
            return true;
        } else {
            return false;
        }
    }

    public void setNeedEat(Casa needEat) {
        this.needEat = needEat;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public void changeTurn() {
        if(getTurn()){
            setTurn(false);
        } 
        else {
            setTurn(true);
        }
        turnCount++;
    }

    public String piecePlaying(){
        if(getTurn()){
            return "White";
        } 
        else {
            return "Red";
        }
    }

    public boolean canPlay(Casa nowCasa){       
        if(piecePlaying() == nowCasa.getPeca().checkTypePeca()) {
            return true;
        } 
        else {
            return false;
        }
    }

    public boolean checkWinner(){
        int counterReds = 0;
        int counterWhites = 0;
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(tabuleiro.getCasa(x,y).getPeca().checkTypePeca() == "Red"){
                    counterReds++;                     
                } 
                else if(tabuleiro.getCasa(x,y).getPeca().checkTypePeca() == "White"){
                    counterWhites++;
                }
            }
        }

        if(counterReds == 12) {
            setWinner("Whites");
            return true;
        } 
        else if(counterWhites == 12) {
            setWinner("Reds");
            return true;
        } 
        else {
            return false;
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public JanelaPrincipal getJanelaPrincipal() {
        return janela;
    }

    public void setWhiteDeads(int whiteDeads){
        this.whiteDeads = whiteDeads;
    }

    public int getWhiteDeads(){
        return whiteDeads;
    }

    public void setRedDeads(int redDeads){
        this.redDeads = redDeads;
    }

    public int getRedDeads(){
        return redDeads;
    }

}
