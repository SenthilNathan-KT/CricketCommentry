//System.out.println();

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author SenthilNathan.KT
 */
public class CricketCommentry {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
       CommentryImplementation c = new CommentryImplementation();
       c.getInput();
       c.getMaximumProbableValuesForABastsmen();
       c.printCommentry();
    }
    
}
