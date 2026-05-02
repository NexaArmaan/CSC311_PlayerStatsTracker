import service.GameService;

public class Main {

    public static void main(String[] args) {

        GameService service = new GameService();

        try {
            service.addGame(1, "Valorant");
            service.addStats(1, 10, 5, 200);
            service.updateStats(1, 15, 6, 250);
            service.deleteStats(1);
            System.out.println("Everything is good");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

