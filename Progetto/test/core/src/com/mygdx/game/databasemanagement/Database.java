package com.mygdx.game.databasemanagement;

import com.mygdx.game.databasemanagement.Enum.DropType;

import java.sql.*;
import java.util.ArrayList;

/**
 * Classe che implementa la gestione del database precedentemente creato. Il DBMS è sqlite. Questa classe mostra solo 3
 * metodi: 2 per stampare i nicknames nella sezione score del gioco e il restante che serve per inserire o rimuovere tuple
 * o coppie di tuple dal database.
 *
 * @author Curcio
 */

public class Database {
    private ArrayList<String> listaGiocatori;

    public Database() {
        listaGiocatori = new ArrayList<String>();
    }

    /**
     * In questo metodo viene interrogato il database. Viene caricato il driver JDBC precedentemente aggiunto alle librerie,
     * creata la connessione ed eseguita la query. Successivamente i record letti verranno aggiunti ad un ArrayList in modo
     * da stampare i primi dieci nella sezione "score". Le tuple vengono lette direttamente in ordine decrescente di punteggio.
     * Sono gestite le com.mygdx.game.eccezioni: SQLException e ClassNotFoundException.
     */
    private void start() {
        try {
            String driver = "org.sqlite.JDBC";
            Class.forName(driver);
            //The main path is in assets
            String url = "jdbc:sqlite:DB.sqlite";
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM GAMES ORDER BY POINTS DESC";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                listaGiocatori.add(rs.getString("NICKNAME") + "              " + rs.getString("POINTS") + "\n\n");
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
    }

    /**
     * Metodo che serve a stampare i primi dieci giocatori della modalità OFFLINE in ordine di punteggio. Se l'Arraylist
     * è vuoto vuol dire che non ha ancora giocato nessuno e verrà visualizzato il relativo messaggio d'errore. Se la
     * dimensione dell'ArrayList è maggiore di 10 memorizzo nella variabile che verrà visualizzata nella sezione score
     * solo i primi 10.
     *
     * @return s: è la stringa che contiene o i nicknames in ordine di punteggio o il messaggio di errore nel caso in cui
     *               l'ArrayList risultasse vuoto.
     */
    public String printTable () {
        start();
        StringBuilder s = new StringBuilder();
        check(s);
        return s.toString();
    }

    /**
     * @param sb: StringBuilder
     */

    private void check(StringBuilder sb) {
        if (listaGiocatori.size() < 10) {
            if (listaGiocatori.size() == 0) {
                sb.append("NO SCORE");
            } else {
                for (int i = 0; i < listaGiocatori.size(); i++) {
                    sb.append(listaGiocatori.get(i));
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                sb.append(listaGiocatori.get(i));
            }
        }
    }

    /**
     * Metodo che dopo aver caricato il driver e creata la connessione permette di inserire o eliminare tuple o coppie di
     * tuple.
     *
     * @param id: numero randomico compreso tra 0 e 1000 generato nella classe "offlinegamescreen" che serve a identificare
     *            univocamnete le varie partite giocate.
     * @param name: nome del giocatore
     * @param points: punteggio del giocatore in questione
     * @param type: parametro che serve a scegliere il tipo di operazione da eseguire
     */
    public String modify(String id, String name, int points, DropType type) {
        String query;
        try {
            String driver = "org.sqlite.JDBC";
            Class.forName(driver);
            String url = "jdbc:sqlite:DB.sqlite";
            Connection conn = DriverManager.getConnection(url);
            switch (type) {
                case INSERT:
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO GAMES VALUES (?, ?, ?)");
                    stmt.setString(1, id);
                    stmt.setString(2, name);
                    stmt.setInt(3, points);
                    stmt.executeUpdate();
                    conn.close();
                    return "Inserito";
                case DROP_PLAYER:
                    Statement stm = conn.createStatement();
                    query = "DELETE FROM GAMES WHERE NICKNAME = '" + name + "'";
                    stm.executeUpdate(query);
                    conn.close();
                    return "Eliminato";
                case DROP_ALL:
                    Statement st = conn.createStatement();
                    query = "DELETE FROM GAMES";
                    st.executeUpdate(query);
                    conn.close();
                    return "Eliminati";
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
        return "Error";
    }

    public ArrayList<String> getListaGiocatori() {
        return listaGiocatori;
    }
}
