package bots;

import penguin_game.*;

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
                destination = game.getNeutralIcebergs()[0];
                myPenguinAmountToSend = destination.penguinAmount + 2;
            } else {
                System.out.println(utils.getSentToSecond());
                System.out.println(game.getMyIcebergs().length == 2);
                if (game.turn == 18 && !utils.getSentToSecond()) {
                    for (Iceberg nextIceberg : game.getNeutralIcebergs()) {
                        System.out.println(nextIceberg.uniqueId);
                        if (nextIceberg.uniqueId == 39) {
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
                                break;
                            }
                        }
                        if (myIceberg.getTurnsTillArrival(des) == firstToStrike(game, des).turnsTillArrival + 1) {
                            destination = des;
                            if (enemyP != -1)
                                myPenguinAmountToSend = enemyP - des.penguinAmount + 2;
                        }
                    } else {
                        if (utils.getSentToSecond() && getEnemyIceDesWithNewYeledKaka(game, myIceberg) != null) {
                            System.out.println("hello there, general darth vader");
                            destination = getEnemyIceDesWithNewYeledKaka(game, myIceberg);
                            myPenguinAmountToSend = destination.penguinAmount + howManyEnemyPSent(game, destination) + destination.penguinsPerTurn
                                    * myIceberg.getTurnsTillArrival(destination) + 1;
                        }
                    }
                }
            }
            int z = 0;
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
        for (Iceberg iceberg : game.getAllIcebergs()) {
            System.out.println(iceberg.uniqueId);
        }

    }

    public int howManyToSend(Game game, Iceberg iceberg) {
        int gainEveryTurn = iceberg.penguinsPerTurn;
        int allICanHandleWith = iceberg.penguinAmount;

        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups()) {

            if (penguinGroup.destination == iceberg) {
/*                for (PenguinGroup p2 : game.getEnemyPenguinGroups()) {
                    if (p2 != penguinGroup) {
                        if ( p2.destination == iceberg) {

                        }
                    }
                }*/
                int canHandle = iceberg.penguinAmount + penguinGroup.turnsTillArrival * gainEveryTurn + 1;
                if (canHandle - penguinGroup.penguinAmount > 0) {
                    allICanHandleWith -= (canHandle - penguinGroup.penguinAmount);
                }
            }
        }
        return allICanHandleWith;
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
        int i = 0;
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
        PenguinGroup first = game.getEnemyPenguinGroups()[0];
        int i = 0;
        for (PenguinGroup penguinGroup : game.getEnemyPenguinGroups())
            if (penguinGroup.destination == iceberg) {
                if (i == 0) {
                    first = penguinGroup;
                    i++;
                }
                if (penguinGroup.turnsTillArrival < first.turnsTillArrival)
                    first = penguinGroup;
            }
        return first;
    }
}
