package ru.gravitana.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '•';
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static int[] lustTurnCoordinates = new int[2];

    private static final int WIN_COUNT = 4; // Выигрышная комбинация
    private static final int FIELD_SIZE = 5; // Размер поля

    /**
     * Инициализация объектов игры
     */
    static void initialize(){
        fieldSizeX = FIELD_SIZE;
        fieldSizeY = FIELD_SIZE;
        field = new char[fieldSizeX][fieldSizeY];

        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++){
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++){
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn(){
        int x;
        int y;
        do {
            System.out.print("Введите координаты хода X и Y\n(от 1 до " + FIELD_SIZE + ") через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
        lustTurnCoordinates = new int[]{x, y};
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn(){
        int x;
        int y;
        do{
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
        lustTurnCoordinates = new int[]{x, y};
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка валидности координат хода
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Поверка на ничью (все ячейки игрового поля заполнены фишками человека или компьютера)
     */
    static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * Метод проверки победы
     * @param dot фишка игрока
     * @return результат проверки победы
     */
    static boolean checkWin(char dot){
        int chainLen;
        int lastX = lustTurnCoordinates[0];
        int lastY = lustTurnCoordinates[1];

        // Проверка горизонтали
        chainLen = 0;
        for (int x = 0; x < fieldSizeX; x++) {
            if (field[x][lastY] == dot) {
                    chainLen++;
            } else {
                chainLen = 0;
            }

            if (chainLen >= WIN_COUNT) {
                return true;
            }
        }

        // Проверка вертикали
        chainLen = 0;
        for (int y = 0; y < fieldSizeY; y++) {
            if (field[lastX][y] == dot) {
                chainLen++;
            } else {
                chainLen = 0;
            }

            if (chainLen >= WIN_COUNT) {
                return true;
            }
        }

        // Проверка диагонали "\"
        chainLen = 0;
        int delta1;
        int delta2;

        if (lastX < lastY) {
            delta1 = lastX - 1;
            delta2 = FIELD_SIZE - lastY;
        } else {
            delta1 = lastY - 1;
            delta2 = FIELD_SIZE - lastX;
        }
        int xStart = lastX - delta1 - 1;
        int yStart = lastY - delta1 - 1;
        int xEnd = lastX + delta2 - 1;
//        int yEnd = lastY + delta2 - 1;

        for (int x = xStart, y = yStart; x < xEnd; x++, y++) {
            if (field[x][y] == dot) {
                chainLen++;
            } else {
                chainLen = 0;
            }

            if (chainLen >= WIN_COUNT) {
                return true;
            }
        }

        // Проверка диагонали "/"
        chainLen = 0;
        int xyKf = lastX + lastY;

        xStart = xyKf;
        yStart = 0;

        if (xStart >= FIELD_SIZE) {
            xStart = FIELD_SIZE - 1;
            yStart = xyKf - xStart;
        }

//        System.out.println("lastX=" + lastX + " lastY=" + lastY);
//        System.out.println("xStart=" + xStart + " yStart=" + yStart);

        for (int x = xStart, y = yStart; x > 0 && y < FIELD_SIZE; x--, y++) {
            if (field[x][y] == dot) {
                chainLen++;
            } else {
                chainLen = 0;
            }

            if (chainLen >= WIN_COUNT) {
                return true;
            }
        }

        return false;
    }


    /**
     * Проверка состояния игры
     * @param dot фишка игрока
     * @param s победный слоган
     * @return состояние игры
     */
    static boolean checkState(char dot, String s){
        if (checkWin(dot)){
            System.out.println(s);
            return true;
        }
        else if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        // Игра продолжается
        return false;
    }

    public static void main(String[] args) {
        do {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkState(DOT_AI, "Вы проиграли!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
        } while (scanner.next().equalsIgnoreCase("Y"));
    }

}
