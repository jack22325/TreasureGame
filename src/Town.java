/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String treasure;
    private static boolean foundDiamond;
    private static boolean foundRuby;
    private static boolean foundEmerald;
    private static boolean hasNoGold;

    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        int randomNum = (int) (Math.random()*3 + 1);
        if(randomNum == 1)
        {
            treasure = "diamond";
        }
        else if(randomNum == 2)
        {
            treasure = "ruby";
        }
        else if(randomNum == 3)
        {
            treasure = "emerald";
        }
    }

    public String getTreasure() {
        return treasure;
    }

    public static boolean foundAllThree()
    {
        return foundDiamond&&foundRuby&&foundEmerald;
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    public static boolean isHasNoGold() {
        return hasNoGold;
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        if(TreasureHunter.isCheatMode())
        {
            printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
            printMessage += "\nYou won the brawl and receive " +  100 + " gold.";
            hunter.changeGold(100);
        }
        else if (TreasureHunter.isEasyMode())
        {
            double noTroubleChance;
            if (toughTown)
            {
                noTroubleChance = 0.66;
            }
            else
            {
                noTroubleChance = 0.33;
            }

            if (Math.random()/2 > noTroubleChance)
            {
                printMessage = "You couldn't find any trouble";
            }
            else {
                printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
                int goldDiff = (int) (Math.random() * 30) + 1;
                double chance = Math.random() * 2;
                if (chance> noTroubleChance) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + goldDiff + " gold.";
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                    hunter.changeGold(-1 * goldDiff);
                    if (hunter.getGold() == 0) {
                        System.out.println("You now have 0 goldj.");
                        hasNoGold = true;
                    }
                }
            }
        } else
        {
            double noTroubleChance;
            if (toughTown)
            {
                noTroubleChance = 0.66;
            }
            else
            {
                noTroubleChance = 0.33;
            }

            if (Math.random() > noTroubleChance)
            {
                printMessage = "You couldn't find any trouble";
            }
            else
            {
                printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
                int goldDiff = (int)(Math.random() * 10) + 1;
                if (Math.random() > noTroubleChance)
                {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                    hunter.changeGold(goldDiff);
                }
                else
                {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                    hunter.changeGold(-1 * goldDiff);
                    if (hunter.getGold() == 0) {
                        System.out.println("You now have 0 gold.");
                        hasNoGold = true;
                    }
                }
            }
        }
    }



    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public void treasureHunt()
    {
        if(TreasureHunter.getTreasureFound())
        {
            System.out.println("You have to go to another town.");
        }
        else
        {
            int randomNum = (int) (Math.random()*2 + 1);
            if(randomNum == 1)
            {
                System.out.println(treasure);
                if(treasure.equals("diamond"))
                {
                    if(foundDiamond)
                    {
                        System.out.println("Already found a diamond.");
                    }
                    else
                    {
                        foundDiamond = true;
                    }
                }
                if(treasure.equals("ruby"))
                {
                    if(foundRuby)
                    {
                        System.out.println("Already found a ruby.");
                    }
                    else
                    {
                        foundRuby = true;
                    }
                }
                if(treasure.equals("emerald"))
                {
                    if(foundEmerald)
                    {
                        System.out.println("Already found an emerald.");
                    }
                    else
                    {
                        foundEmerald = true;
                    }
                }
            }
            else
            {
                System.out.println("No treasure here.");
            }
        }
        TreasureHunter.setTreasureFoundToTrue();
        if(foundDiamond&&foundRuby&&foundEmerald)
        {
            System.out.println("You found all three treasures, congratulations!");
        }
    }
}
