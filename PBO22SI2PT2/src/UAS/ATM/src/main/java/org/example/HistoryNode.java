package org.example;

import java.util.List;

import static org.example.RekeningNode.scanner;

public class HistoryNode {
    public static void getHistoryTransaction(String noRel, List<History> historyQueue) {
        // Linear sort
        for (int i = 0; i < historyQueue.size() - 1; i++) {
            for (int j = i + 1; j < historyQueue.size(); j++) {
                History history1 = historyQueue.get(i);
                History history2 = historyQueue.get(j);
                // acs
                if (history1.noRekening.equals(noRel) && history2.noRekening.equals(noRel) && history2.CreatedOn.compareTo(history1.CreatedOn) > 0) {
                    historyQueue.set(i, history2);
                    historyQueue.set(j, history1);
                    history1 = history2;
                }
            }
        }

        // Print the History objects with matching noRekening
        for (History history : historyQueue) {
            if (history.noRekening.equals(noRel)) {
                System.out.println(history.noRekening + " " + history.jenisOperasi + " " + history.Keterangan + " " + history.CreatedOn);
            }
        }

        System.out.println("\nPress enter to continue...");
        scanner.nextLine();
    }
}
