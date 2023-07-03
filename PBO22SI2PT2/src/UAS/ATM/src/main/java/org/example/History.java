package org.example;

public class History {
    String noRekening;
    String jenisOperasi;
    String Keterangan;
    String CreatedOn;

    public History(String noRekening, String jenisOperasi, String Keterangan, String CreatedOn) {
        this.noRekening = noRekening;
        this.jenisOperasi = jenisOperasi;
        this.Keterangan = Keterangan;
        this.CreatedOn = CreatedOn;
    }
}