import java.util.*;
//a simple tic tac toe game
public class TicTacToe {
    //public scanner
    public static Scanner input = new Scanner(System.in);
    //main method
    public static void main(String[] args) {
        //introductory logic
        boolean continueWhile = true;
        while (continueWhile) {
            System.out.println("Enter 1 for 2 players, 2 for playing against the CPU or 0 to quit: ");
            int answer = Integer.parseInt(input.nextLine());
            if (answer == 1) {
                twoPlayers();
                continueWhile = false;
            } else if (answer == 2) {
                playCpu();
                continueWhile = false;
            } else if (answer == 0) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid input try again: ");
            }
        }
    }
    //two players mode as a method
    public static void twoPlayers() {
        char[] arr = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        printBoard(arr);
        //store player moves in arraylists
        ArrayList<Integer> PlayerOneMoves = new ArrayList<>();
        ArrayList<Integer> PlayerTwoMoves = new ArrayList<>();
        //initialize variables
        int player = 1;
        int player1CPUAssist = 0;
        int player2CPUAssist = 0;
        int playerOneConsecutiveIncorrect = 0;
        int playerTwoConsecutiveIncorrect = 0;
        int playerOneIncorrect = 0;
        int playerTwoIncorrect = 0;
        //main loop
        while (true) {
            if (player == 3) {
                player = 1;
            }
            //get input
            System.out.println("Player "  + player + "'s turn, enter a selection (1-9): ");
            String selectionString = input.nextLine();
            int selection = Integer.parseInt(selectionString);
            try {
                //logic for forfeiting
                if (selectionString.equals("0")) {
                    System.out.println("Game Over! Player " + player + " forfeits the game.");
                    System.exit(0);
                }
                //CPU help logic
                if(selectionString.equals("000")) {
                    if(player == 1 && player1CPUAssist < 2) {
                        int assistSelection = CPUAssist(PlayerOneMoves, PlayerTwoMoves);
                        printBoard(addSelection(assistSelection, player, arr));
                        PlayerOneMoves.add(assistSelection);
                        player1CPUAssist++;
                        if (isWinner(PlayerOneMoves) || isWinner(PlayerTwoMoves)) {
                            printBoard(arr);
                            System.out.println("Game Over! Player " + player + " Wins.");
                            System.exit(0);
                        } else if (isTie(arr)) {
                            printBoard(arr);
                            System.out.println("“Game Over! It is a tie");
                            System.exit(0);
                        }
                        player += 1;
                    } else if(player == 2 && player2CPUAssist < 2) {
                        int assistSelection = CPUAssist(PlayerTwoMoves, PlayerOneMoves);
                        printBoard(addSelection(assistSelection, player, arr));
                        PlayerTwoMoves.add(assistSelection);
                        player2CPUAssist++;
                        if (isWinner(PlayerOneMoves) || isWinner(PlayerTwoMoves)) {
                            System.out.println("Game Over! Player " + player + " Wins.");
                            System.exit(0);
                        } else if (isTie(arr)) {
                            System.out.println("“Game Over! It is a tie");
                            System.exit(0);
                        }
                        player += 1;
                    } else {
                        System.out.println("Player " + player + " reaches max CPU assists, please try again");
                    }
                    //normal play logic
                } else if (arr[selection - 1] == (char) selection + 48) {
                    printBoard(addSelection(selection, player, arr));
                    if(player == 1) {
                        PlayerOneMoves.add(selection);
                        //means that no exception was thrown therefore:
                        playerOneConsecutiveIncorrect = 0;
                    } else {
                        PlayerTwoMoves.add(selection);
                        //means that no exception was thrown therefore:
                        playerTwoConsecutiveIncorrect = 0;
                    }
                    //check for winner
                    if (isWinner(PlayerOneMoves) || isWinner(PlayerTwoMoves)) {
                        System.out.println("Game Over! Player " + player + " Wins.");
                        System.exit(0);
                    } else if (isTie(arr)) {
                        System.out.println("“Game Over! It is a tie");
                        System.exit(0);
                    }
                    player += 1;
                } else {
                    System.out.println("Space already taken, please try again.");
                }
                //identify incorrect entries as out of bounds exception thrown by the print board method
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (player == 1) {
                    //increment the incorrect counters
                    playerOneIncorrect += 1;
                    playerOneConsecutiveIncorrect += 1;
                } else {
                    playerTwoIncorrect += 1;
                    playerTwoConsecutiveIncorrect += 1;
                }
                if (playerOneIncorrect > 4 || playerOneConsecutiveIncorrect > 2) {
                    System.out.println("Player 1 forfeits the game due to maximum incorrect entries");
                } else if (playerTwoIncorrect > 4 || playerTwoConsecutiveIncorrect > 2) {
                    System.out.println("Player 2 forfeits the game due to maximum incorrect entries");
                } else {
                    System.out.println("Incorrect entry, please try again.");
                }
            }
        }
    }
    //playCPU mode as a method (similar to two player mode)
    public static void playCpu() {
        char[] arr = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        ArrayList<Integer> PlayerMoves = new ArrayList<>();
        ArrayList<Integer> CPUMoves = new ArrayList<>();
        boolean playersTurn;
        int assistCount = 0;
        int playerConsecutiveIncorrect = 0;
        int playerIncorrect = 0;
        System.out.println("Do you want to go first? (enter 'YES' or 'NO'): ");
        String ans = input.nextLine();
        //analyze input for turn order
        while(!ans.equalsIgnoreCase("yes") && !ans.equalsIgnoreCase("no")) {
            System.out.println("Invalid response. (enter 'YES' or 'NO')");
            ans = input.nextLine();
        }
        playersTurn = ans.equals("YES");
        while (true) {
            if (playersTurn) {
                printBoard(arr);
                System.out.println("Enter a selection (1-9): ");
                String selectionString = input.nextLine();
                if (selectionString.equals("0")) {
                    System.out.println("Game Over! Player forfeits the game ot the CPU.");
                }
                int selection = Integer.parseInt(selectionString);
                try {
                    if(selectionString.equals("000")) {
                        if(assistCount < 2) {
                            int assistSelection = CPUAssist(PlayerMoves, CPUMoves);
                            addSelection(assistSelection, 1, arr);
                            PlayerMoves.add(assistSelection);
                            playersTurn = false;
                            assistCount++;
                            if (isWinner(PlayerMoves)) {
                                printBoard(arr);
                                System.out.println("Game Over! Player 1 Wins.");
                                System.exit(0);
                            } else if (isTie(arr)) {
                                printBoard(arr);
                                System.out.println("Game Over! It is a tie");
                                System.exit(0);
                            }
                        } else {
                            System.out.println("Player 1 reaches max CPU assists, please try again.");
                        }
                    } else if (arr[selection - 1] == (char) selection + 48) {
                        addSelection(selection, 1, arr);
                        PlayerMoves.add(selection);
                        playerConsecutiveIncorrect = 0;
                        playersTurn = false;
                        if (isWinner(PlayerMoves)) {
                            printBoard(arr);
                            System.out.println("Game Over! Player 1 Wins.");
                            System.exit(0);
                        } else if (isTie(arr)) {
                            printBoard(arr);
                            System.out.println("Game Over! It is a tie");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("Space already taken, please try again.");
                    }
                    //catch incorrect entry
                } catch (ArrayIndexOutOfBoundsException ex) {
                    playerConsecutiveIncorrect += 1;
                    playerIncorrect += 1;
                    if (playerIncorrect  > 4 ||  playerConsecutiveIncorrect > 2) {
                        System.out.println("Player 1 forfeits the game due to maximum incorrect entries");
                    }
                    System.out.println("Incorrect entry, please try again. ");
                }
            } else {
                //if not player turn CPU moves
                int CPUMove = CPUMove(PlayerMoves, CPUMoves);
                addSelection(CPUMove, 0, arr);
                CPUMoves.add(CPUMove);
                //check for winner/tie
                if(isWinner(CPUMoves)) {
                    System.out.println("Game Over! CPU Wins");
                    printBoard(arr);
                    System.exit(0);
                }
                if(isTie(arr)) {
                    System.out.println("Game Over! It is a tie");
                    printBoard(arr);
                    System.exit(0);
                }
                playersTurn = true;
            }
        }
    }
    //primitive CPU move logic, plan on replacing with minimax algorithm
    public static int CPUMove(ArrayList<Integer> playerMoves, ArrayList<Integer> CPUMoves) {
        int CPUattacks = CPUAttack(CPUMoves, playerMoves);
        //if attacks returns a move
        if(CPUattacks != -1) {
            return CPUattacks;
        }
        //check defends if no attack method
        int CPUdefends = CPUDefend(playerMoves, CPUMoves);
        if(CPUdefends != -1) {
            return CPUdefends;
        }
        //random move if no configuration matches
        return CPURandomMove(playerMoves, CPUMoves);
    }

    //cpu logic
//    CPUDefend method returns CPU selection for defending, returns -1 if not applicable (arr index + 1)
    public static int CPUDefend(ArrayList<Integer> playerMove, ArrayList<Integer> prevCPUMove) {
//        1,2,3, defend
        List<Integer> topRowPossibility1 = new ArrayList<>();
        topRowPossibility1.add(1); topRowPossibility1.add(2);
        List<Integer> topRowPossibility2 = new ArrayList<>();
        topRowPossibility2.add(2); topRowPossibility2.add(3);
        List<Integer> topRowPossibility3 = new ArrayList<>();
        topRowPossibility3.add(1); topRowPossibility3.add(3);
//        4,5,6 defend
        List<Integer> midRowPossibility1 = new ArrayList<>();
        midRowPossibility1.add(4); midRowPossibility1.add(5);
        List<Integer> midRowPossibility2 = new ArrayList<>();
        midRowPossibility2.add(5); midRowPossibility2.add(6);
        List<Integer> midRowPossibility3 = new ArrayList<>();
        midRowPossibility3.add(4); midRowPossibility3.add(6);
//        7,8,9 defend
        List<Integer> botRowPossibility1 = new ArrayList<>();
        botRowPossibility1.add(7); botRowPossibility1.add(8);
        List<Integer> botRowPossibility2 = new ArrayList<>();
        botRowPossibility2.add(8); botRowPossibility2.add(9);
        List<Integer> botRowPossibility3 = new ArrayList<>();
        botRowPossibility3.add(7); botRowPossibility3.add(9);
//        1,4,7 defend
        List<Integer> leftColPossibility1 = new ArrayList<>();
        leftColPossibility1.add(1); leftColPossibility1.add(4);
        List<Integer> leftColPossibility2 = new ArrayList<>();
        leftColPossibility2.add(4); leftColPossibility2.add(7);
        List<Integer> leftColPossibility3 = new ArrayList<>();
        leftColPossibility3.add(1); leftColPossibility3.add(7);
//        2,5,8 defend
        List<Integer> midColPossibility1 = new ArrayList<>();
        midColPossibility1.add(2); midColPossibility1.add(5);
        List<Integer> midColPossibility2 = new ArrayList<>();
        midColPossibility2.add(5); midColPossibility2.add(8);
        List<Integer> midColPossibility3 = new ArrayList<>();
        midColPossibility3.add(2); midColPossibility3.add(8);
//        3,6,9 defend
        List<Integer> rightColPossibility1 = new ArrayList<>();
        rightColPossibility1.add(3); rightColPossibility1.add(6);
        List<Integer> rightColPossibility2 = new ArrayList<>();
        rightColPossibility2.add(6); rightColPossibility2.add(9);
        List<Integer> rightColPossibility3 = new ArrayList<>();
        rightColPossibility3.add(3); rightColPossibility3.add(9);
//        1,5,9 defend
        List<Integer> negDiagonalPossibility1 = new ArrayList<>();
        negDiagonalPossibility1.add(1); negDiagonalPossibility1.add(5);
        List<Integer> negDiagonalPossibility2 = new ArrayList<>();
        negDiagonalPossibility2.add(5); negDiagonalPossibility2.add(9);
        List<Integer> negDiagonalPossibility3 = new ArrayList<>();
        negDiagonalPossibility3.add(1); negDiagonalPossibility3.add(9);
//        3,5,7 defend
        List<Integer> posDiagonalPossibility1 = new ArrayList<>();
        posDiagonalPossibility1.add(3); posDiagonalPossibility1.add(5);
        List<Integer> posDiagonalPossibility2 = new ArrayList<>();
        posDiagonalPossibility2.add(5); posDiagonalPossibility2.add(7);
        List<Integer> posDiagonalPossibility3 = new ArrayList<>();
        posDiagonalPossibility3.add(3); posDiagonalPossibility3.add(7);

        if(playerMove.containsAll(topRowPossibility1)) {
            if(!prevCPUMove.contains(3)) {
                return 3;
            }
        } else if(playerMove.containsAll(topRowPossibility2)) {
            if(!prevCPUMove.contains(1)) {
                return 1;
            }
        } else if(playerMove.containsAll(topRowPossibility3)) {
            if(!prevCPUMove.contains(2)) {
                return 2;
            }
        } else if(playerMove.containsAll(midRowPossibility1)) {
            if(!prevCPUMove.contains(6)) {
                return 6;
            }
        } else if(playerMove.containsAll(midRowPossibility2)) {
            if(!prevCPUMove.contains(4)) {
                return 4;
            }
        } else if(playerMove.containsAll(midRowPossibility3)) {
            if(!prevCPUMove.contains(5)) {
                return 5;
            }
        } else if(playerMove.containsAll(botRowPossibility1)) {
            if(!prevCPUMove.contains(9)) {
                return 9;
            }
        } else if(playerMove.containsAll(botRowPossibility2)) {
            if(!prevCPUMove.contains(7)) {
                return 7;
            }
        } else if(playerMove.containsAll(botRowPossibility3)) {
            if(!prevCPUMove.contains(8)) {
                return 8;
            }
        } else if(playerMove.containsAll(leftColPossibility1)) {
            if(!prevCPUMove.contains(7)) {
                return 7;
            }
        } else if(playerMove.containsAll(leftColPossibility2)) {
            if(!prevCPUMove.contains(1)) {
                return 1;
            }
            return 1;
        } else if(playerMove.containsAll(leftColPossibility3)) {
            if(!prevCPUMove.contains(4)) {
                return 4;
            }
        } else if(playerMove.containsAll(midColPossibility1)) {
            if(!prevCPUMove.contains(8)) {
                return 8;
            }
        } else if(playerMove.containsAll(midColPossibility2)) {
            if(!prevCPUMove.contains(2)) {
                return 2;
            }
        } else if(playerMove.containsAll(midColPossibility3)) {
            if(!prevCPUMove.contains(5)) {
                return 5;
            }
        } else if(playerMove.containsAll(rightColPossibility1)) {
            if(!prevCPUMove.contains(9)) {
                return 9;
            }
        } else if(playerMove.containsAll(rightColPossibility2)) {
            if(!prevCPUMove.contains(3)) {
                return 3;
            }
        } else if(playerMove.containsAll(rightColPossibility3)) {
            if(!prevCPUMove.contains(6)) {
                return 6;
            }
        } else if(playerMove.containsAll(negDiagonalPossibility1)) {
            if(!prevCPUMove.contains(9)) {
                return 9;
            }
        } else if(playerMove.containsAll(negDiagonalPossibility2)) {
            if(!prevCPUMove.contains(1)) {
                return 1;
            }
        } else if(playerMove.containsAll(negDiagonalPossibility3)) {
            if(!prevCPUMove.contains(5)) {
                return 5;
            }
        } else if(playerMove.containsAll(posDiagonalPossibility1)) {
            if(!prevCPUMove.contains(7)) {
                return 7;
            }
        } else if(playerMove.containsAll(posDiagonalPossibility2)) {
            if(!prevCPUMove.contains(3)) {
                return 3;
            }
        } else if(playerMove.containsAll(posDiagonalPossibility3)) {
            if(!prevCPUMove.contains(5)) {
                return 5;
            }
        }
        return -1;

    }
    //    CPUAttack method returns CPU selection for attacking, returns -1 if not applicable (arr index + 1)
    public static int CPUAttack(ArrayList<Integer> prevCPUMove, ArrayList<Integer> playerMove) {
//        1,2,3 attack
        List<Integer> topRowPossibility1 = new ArrayList<>();
        topRowPossibility1.add(1); topRowPossibility1.add(2);
        List<Integer> topRowPossibility2 = new ArrayList<>();
        topRowPossibility2.add(2); topRowPossibility2.add(3);
        List<Integer> topRowPossibility3 = new ArrayList<>();
        topRowPossibility3.add(1); topRowPossibility3.add(3);
//        4,5,6 attack
        List<Integer> midRowPossibility1 = new ArrayList<>();
        midRowPossibility1.add(4); midRowPossibility1.add(5);
        List<Integer> midRowPossibility2 = new ArrayList<>();
        midRowPossibility2.add(5); midRowPossibility2.add(6);
        List<Integer> midRowPossibility3 = new ArrayList<>();
        midRowPossibility3.add(4); midRowPossibility3.add(6);
//        7,8,9 attack
        List<Integer> botRowPossibility1 = new ArrayList<>();
        botRowPossibility1.add(7); botRowPossibility1.add(8);
        List<Integer> botRowPossibility2 = new ArrayList<>();
        botRowPossibility2.add(8); botRowPossibility2.add(9);
        List<Integer> botRowPossibility3 = new ArrayList<>();
        botRowPossibility3.add(7); botRowPossibility3.add(9);
//        1,4,7 attack
        List<Integer> leftColPossibility1 = new ArrayList<>();
        leftColPossibility1.add(1); leftColPossibility1.add(4);
        List<Integer> leftColPossibility2 = new ArrayList<>();
        leftColPossibility2.add(4); leftColPossibility2.add(7);
        List<Integer> leftColPossibility3 = new ArrayList<>();
        leftColPossibility3.add(1); leftColPossibility3.add(7);
//        2,5,8 attack
        List<Integer> midColPossibility1 = new ArrayList<>();
        midColPossibility1.add(2); midColPossibility1.add(5);
        List<Integer> midColPossibility2 = new ArrayList<>();
        midColPossibility2.add(5); midColPossibility2.add(8);
        List<Integer> midColPossibility3 = new ArrayList<>();
        midColPossibility3.add(2); midColPossibility3.add(8);
//        3,6,9 attack
        List<Integer> rightColPossibility1 = new ArrayList<>();
        rightColPossibility1.add(3); rightColPossibility1.add(6);
        List<Integer> rightColPossibility2 = new ArrayList<>();
        rightColPossibility2.add(6); rightColPossibility2.add(9);
        List<Integer> rightColPossibility3 = new ArrayList<>();
        rightColPossibility3.add(3); rightColPossibility3.add(9);
//        1,5,9 attack
        List<Integer> negDiagonalPossibility1 = new ArrayList<>();
        negDiagonalPossibility1.add(1); negDiagonalPossibility1.add(5);
        List<Integer> negDiagonalPossibility2 = new ArrayList<>();
        negDiagonalPossibility2.add(5); negDiagonalPossibility2.add(9);
        List<Integer> negDiagonalPossibility3 = new ArrayList<>();
        negDiagonalPossibility3.add(1); negDiagonalPossibility3.add(9);
//        3,5,7 attack
        List<Integer> posDiagonalPossibility1 = new ArrayList<>();
        posDiagonalPossibility1.add(3); posDiagonalPossibility1.add(5);
        List<Integer> posDiagonalPossibility2 = new ArrayList<>();
        posDiagonalPossibility2.add(5); posDiagonalPossibility2.add(7);
        List<Integer> posDiagonalPossibility3 = new ArrayList<>();
        posDiagonalPossibility3.add(3); posDiagonalPossibility3.add(7);

        if(prevCPUMove.containsAll(topRowPossibility1)) {
            if(!playerMove.contains(3)) {
                return 3;
            }
        } else if(prevCPUMove.containsAll(topRowPossibility2)) {
            if(!playerMove.contains(1)) {
                return 1;
            }
        } else if(prevCPUMove.containsAll(topRowPossibility3)) {
            if(!playerMove.contains(2)) {
                return 2;
            }
        } else if(prevCPUMove.containsAll(midRowPossibility1)) {
            if(!playerMove.contains(6)) {
                return 6;
            }
        } else if(prevCPUMove.containsAll(midRowPossibility2)) {
            if(!playerMove.contains(4)) {
                return 4;
            }
        } else if(prevCPUMove.containsAll(midRowPossibility3)) {
            if(!playerMove.contains(5)) {
                return 5;
            }
        } else if(prevCPUMove.containsAll(botRowPossibility1)) {
            if(!playerMove.contains(9)) {
                return 9;
            }
        } else if(prevCPUMove.containsAll(botRowPossibility2)) {
            if(!playerMove.contains(7)) {
                return 7;
            }
        } else if(prevCPUMove.containsAll(botRowPossibility3)) {
            if(!playerMove.contains(8)) {
                return 8;
            }
        } else if(prevCPUMove.containsAll(leftColPossibility1)) {
            if(!playerMove.contains(7)) {
                return 7;
            }
        } else if(prevCPUMove.containsAll(leftColPossibility2)) {
            if(!playerMove.contains(1)) {
                return 1;
            }
            return 1;
        } else if(prevCPUMove.containsAll(leftColPossibility3)) {
            if(!playerMove.contains(4)) {
                return 4;
            }
        } else if(prevCPUMove.containsAll(midColPossibility1)) {
            if(!playerMove.contains(8)) {
                return 8;
            }
        } else if(prevCPUMove.containsAll(midColPossibility2)) {
            if(!playerMove.contains(2)) {
                return 2;
            }
        } else if(prevCPUMove.containsAll(midColPossibility3)) {
            if(!playerMove.contains(5)) {
                return 5;
            }
        } else if(prevCPUMove.containsAll(rightColPossibility1)) {
            if(!playerMove.contains(9)) {
                return 9;
            }
        } else if(prevCPUMove.containsAll(rightColPossibility2)) {
            if(!playerMove.contains(3)) {
                return 3;
            }
        } else if(prevCPUMove.containsAll(rightColPossibility3)) {
            if(!playerMove.contains(6)) {
                return 6;
            }
        } else if(prevCPUMove.containsAll(negDiagonalPossibility1)) {
            if(!playerMove.contains(9)) {
                return 9;
            }
        } else if(prevCPUMove.containsAll(negDiagonalPossibility2)) {
            if(!playerMove.contains(1)) {
                return 1;
            }
        } else if(prevCPUMove.containsAll(negDiagonalPossibility3)) {
            if(!playerMove.contains(5)) {
                return 5;
            }
        } else if(prevCPUMove.containsAll(posDiagonalPossibility1)) {
            if(!playerMove.contains(7)) {
                return 7;
            }
        } else if(prevCPUMove.containsAll(posDiagonalPossibility2)) {
            if(!playerMove.contains(3)) {
                return 3;
            }
        } else if(prevCPUMove.containsAll(posDiagonalPossibility3)) {
            if(!playerMove.contains(5)) {
                return 5;
            }
        }
        return -1;
    }
    //    CPURandomMove method returns CPU selection for random available space (arr index + 1)
    public static int CPURandomMove(ArrayList<Integer> playerMove, ArrayList<Integer> prevCPUMove) {
        List<Integer> allMoves = new ArrayList<>();
        allMoves.add(1);
        allMoves.add(2);
        allMoves.add(3);
        allMoves.add(4);
        allMoves.add(5);
        allMoves.add(6);
        allMoves.add(7);
        allMoves.add(8);
        allMoves.add(9);

        List<Integer> takenMoves = new ArrayList<>();
        takenMoves.addAll(playerMove);
        takenMoves.addAll(prevCPUMove);

        allMoves.removeAll(takenMoves);

        int size = allMoves.size();
        int chooseRand = (int)((Math.random()*size) + 1);

        return allMoves.get(chooseRand - 1);
    }
    //    CPU ASSIST(EXTRA CREDIT)
    public static int CPUAssist(ArrayList<Integer> playerMove, ArrayList<Integer> prevCPUMove) {
        return CPUMove(prevCPUMove, playerMove);
    }
    //adds array into board format and prints board
    public static void printBoard(char[] arr) {
        System.out.println(" " + arr[0] + " | " + arr[1] + " | " + arr[2] + " \n"
                + "---+---+---\n"
                +  " " + arr[3] + " | " + arr[4] + " | " + arr[5] + " \n"
                + "---+---+---\n"
                +  " " + arr[6] + " | " + arr[7] + " | " + arr[8] + " ");
    }
    //adds a selection to the board arr
    public static char[] addSelection(int selection, int player, char[] arr) {
        char symbol = 'O';
        if (player == 1) {
            symbol = 'X';
        }
        arr[selection - 1] = symbol;
        return arr;
    }
    //checks for tie by seeing if all spaces are full
    public static boolean isTie(char[] arr) {
        for (char el : arr) {
            if (el == '1' | el == '2' | el == '3' | el == '4' | el == '5' | el == '6'| el == '7' | el == '8' | el == '9') {
                return false;
            }
        }
        return true;
    }
    //checks for a winner by checking for a match to the
    // 8 possible winning states
    public static boolean isWinner(ArrayList<Integer> arr) {
        List<Integer> winningTopRow = new ArrayList<>();
        winningTopRow.add(1);
        winningTopRow.add(2);
        winningTopRow.add(3);
        List<Integer> winningMidRow = new ArrayList<>();
        winningMidRow.add(4);
        winningMidRow.add(5);
        winningMidRow.add(6);
        List<Integer> winningBottomRow = new ArrayList<>();
        winningBottomRow.add(7);
        winningBottomRow.add(8);
        winningBottomRow.add(9);
        List<Integer> winningRightCol = new ArrayList<>();
        winningRightCol.add(1);
        winningRightCol.add(4);
        winningRightCol.add(7);
        List<Integer> winningMidCol = new ArrayList<>();
        winningMidCol.add(2);
        winningMidCol.add(5);
        winningMidCol.add(8);
        List<Integer> winningLeftCol = new ArrayList<>();
        winningLeftCol.add(3);
        winningLeftCol.add(6);
        winningLeftCol.add(9);
        List<Integer> winningCross = new ArrayList<>();
        winningCross.add(1);
        winningCross.add(5);
        winningCross.add(9);
        List<Integer> winningCrossTwo = new ArrayList<>();
        winningCrossTwo.add(3);
        winningCrossTwo.add(5);
        winningCrossTwo.add(7);

        return arr.containsAll(winningTopRow) ||
                arr.containsAll(winningMidRow) ||
                arr.containsAll(winningBottomRow) ||
                arr.containsAll(winningRightCol) ||
                arr.containsAll(winningMidCol) ||
                arr.containsAll(winningLeftCol) ||
                arr.containsAll(winningCross) ||
                arr.containsAll(winningCrossTwo);
    }
}
