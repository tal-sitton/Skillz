package bots;

import penguin_game.Game;
import penguin_game.Iceberg;
import penguin_game.PenguinGroup;
import penguin_game.SkillzBot;

import java.util.Arrays;
import java.util.Comparator;

public class MyBot implements SkillzBot {

    public static Utils utils = new Utils();

    public void doTurn(Game game) {
        // Enter your code here:
        for (Iceberg myIceberg : game.getMyIcebergs()) {
            int howManyToSend = howManyToSend(game, myIceberg);
            // The amount of penguins in my iceberg.
            int myPenguinAmount = myIceberg.penguinAmount;
            int myPenguinAmountToSend = 0;

            // Initializing the iceberg we want to send penguins to.
            Iceberg destination = null;
            Iceberg des = null;
            int enemyP = -1;

            if (game.turn < 10) {
                destination = game.getNeutralIcebergs()[2];
                System.out.println(destination.uniqueId);
                myPenguinAmountToSend = destination.penguinAmount + 2;
            } else {
                uniqueIdChecker(game);
                System.out.println(utils.getSentToSecond());
                System.out.println(game.getMyIcebergs().length == 2);
                if (game.turn == 20 && !utils.getSentToSecond() && myIceberg.uniqueId == 59) {
                    for (Iceberg nextIceberg : game.getNeutralIcebergs()) {
                        System.out.println(nextIceberg.uniqueId);
                        if (nextIceberg.uniqueId == 37) {
                            System.out.println("found neo, the one");
                            destination = nextIceberg;
                            myPenguinAmountToSend = destination.penguinAmount + 1;
                            if (myPenguinAmount > destination.penguinAmount + 1)
                                utils.setSentToSecond(true);
                            break;
                        }
                    }
                } else {
                    if (utils.getSentToSecond() && game.getNeutralIcebergs().length > 0 && getNeutralIceDesWithYeledKaka(game, myIceberg) != null) {
                        System.out.println("hello there, general kenoli");
                        for (Iceberg destinations : getNeutralIceDesWithYeledKaka(game, myIceberg)) {
                            des = destinations;
                            enemyP = howManyEnemyPSent(game, des);
                            if (myPenguinAmount > enemyP - des.penguinAmount + 1) { //checks if we have enough penguins to take over the island
                                System.out.println("i can do it, my masterv " + myIceberg.id);
                                break;
                            }
                        }
                        if (myIceberg.getTurnsTillArrival(des) == firstToStrike(game, des).turnsTillArrival + 1) {
                            destination = des;
                            if (enemyP != -1) {
                                myPenguinAmountToSend = enemyP - des.penguinAmount + des.penguinsPerTurn + 1;
                                System.out.println("i know i can! im going in " + myIceberg.id);
                            }
                        }
                    } else {
                        if (utils.getSentToSecond() && getEnemyIceDesWithNewYeledKaka(game, myIceberg) != null) {
                            System.out.println("hello there, general darth vader");
                            destination = getEnemyIceDesWithNewYeledKaka(game, myIceberg);
                            myPenguinAmountToSend = destination.penguinAmount + howManyEnemyPSent(game, destination) + destination.penguinsPerTurn
                                    * myIceberg.getTurnsTillArrival(destination) + 1;

                        }
                    }
                    defense(game, myIceberg);
                }
            }
            // The amount of penguins the target has.
            if (destination != null && myPenguinAmountToSend != 0) {
                if (myPenguinAmount > destination.penguinAmount + 1 && howManyToSend + 1 > myPenguinAmountToSend) {
                    int destinationPenguinAmount = destination.penguinAmount;
                    System.out.println(myIceberg + " sends " + (destinationPenguinAmount + 1) + " penguins to " + destination);
                    myIceberg.sendPenguins(destination, myPenguinAmountToSend);
                }
            }
        }

    }

    public void uniqueIdChecker(Game game) {
        for (Iceberg iceberg : game.getNeutralIcebergs()) {
            System.out.println(iceberg.uniqueId);
        }

    }

