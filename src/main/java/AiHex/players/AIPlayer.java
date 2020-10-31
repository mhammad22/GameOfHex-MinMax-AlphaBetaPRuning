package AiHex.players;

import AiHex.gameMechanics.Move;
import AiHex.gameMechanics.Runner;
import AiHex.hexBoards.*;
import no.uib.cipr.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer implements Player {

    public Runner game = null;
    private int colour = 0;

    public AIPlayer(Runner game, int colour) {
        this.game = game;
        this.colour = colour;
    }

    //to get the move returned by AI player
    public Move getMove() {
        switch (colour) {
            case Board.RED:
                System.out.print("Red move: \n");

                break;
            case Board.BLUE:
                System.out.print("Blue move: \n");
                break;
        }

        while (game.getBoard().getSelected() != null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//         int rand_int1=0,rand_int2=0;
//         Point choice = game.getBoard().getSelected();
//
//        //----------------------------random generated moves-----------------------------//
//        Random rand = new Random();
//        int rand_int1 = rand.nextInt(game.getBoard().getSize());
//        int rand_int2 = rand.nextInt(game.getBoard().getSize());
//
//        while (game.getBoard().get(rand_int1, rand_int2) != Board.BLANK) {
//            rand_int1 = rand.nextInt(game.getBoard().getSize());
//            rand_int2 = rand.nextInt(game.getBoard().getSize());
//        }

        int depth = 3;

        int[] result = alphaBetaPruning(true, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        game.getBoard().setSelected(null);


        System.out.println("Best Move Played : " + result[1] + "  " + result[2]);

        Move bestMove = new Move(colour, result[1], result[2]);   //set the BestMove we achieve from pruning
        return bestMove;
    }

    //Alpha beta pruning to get Good Move
    int[] alphaBetaPruning(boolean maximizingPlayer, int depth, int _alpha, int _beta) {

        List<int[]> moves = GenerateChild();            //Generate Possible child of Given Board

        int current_score;                              //to Get score
        int NextMoveRow =-1 ;                           //row of good Move to be taken
        int NextMoveCol =-1 ;                           //column of good move to be taken
        if (depth == 0)                                 //evaluate the terminal state at given Depth
        {
            current_score = Evaluation();
            return new int[]{current_score, NextMoveRow, NextMoveCol};
        }
        else
            {
            for (int[] move : moves)                            //for Each Possible Moves(Child Generate)
            {
                game.getBoard().getBoard().setValue(move[0], move[1], Board.BLUE);
                if (maximizingPlayer) {                         //for Maximizing player
                    current_score = alphaBetaPruning(false, depth - 1, _alpha, _beta)[0];

                    if (current_score > _alpha) {
                        _alpha = current_score;
                        NextMoveRow = move[0];
                        NextMoveCol = move[1];
                    }
                    game.getBoard().getBoard().setValue(move[0], move[1], Board.BLANK);
                    if (_alpha >= _beta)       //prune the branch
                        break;
                }
                else                                            //For Minimizing player
                    {
                    current_score = alphaBetaPruning(true, depth - 1, _alpha, _beta)[0];

                    if (current_score < _beta) {
                        _beta = current_score;
                        NextMoveRow = move[0];
                        NextMoveCol = move[1];
                    }
                    game.getBoard().getBoard().setValue(move[0], move[1], Board.BLANK);

                    if (_alpha >= _beta)      //prune the branch
                        break;
                }
            }
        }
        return new int[]{(maximizingPlayer) ? _alpha : _beta, NextMoveRow, NextMoveCol};
        //return the Good Move to be taken
    }

    //Generate Possible child of Given Board
    List<int[]> GenerateChild() {
        int size = game.getBoard().getBoard().getSize();
        int board_size = size * size;
        int free_index = 0;
        for (int i = 0; i < size; i++) {                 //Count the Number of Child to be Generate
            for (int j = 0; j < size; j++) {
                if (game.getBoard().getBoard().getValue(j, i) == Board.BLANK) {
                    free_index = free_index + 1;
                }
            }
        }
        List<int[]> moves = new ArrayList<int[]>();
        int j = 0, k = 0;
        int Savek=0,Savej=0;
        boolean flag=true;
        for (int i = 0; i < free_index+(board_size-free_index); i++) {

            if (game.getBoard().getBoard().getValue(k, j) == Board.BLANK) {
                Savek=k;
                Savej=j;
                flag=false;
                k++;
            }
            else
            {
                k++;
            }

            if (k % size == 0) {
                k = 0;
                j = (j + 1) % size;
            }
            if(flag==false){
                moves.add(new int[]{Savek,Savej});          //add possible moves/child to the list
                flag=true;
            }

        }
        return moves;
    }

    //Evaluation Function return The possibility to win for AI Player
    public int Evaluation() {

        int val=0,val1=0,val2=0,val3=0,val4=0;
        int size=game.getBoard().getSize();
        int x=0,y=0;
        int BestScore=0;

        boolean flag=true;
        int update=30;

        for(int j=0;j<size;j++) {                     //Give the Hueristic Score of Chain Form
            for(int i=0;i<size;i++) {
                if(game.getBoard().getBoard().getValue(i,j)==Board.BLUE) {

                    x=i;y=j;update=10;
                    while((x+1)<size && game.getBoard().getBoard().getValue(x+1,y)==Board.BLUE){
                        val1+=update;
                        x++;
                        update+=200;
                    }

                    x=i;y=j;
                    while((x-1)>0 && game.getBoard().getBoard().getValue(x-1,y)==Board.BLUE){
                        val2+=update;
                        x--;
                        update+=200;
                    }

                    x=i;y=j;
                    while((y-1)>0 && game.getBoard().getBoard().getValue(x,y-1)==Board.BLUE){
                        val3+=update;
                        y--;
                        update+=100;
                    }

                    x=i;y=j;
                    while((y+1)<size && game.getBoard().getBoard().getValue(x,y+1)==Board.BLUE){
                        val4+=update;
                        y++;
                        update+=100;
                    }

                    int[] numbers=new int[4];
                    numbers[0]=val1;
                    numbers[1]=val2;
                    numbers[2]=val3;
                    numbers[3]=val4;

                   BestScore=getMaxValue(numbers);
                }

            }
        }

        for(int j=0;j<size;j++) {                           //Give Hueristic Score of Neighbour opponent check
            for(int i=0;i<size;i++) {
                if(game.getBoard().getBoard().getValue(i,j)==Board.RED)
                {
                    if(i-1>0){
                        if(game.getBoard().getBoard().getValue(i-1,j)==Board.BLUE){
                            BestScore+=50;
                        }
                        else{
                            BestScore-=10;
                        }
                    }
                    if(j-1>0){
                        if(game.getBoard().getBoard().getValue(i,j-1)==Board.BLUE){
                            BestScore+=50;
                        }
                        else{
                            BestScore-=10;
                        }
                    }
                    if(i+1<size){
                        if(game.getBoard().getBoard().getValue(i+1,j)==Board.BLUE){
                            BestScore+=50;
                        }
                        else{
                            BestScore-=10;
                        }
                    }
                    if(j+1>size){
                        if(game.getBoard().getBoard().getValue(i,j+1)==Board.BLUE){
                            BestScore+=50;
                        }
                        else{
                            BestScore-=10;
                        }
                    }
                    if(i+1<size && j+1<size){
                        if(game.getBoard().getBoard().getValue(i+1,j+1)==Board.BLUE){
                            BestScore+=50;
                        }
                        else{
                            BestScore-=10;
                        }
                    }

                    if(i-1>0 && j-1>0){
                        if(game.getBoard().getBoard().getValue(i-1,j-1)==Board.BLUE){
                            BestScore+=50;
                        }
                        else{
                            BestScore-=10;
                        }
                    }
                }

            }
        }

        int row=0;                          //Check Boundary Score
        int col=0;

        for(col=0;col<size;col++){
            if(game.getBoard().getBoard().getValue(row,col)==Board.BLUE){
                BestScore+=10;
            }
        }

        row=size-1;
        col=0;

        for(col=0;col<size;col++){
            if(game.getBoard().getBoard().getValue(row,col)==Board.BLUE){
                BestScore+=10;
            }
        }


        return BestScore;       //return the BestScore of the Given state
    }

    //Give Maximum Number Among Given Array
    public static int getMaxValue(int[] numbers){
        int maxValue = numbers[0];
        for(int i=1;i < numbers.length;i++){
            if(numbers[i] > maxValue){
                maxValue = numbers[i];
            }
        }
        return maxValue;
    }


    public ArrayList<Board> getAuxBoards() {
        return new ArrayList<Board>();
    }




}


