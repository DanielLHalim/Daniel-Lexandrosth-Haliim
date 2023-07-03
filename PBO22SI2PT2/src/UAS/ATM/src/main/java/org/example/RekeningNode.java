package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.text.NumberFormat;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.example.JenisOperasi.getTimeStamp;
import static org.example.QueueHistory.historyQueue;

public class RekeningNode {
    public static Scanner scanner = new Scanner(System.in);
    public static String idrFormat(int amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }

    public static LinkedList<String> getDataFromCsv() {
        String csvFile = "src/main/resources/data/rekening.csv";
        CSVReader reader = null;
        LinkedList<String> data = new LinkedList<>();

        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;

            while ((line = reader.readNext()) != null) {
                for (String cell : line) {
                    // ignore header
                    if (cell.equals("NomorRekening;NamaPemilik;Sandi;Saldo")) {
                        continue;
                    }
                    String[] item = cell.split(";");
                    Rekening rekening = new Rekening(item[0], item[1], item[2], item[3]);
                    data.add(Arrays.toString(new String[]{rekening.noRekening, rekening.namaPemilik, rekening.sandi, rekening.saldo}));
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static void getBalance(String noRek, List data) {
        for (Object item : data) {
            String[] arr = item.toString().split(",");
            String norek = arr[0].replace("[", "");
            String nama = arr[1].replace(" ", "");
            String saldo = arr[3].replace("]", "").replace(" ", "");
            int amount = Integer.parseInt(saldo);
            if (noRek.equals(norek)) {
                System.out.println("No rekening anda: " + norek);
                System.out.println("Nama Pemilik: " + nama);
                System.out.println("Saldo anda: " + idrFormat(amount));
            }
        }

        System.out.println("\nPress enter to continue...");
        scanner.nextLine();
    }

    public static void withdraw(String noRek, List data) {
        System.out.print("Masukkan jumlah penarikan: Rp.");
        int amount = scanner.nextInt();
        scanner.nextLine();

        for (Object item : data) {
            String[] arr = item.toString().split(",");
            String norek = arr[0].replace("[", "");
            String nama = arr[1].replace(" ", "");
            String saldo = arr[3].replace("]", "").replace(" ", "");
            int saldoInt = Integer.parseInt(saldo);
            if (noRek.equals(norek)) {
                if (amount > saldoInt) {
                    System.out.println("Saldo anda tidak cukup");
                } else {
                    int newSaldo = saldoInt - amount;
                    System.out.println("Penarikan berhasil");
                    System.out.println("Saldo anda: " + idrFormat(newSaldo));
                    data.set(data.indexOf(item), Arrays.toString(new String[]{norek, nama, "root", String.valueOf(newSaldo)}));
                    historyQueue.add(new History(norek, "Withdraw", "Penarikan Sebesar " + idrFormat(amount) , getTimeStamp()));
                }
            }
        }

        System.out.println("\nPress enter to continue...");
        scanner.nextLine();
    }

    public static void deposit(String noRek, List data) {
        int amount = 0;

        while (true) {
            System.out.print("Masukkan jumlah penyetoran (kelipatan 100.000, maksimum 10.000.000): Rp.");
            amount = scanner.nextInt();
            scanner.nextLine();
            if (amount % 100000 == 0 && amount <= 10000000) {
                break;
            }
            System.out.println("Jumlah penyetoran harus kelipatan 100.000 dan tidak boleh melebihi 10.000.000");
        }

        for (Object item : data) {
            String[] arr = item.toString().split(",");
            String norek = arr[0].replace("[", "");
            String nama = arr[1].replace(" ", "");
            String saldo = arr[3].replace("]", "").replace(" ", "");
            int saldoInt = Integer.parseInt(saldo);
            if (noRek.equals(norek)) {
                int newSaldo = saldoInt + amount;
                System.out.println("Penyetoran berhasil");
                System.out.println("Saldo anda: " + idrFormat(newSaldo));
                data.set(data.indexOf(item), Arrays.toString(new String[]{norek, nama, "root", String.valueOf(newSaldo)}));
                historyQueue.add(new History(norek, "Deposit", "Penyetoran Sebesar " + idrFormat(amount) , getTimeStamp()));
            }
        }

        System.out.println("\nPress enter to continue...");
        scanner.nextLine();
    }

    public static void transfer(String noRek, List data) {
        System.out.print("Masukkan nomor rekening tujuan: ");
        String noRekTujuan = scanner.nextLine();
        System.out.print("Masukkan jumlah transfer: Rp.");
        int amount = scanner.nextInt();
        scanner.nextLine();

        boolean isExist = false;

        for (Object item : data) {
            String[] arr = item.toString().split(",");
            String norek = arr[0].replace("[", "");
            String nama = arr[1].replace(" ", "");
            if (noRekTujuan.equals(norek)) {
                System.out.println("\nNomor Rekening Tujuan: " + norek);
                System.out.println("Nama Pemilik: " + nama);
                System.out.println("Nominal Transfer: " + idrFormat(amount));
                isExist = true;
            }
        }

        if (!isExist) {
            System.out.println("Nomor rekening tujuan tidak ditemukan");
            System.out.println("\nPress enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.print("\nApakah anda yakin ingin melanjutkan transfer? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equals("yes")) {
            for (Object item : data) {
                String[] arr = item.toString().split(",");
                String norek = arr[0].replace("[", "");
                String nama = arr[1].replace(" ", "");
                String saldo = arr[3].replace("]", "").replace(" ", "");
                int saldoInt = Integer.parseInt(saldo);
                if (noRek.equals(norek)) {
                    if (amount > saldoInt) {
                        System.out.println("Saldo anda tidak cukup");
                    } else {
                        int newSaldo = saldoInt - amount;
                        System.out.println("Transfer berhasil");
                        System.out.println("Saldo anda: " + idrFormat(newSaldo));
                        data.set(data.indexOf(item), Arrays.toString(new String[]{norek, nama, "root", String.valueOf(newSaldo)}));
                        historyQueue.add(new History(norek, "Transfer", "Transfer Sebesar " + idrFormat(amount) + " Ke " + noRekTujuan, getTimeStamp()));
                    }
                }
            }

            for (Object item : data) {
                String[] arr = item.toString().split(",");
                String norek = arr[0].replace("[", "");
                String nama = arr[1].replace(" ", "");
                String saldo = arr[3].replace("]", "").replace(" ", "");
                int saldoInt = Integer.parseInt(saldo);
                if (noRekTujuan.equals(norek)) {
                    int newSaldo = saldoInt + amount;
                    data.set(data.indexOf(item), Arrays.toString(new String[]{norek, nama, "root", String.valueOf(newSaldo)}));
                }
            }

            System.out.println("\nPress enter to continue...");
            scanner.nextLine();
        } else {
            System.out.println("Transfer dibatalkan");
        }
    }
}
