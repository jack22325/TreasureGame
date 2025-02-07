/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 *
 */
import java.util.Scanner;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private static boolean treasureFound;
    private boolean menuShownOnce;
    private static boolean cheatMode;
    public static boolean easyMode;

    //Constructor
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        menuShownOnce = false;
        currentTown = null;
        hunter = null;
        hardMode = false;
    }

    // starts the game; this is the only public method
    public void play ()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        System.out.print("Hard mode? (y/n): ");
        String hard = scanner.nextLine();

        if (hard.equals("n")||hard.equals("N"))
        {
            System.out.print("Easy mode? (y/n): ");
            String easy = scanner.nextLine();
            if (easy.equals("y")||easy.equals("Y"))
            {
                easyMode = true;
            }
        }
        if (hard.equals("y") || hard.equals("Y"))
        {
            hardMode = true;
        }
        if (hard.equals("secret cheat mode"))
        {
            cheatMode = true;
        }
    }

    public static boolean isCheatMode() {
        return cheatMode;
    }

    public static boolean isEasyMode() {
        return easyMode;
    }

    public static boolean getTreasureFound()
    {
        return treasureFound;
    }

    public static void setTreasureFoundToTrue()
    {
        treasureFound = true;
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown()
    {
        treasureFound = false;
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode)
        {
            // in hard mode, you get less money back when you sell items
            markdown = 0.5;

            // and the town is "tougher"
            toughness = 0.75;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String choice = "";
        {
            while ((!(choice.equals("X") || choice.equals("x")))&&(!Town.foundAllThree())&&(!Town.isHasNoGold()))
            {
                if (!menuShownOnce){
                    System.out.println();
                System.out.println(currentTown.getLatestNews());
                System.out.println("***");
                System.out.println(hunter);
                System.out.println(currentTown);
                System.out.println("(B)uy something at the shop.");
                System.out.println("(S)ell something at the shop.");
                System.out.println("(M)ove on to a different town.");
                System.out.println("(L)ook for trouble!");
                System.out.println("(H)unt for treasure!");
                System.out.println("(C)asino ");
                System.out.println("Give up the hunt and e(X)it.");
                System.out.println();
                System.out.print("What's your next move? ");
                choice = scanner.nextLine();
                choice = choice.toUpperCase();
                processChoice(choice);
                menuShownOnce = true;
                }
                System.out.println();
                System.out.println(currentTown.getLatestNews());
                System.out.println("***");
                System.out.println(hunter);
                System.out.println(currentTown);
                System.out.print("(B)");
                System.out.print("(S)");
                System.out.print("(M)");
                System.out.print("(L)");
                System.out.print("(H)");
                System.out.println("(C)");
                System.out.print("(X)");
                System.out.println();
                System.out.print("What's your next move? ");
                choice = scanner.nextLine();
                choice = choice.toUpperCase();
                processChoice(choice);
            }
        }

    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice)
    {
        if (choice.equals("B") || choice.equals("b") || choice.equals("S") || choice.equals("s"))
        {
            currentTown.enterShop(choice);
        }
        else if (choice.equals("M") || choice.equals("m"))
        {
            if (currentTown.leaveTown())
            {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        }
        else if (choice.equals("L") || choice.equals("l"))
        {
            currentTown.lookForTrouble();
        }
        else if (choice.equals("X") || choice.equals("x"))
        {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }
        else if (choice.equals("H") || choice.equals("h"))
        {
            currentTown.treasureHunt();
        }
        else if (choice.equals("C") || choice.equals("c"))
        {
            currentTown.Casino();
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}