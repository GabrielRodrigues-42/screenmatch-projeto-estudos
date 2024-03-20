package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer season;
    private String title;
    private double rating;
    //private String plot;
    private Integer episodeN;
    private LocalDate released;

    public Episodio(Integer numberS, DadosEpisodio dados) {
        this.season = numberS;
        this.title = dados.title();

        try {
            this.rating = Double.valueOf(dados.rating());
        } catch(NumberFormatException ex) {
            this.rating = -1.0;
        }
        //this.plot = dados.plot();

        try {
            this.released = LocalDate.parse(dados.date());
        } catch(DateTimeParseException ex) {
            this.released = null;
        }
        this.episodeN = dados.number();
    }

    public LocalDate getReleased() {
        return released;
    }

    public Integer getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public Integer getEpisodeN() {
        return episodeN;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", rating=" + rating +
 //               ", plot='" + plot + '\'' +
                ", episodeN=" + episodeN +
                ", released=" + released
               ;
    }
}
