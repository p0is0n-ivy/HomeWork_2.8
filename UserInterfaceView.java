package project8;
import java.io.IOException;
import java.util.Scanner;

public class UserInterfaceView {
    private Controller controller = new Controller();

    public void runInterface() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите название города: ");
            String city = scanner.nextLine();

            System.out.println("Введите опцию: 1 - получить текущую погоду, 0 - получить " +
                    "прогноз погоды на 5 дней; 2 - вывести погоду из базы");
            //TODO: добавить опцию получения погоды из базы данных
            String command = scanner.nextLine();

            try {
                controller.getWeather(command, city);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}