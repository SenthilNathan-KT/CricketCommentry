/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author SenthilNathan.KT
 */
public class CommentryImplementation {
    Scorecard[] scorecardArr;
    Result result = new Result();
    
    int probabilityMetaSize = 7;
    int noOfWickets = 5;
    private static float[][] probabilityMeta = {{0.05f, 0.3f, 0.25f, 0.1f, 0.15f, 0.01f, 0.09f, 0.05f}, {0.1f, 0.4f, 0.2f, 0.05f, 0.1f, 0.01f, 0.04f, 0.1f},
            {0.2f, 0.3f, 0.15f, 0.05f, 0.05f, 0.01f, 0.04f, 0.2f}, {0.3f, 0.25f, 0.05f, 0f, 0.05f, 0.01f, 0.04f, 0.3f}, {0.3f, 0.25f, 0.05f, 0f, 0.05f, 0.01f, 0.04f, 0.3f}};

    public void getInput() throws Exception {
        Scanner kb = new Scanner(System.in);
        String[] res = kb.nextLine().split(" ");
        result.status = res[1];
        result.tName = res[0];
        if(res[4].contains("wicket"))
        {
            result.wicket = noOfWickets - Integer.parseInt(res[3]); // Total no of players(6) - with remaining wickets in hands.
            scorecardArr = new Scorecard[result.wicket];
            for(int i = 0; i < result.wicket; i++)
            {
                scorecardArr[i] = new Scorecard();
            }
        }
        else
        {
            result.runs = Integer.parseInt(res[3]);
        }
        result.balls = 24 - Integer.parseInt(res[6]);

        System.out.println("Please enter " + result.wicket + " inputs in the form of - (PlayerName)&(Score)&(balls)&(Status)");
        System.out.println("For instance Dhoni&25&11&w - if status is w then the batsmen is out. If not he will be considered as active player");
        int totalNoOfActivePlayers = 0;
       for(int i = 0; i < result.wicket; i++)
       {
            String[] scoreCard = kb.nextLine().split("&");
            scorecardArr[i].pName = (String.valueOf(scoreCard[0]));
            scorecardArr[i].runs = Integer.parseInt(scoreCard[1]);
            scorecardArr[i].balls = Integer.parseInt(scoreCard[2]);
            scorecardArr[i].status = scoreCard[3];
            if(!scoreCard[3].equalsIgnoreCase("w"))
            {
                totalNoOfActivePlayers++;
            }
       }
        if(totalNoOfActivePlayers != 2)
        {
            throw new Exception("No of active players should be 2. Please enter the valid input");
        }
    }

    void getMaximumProbableValuesForABastsmen()
    {
        for(int iter = 0; iter < scorecardArr.length; iter++)
        {
            int balls = scorecardArr[iter].balls;
            int[][] probabilityOfEachScoreArray = new int[noOfWickets][probabilityMetaSize]; //Here 6 is no
                for(int j = 0; j < probabilityMetaSize; j++)
                {
                    probabilityMeta[iter][j] = probabilityMeta[iter][j] * (float) balls;
                    probabilityOfEachScoreArray[iter][j] = (int) Math.ceil(probabilityMeta[iter][j]);
                }
                List possibiltyList = new ArrayList();
                for (int i = 0; i < 7; i++)
                {
                    for (int j = 0; j < probabilityOfEachScoreArray[iter][i]; j++)
                    {
                    possibiltyList.add(i);
                    }
                }
                Collections.sort(possibiltyList, Collections.reverseOrder());
                updateRunsScoredByTheBatsmenInScorecard(possibiltyList, possibiltyList.size(),scorecardArr[iter]);
        }
    }

