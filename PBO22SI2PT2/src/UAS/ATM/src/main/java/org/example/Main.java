package org.example;
import java.io.Console;
import java.util.*;

import static org.example.JenisOperasi.getTimeStamp;
import static org.example.JenisOperasi.loginSuccess;
import static org.example.QueueHistory.historyQueue;

public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Console console = System.console();

        List<String> data = RekeningNode.getDataFromCsv();

        System.out.println(getTimeStamp());

        boolean breakLoop = false;
        boolean menu = true;

        while (!breakLoop) {
            if (menu) {
                System.out.println("Menu:");
                System.out.println("1. Login");
                System.out.println("2. Exit [Stop Program]");
                System.out.print("Pilih menu: ");
            }
            menu = true;

            String menuChoice = input.nextLine();


            if (menuChoice.equals("1")) {
                boolean loginSuccess = false;

                while (!loginSuccess) {
                    String noRekening = "";
                    String sandi = "";

                    if (console == null) {
                        System.out.println("#########################");
                        System.out.print("Masukkan nomor rekening: ");
                        noRekening = input.nextLine();
                        System.out.print("Masukkan sandi: ");
                        sandi = input.nextLine();
                    } else {
                        noRekening = console.readLine("Masukkan nomor rekening: ");
                        sandi = new String(console.readPassword("Masukkan sandi: "));
                    }

                    for (String item : data) {
                        String[] arr = item.split(",");
                        String norek = arr[0].replace("[", "");
                        String passw = arr[2].replace(" ", "");

                        if (noRekening.equals(norek) && sandi.equals(passw)) {
                            historyQueue.add(new History(norek, "Login", "Login berhasil", getTimeStamp()));
                            System.out.println("Login berhasil");
                            loginSuccess(norek, data);
                            loginSuccess = true;
                        }
                    }

                    if (!loginSuccess) {
                        System.out.println("Login gagal. Silakan coba lagi.");
                    }
                }
            } else if (menuChoice.equals("2")) {
                breakLoop = true;
                System.out.println("Program dihentikan.");
            } else {
                menu = false;
            }
        }
    }



}