    public int howManyToSend(Game game, Iceberg iceberg) {
        int gainEveryTurn = iceberg.penguinsPerTurn;
        int mTurns = 0;
        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) {

            if (penguinGroup.destination == iceberg) {
                if (mTurns < penguinGroup.turnsTillArrival)
                    mTurns = penguinGroup.turnsTillArrival;

                /*if (canHandle - howManyEnemyPSent(game,iceberg) > 0){                   //penguinGroup.penguinAmount > 0) {
                    allICanHandleWith -= (canHandle - penguinGroup.penguinAmount);
                }
            */
            }
        }
        return iceberg.penguinAmount + gainEveryTurn * mTurns - howManyEnemyPSent(game, iceberg) + 1;
    }

    public Iceberg[] getNeutralIceDesWithYeledKaka(Game game, Iceberg iceberg) {
        Iceberg[] icebergs;
        int howManyIcebergs = 0;
        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) {
            if (penguinGroup.destination.owner.id != 1 && penguinGroup.destination.owner.id != 0)
                howManyIcebergs++;
        }
        if (howManyIcebergs == 0)
            return null;
        icebergs = new Iceberg[howManyIcebergs];
        int i = 0;
        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) {
            if (penguinGroup.destination.owner.id != 1 && penguinGroup.destination.owner.id != 0) {
                icebergs[i] = penguinGroup.destination;
                i++;
            }
        }
        Arrays.sort(icebergs, Comparator.comparingInt(iceberg::getTurnsTillArrival));
        return icebergs;
    }

    public Iceberg getEnemyIceDesWithNewYeledKaka(Game game, Iceberg iceberg) {
        Iceberg[] icebergs = game.getEnemyIcebergs();

        Arrays.sort(icebergs, Comparator.comparingInt(iceberg::getTurnsTillArrival));
        for (Iceberg i1 : icebergs) {
            System.out.println("im just singing my song");
            int needToSend = i1.penguinAmount + howManyEnemyPSent(game, i1) + i1.penguinsPerTurn * iceberg.getTurnsTillArrival(i1);
            if (iceberg.penguinAmount > needToSend && howManyToSend(game, iceberg) >= needToSend) {
                if (howManyEnemyPSent(game, i1) + i1.penguinAmount + i1.penguinsPerTurn * iceberg.getTurnsTillArrival(i1) > howManyMyPSent(game, i1)) {
                    System.out.println("hello, i found one my master");
                    return i1;
                }
            }
        }
        return null;
    }

    public PenguinGroup[] howManyEnemyPGSent(Game game, Iceberg des) {
        int arrLength = 0;
        for (PenguinGroup CountPenguinGroup : game.getEnemyPenguinGroups()) {
            if (CountPenguinGroup.destination == des)
                arrLength++;
        }

        PenguinGroup[] penguinGroupSent = new PenguinGroup[arrLength];
        int i = 0;
        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) {
            if (penguinGroup.destination == des) {
                penguinGroupSent[i] = penguinGroup;
                i++;
            }
        }
        return penguinGroupSent;
    }

    public int howManyEnemyPSent(Game game, Iceberg des) {
        int penguinSent = 0;
        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) {
            if (penguinGroup.destination == des)
                penguinSent += penguinGroup.penguinAmount;
        }
        return penguinSent;
    }

    public int howManyMyPSent(Game game, Iceberg des) {
        int penguinSent = 0;
        for (PenguinGroup penguinGroup : game.getMyPenguinGroups()) {
            if (penguinGroup.destination == des)
                penguinSent += penguinGroup.penguinAmount;
        }
        return penguinSent;
    }

    public PenguinGroup firstToStrike(Game game, Iceberg iceberg) {
        PenguinGroup[] arr = howManyEnemyPGSent(game, iceberg);
        Arrays.sort(arr, Comparator.comparingInt(o -> o.turnsTillArrival));
        int needAmount = iceberg.penguinAmount;
        int i = 0;
        for (PenguinGroup penguinGroup : arr) {
            needAmount-=penguinGroup.penguinAmount;
            if(needAmount<0){
                return penguinGroup;
            }
        }
        return arr[0];
    }

    public void defense(Game game, Iceberg myIceberg) { //tries to defend our icebergs from the enemy's attacks

        System.out.println("defense 1 " + howManyEnemyPSent(game, myIceberg) + " iceberg: " + myIceberg);
        int amountToSend = 0;
        if ((howManyEnemyPSent(game, myIceberg) > 0)) { // we are under attack!
            System.out.println("defense 2");
            PenguinGroup locatedGroup = null;
            for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) { //searches for the enemy penguin groups that attacks myIceberg
                if (penguinGroup.destination == myIceberg && howManyMyPSent(game, myIceberg) + myIceberg.penguinAmount + penguinGroup.turnsTillArrival * myIceberg.penguinsPerTurn < howManyEnemyPSent(game, myIceberg)) {  //if there is a need to defend
                    amountToSend = howManyEnemyPSent(game, myIceberg) - (myIceberg.penguinAmount + penguinGroup.turnsTillArrival * myIceberg.penguinsPerTurn) + 1;  //how many penguins we need to send to defend myIceberg
                    System.out.println("defense 3 " + amountToSend);
                    locatedGroup = penguinGroup;    //a penguin group that attacks myIceberg
                    break;
                }
            }
            for (Iceberg myI1 : game.getMyIcebergs()) { //where salvation will come from?
                if (locatedGroup != null) { //if there is a
                    if (myI1.getTurnsTillArrival(myIceberg) <= locatedGroup.turnsTillArrival && myI1 != myIceberg && myI1.penguinAmount-amountToSend > 0) {    //can iceberg myI1 reach in time?
                        System.out.println("defense 4 -- end ( " + myI1 + " ):");
                        myI1.sendPenguins(myIceberg, amountToSend); //defence successful!
                        break;
                    }
                }
            }
        }
    }
}