    void updateRunsScoredByTheBatsmenInScorecard(List<Integer> possibilityList, int n, Scorecard scorecard)
    {
        int totalScore = scorecard.runs;
        int totalNoOfBalls = scorecard.balls;
        boolean isPlayerOut = scorecard.status.equals("w");

        int currScore  = 0, iter = totalNoOfBalls;
        if(isPlayerOut)
        {
            iter = iter--;
            totalNoOfBalls--;
        }
        if(possibilityList.get(0) < totalScore)
        {
            currScore = possibilityList.get(0);
            scorecard.scoreList.add(possibilityList.get(0));

            iter--;
        }

        for (int i = 1; i < n; i++)
        {
            if(iter == 0)
            {
                break;
            }
            if(currScore + possibilityList.get(i) > totalScore)
            {
                continue;
            }
            currScore += possibilityList.get(i);
            scorecard.scoreList.add(possibilityList.get(i));
            iter--;
            if(currScore == totalScore)
            {
                break;
            }
        }

        int listSize = scorecard.scoreList.size();
        if(listSize < totalNoOfBalls)
        {
            for(int i = 0; i < (totalNoOfBalls - listSize); i++)
            {
                scorecard.scoreList.add(0);
            }
        }
        Collections.shuffle(scorecard.scoreList);
        if(isPlayerOut)
        {
            scorecard.scoreList.add(-1);
        }
        listSize = scorecard.scoreList.size();
        if((int) scorecard.scoreList.get(listSize-1) == 0)
        {
            int value=0, index=0;
            for(int i = 0; i < listSize-2; i++)
            {
                if((int) scorecard.scoreList.get(i) != 0)
                {
                    value = (int) scorecard.scoreList.get(i);
                    index = i;
                }
            }
            scorecard.scoreList.remove(index);
            scorecard.scoreList.add(value);
        }
    }
    
    void printCommentry() {
        int player1 = 0, player2 = 1, cPlayer = 0, noOfWickets = 0, p1CurrIter =0, p2CurrIter =0, currScore = 0;
        boolean isPlayer1 = true;
        System.out.println("4 overs left. 40 runs to win");
        for(int i = 0; i < result.balls; i++)
        {
            int currIter = 0;
            if(isPlayer1)
            {
                if(scorecardArr[player1].scoreList.size() > p1CurrIter)
                {
                    currIter = p1CurrIter;
                }
                else
                {
                    currIter = p2CurrIter;
                    isPlayer1 = !isPlayer1;
                }
            }
            else
            {
                if(scorecardArr[player2].scoreList.size() > p2CurrIter)
                {
                    currIter = p2CurrIter;
                }
                else
                {
                    currIter = p1CurrIter;
                    isPlayer1 = !isPlayer1;
                }
            }
            cPlayer = (isPlayer1) ? player1 : player2;
            List scoreList = scorecardArr[cPlayer].scoreList;
            int score = (int) scoreList.get(currIter);
            if(score != -1)
            {
                if(isPlayer1)
                {
                    p1CurrIter++;
                }
                else
                {
                    p2CurrIter++;
                }
                System.out.println(((i+1)/6) +"." +((i+1)%6) + " " + scorecardArr[cPlayer].pName + " scores " + score + " runs");
                currScore += score;
                if(score %2 != 0)
                {
                    isPlayer1 = !isPlayer1;
                }
            }
            else
            {
                System.out.println(((i+1)/6) +"." +((i+1)%6) + " " + scorecardArr[cPlayer].pName + " is out");
                noOfWickets++;
                if(isPlayer1)
                {
                    player1 = noOfWickets +1;
                    p1CurrIter =0;
                }
                else
                {
                    player2 = noOfWickets +1;
                    p2CurrIter =0;
                }
            }
            if((i+1)%6 ==0)
            {
                isPlayer1 = !isPlayer1;
                System.out.println();
                System.out.println((4-((i+1)/6)) + " overs left. " + (40-currScore) + " runs to win");
                System.out.println();
            }
        }
        System.out.println();
        System.out.println(result.tName + " " + result.status + " by " + result.wicket + " and " + (24-result.balls) + " balls remaninig");
    }
}
